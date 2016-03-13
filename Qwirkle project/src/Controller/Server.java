package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import model.Game;
import model.Move;
import model.Player;
import model.Tile;
import model.TileBag;

public class Server {

	private static final int GAMESIZE = 4;
	private Game game;
	private boolean running;
	private boolean setup;
	private static int portNumber = 4444;
	private ArrayList<PlayerHandler> players;
	private int skipCount;
	private int AItime = 3000;

	/**
	 * constructor for Server, it has two states: running (which indicates if
	 * the server is running or not)and setup (which indicates if the setup is
	 * complete). The Server also has an own version of the game, list of
	 * players in the game and a skipcount to keep track of skips that would
	 * indicate a possible end game.
	 */
	public Server() {
		running = true;
		game = new Game();
		players = new ArrayList<PlayerHandler>();
		skipCount = 0;
		setup = false;
	}

	// TODO public void broadcast send message to all players
	// TODO public void kick (moet dit in game of in de server?)

	/**
	 * the run method of Client, when started it will ask the serverowner through the console,
	 * which port number he wants to use. After the setup is complete, run will listen to the
	 * PlayerHandlers that are currently connected and will act accordingly to what the
	 * PlayerHandlers send to the Server.
	 * 
	 */
	public void run() {
		ServerSocket serverSocket = null;
		try {
//			TODO
			boolean portNumberSet = false;
			Scanner scan = new Scanner(System.in);
			System.out.println("What port number would you like to host the game on? ");
			while (scan.hasNext() && !portNumberSet) {
				String lline = scan.nextLine();
				String[] words = lline.split(" ");
				if (words[0].matches("^\\d{4}$")) {
					portNumber = Integer.parseInt(words[0]);
					System.out.println("The game will be hosted on port number " + portNumber);
					break;
				} else {
					System.out.println(words[0] +
							  " Is not a valid port number. Please try something else... ");
				}
			}
			System.out.println("Setting up server on portnumber " + portNumber + "...");
			int p = 0;
			serverSocket = new ServerSocket(portNumber);
			portNumber = serverSocket.getLocalPort();
			// if connection is closed, use serverSocket.close();

			while (running) {
				while (!setup) {
					Socket clientSoc;
					if (players.size() < GAMESIZE) {
						clientSoc = serverSocket.accept();
						PlayerHandler playerHandler = new PlayerHandler(
								  clientSoc, this);
						players.add(playerHandler);
						new Thread(playerHandler).start();
						System.out.println("Server is set.");
					}
					boolean allSet = true;
					for (PlayerHandler pH : players) {
						System.out.println("Still busy...");
						System.out.println("player " + pH.getplayer()
								  + " is selected");
						if (pH.getplayer() == null) {
							allSet = false;
							System.out.println("allSet= " + allSet);
						}
					}
					if (allSet) {
						System.out.println("Let's Go!");
						startGame();

					}
				}
				// addHandler(playerHandler);
			}
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * kickPlayerFromLobby version which is executed when kickPlayerFromLobby is not given
	 * a reason for kicking.
	 * @param p
	 */
	public void kickPlayerFromLobby(PlayerHandler p) {
		kickPlayerFromLobby(p, "Invalid information has been send to the Server.");
	}

	/**
	 * removes the Player p from the game and broadcasts the reason for kicking to the 
	 * PlayerHandlers of the players (including the currently kicked player). 
	 * The Tiles that the player p had in possession will be added to the TileBag again.
	 * @param p
	 * @param reason
	 */
	public void kickPlayerFromLobby(PlayerHandler p, String reason) {
		System.out.println("Kicking player " + p + " for " + reason);
		if (players.contains(p.getplayer())) {
			int tilesBackToBag = p.getplayer().getHand().size();
			broadCast("KICKED " + game.getPlayerNr(p.getplayer()) + " "
					  + tilesBackToBag + " " + reason);
			game.kickFromGame(p.getplayer());
			players.remove(p);
			p.quit();
		}
	}

	/**
	 * the broadcast method that is used for broadcasting in all the Server methods,
	 * it broadcasts the String s to all the PlayerHandlers in the players.
	 * @param s
	 */
	public void broadCast(String s) {
		System.out.println("Broadcasting: " + s);
		for (PlayerHandler p : players) {
			p.writeMe(s);
		}
	}

	/**
	 * getter for the Server's Game.
	 * @return game
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * broadcasts the player number of the player that has the upcoming turn.
	 */
	public void sendNext() {
		System.out.println("NEXT send");
		game.nextTurn();
		broadCast("NEXT " + game.getTurn());
	}

	/**
	 * broadcasts the received turn (a collection of moves given in set) 
	 * of Player p to the other PlayerHandlers.
	 * @param p
	 * @param set
	 */
	//TODO
	public void sendTurn(Player p, Set<Move> set) {
		System.out.println("Sending TURN");
		System.out.println("set contains: " + set.toString());
		StringBuilder sb = new StringBuilder();
		for (Move move : set) {
			sb.append(" " + move.toString());
		}
		broadCast("TURN " + game.getPlayerNr(p) + sb.toString());
	}

	/**
	 * starts the game by drawing Tiles for the player hands and executing sendNames and sendNext.
	 */
	public void startGame() {
		TileBag tilebag = game.getTileBag();
		for (PlayerHandler p : players) {
			Set<Tile> tiddies = tilebag.drawSix();
			p.getplayer().setHand(tiddies);
			p.sendTiles(tiddies);
		}
		setup = true;
		sendNames();
		sendNext();
	}

	/**
	 * broadcast the Names of all the players with their player numbers.
	 */
	public void sendNames() {
		StringBuilder sb = new StringBuilder();
		sb.append("NAMES");
		for (PlayerHandler p : players) {
			sb.append(" " + p.getplayer().getName() + " "
					  + game.getPlayerNr(p.getplayer()));
		}
		sb.append(" " + AItime);
		broadCast(sb.toString());
	}

	/**
	 * keeps track of how many players skipped in succession
	 * if every player has skipped once a winner will be determined.
	 */
	public void skipped() {
		System.out.println("SKIP called");
		if (skipCount >= game.getPlayerAmount()) {
			int score = 0;
			ArrayList<Player> peoples = new ArrayList<Player>();

			for (Map.Entry<Player, Integer> p : game.getScores().entrySet()) {
				if (p.getValue() > score) {
					peoples = new ArrayList<Player>();
					peoples.add(p.getKey());
					score = p.getValue();
				} else if (p.getValue() == score) {
					peoples.add(p.getKey());
				}
			}
			if (peoples.size() != 1) {
				broadCast("WINNER " + -1);
			} else {
				broadCast("WINNER " + game.getPlayerNr(peoples.get(0)));
			}
		} else {
			skipCount++;
		}
	}

	/**
	 * resets the skipCount back to zero.
	 */
	public void skipReset() {
		skipCount = 0;
	}

	public static void main(String[] args) {
		new Server().run();
	}

}
