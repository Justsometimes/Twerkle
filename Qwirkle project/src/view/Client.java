package view;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import model.Coord;
import model.Move;
import model.Player;
import model.Tile;

public class Client implements Runnable {

	private int port;
	private Socket sock;
	private String hostName;
	private BufferedReader in;
	private BufferedWriter out;
	private boolean running;
	private boolean joined;
	private Player player;
	private int playernum;
	private int gameSize;
	private int AItime;

//	/**
//	 *  a preset constructor for Client
//	 */
//	public Client() {
//		this("localhost", 4444, new Player("Bert", new HashSet<Tile>()));
//	}

	/**
	 * constructor for Client, it uses an address and port (number) to connect to 
	 * and requires a Player to represent a player.
	 * @param address
	 * @param port
	 * @param player
	 */
	//String address, int port, Player player
	public Client() {
//		this.player = player;
//		this.hostName = address;
//		this.port = port;
		running = true;
		joined = false;

//		try {
//			sock = new Socket(address, port);
//			in = new BufferedReader(
//					new InputStreamReader(sock.getInputStream()));
//			out = new BufferedWriter(new OutputStreamWriter(
//					sock.getOutputStream()));
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}

	/**
	 * the run method of Client, when started it will ask input from the player,
	 * a name for the player, a port he wants to play on and a host address he wants to
	 * connect to.
	 * After the setup it constantly checks if it has received messages 
	 * and acts accordingly by executing the method that belongs to the received protocol. 
	 */
	@Override
	public void run() {
		Scanner scan = new Scanner(System.in);
		boolean nameSet = false;
		boolean portSet = false;
		System.out
				  .println("What name would you like to play as? "
						 + "\nYour name will have to consist of"
						 + " 1 to 16 upper and lower case letters.");
		while (scan.hasNext()) {
			String setupLine = scan.nextLine();
			String[] words = setupLine.split(" ");
			if (!nameSet) {
				if (words[0].matches("^[a-zA-Z]{1,16}$")) {
					this.player = new Player(words[0], new HashSet<Tile>());
					nameSet = true;
					System.out
							.println("What port number would you like to play the game on? ");
					continue;
				} else {
					System.out
							  .println("The given name does not follow the given requirements. "
									+ "Please try again.");
				}
			} else if (!portSet) {
				if (words[0].matches("\\d{4}")) {
					this.port = Integer.parseInt(words[0]);
					System.out
							  .println("The game will be played on port number "
									+ port);
					portSet = true;
					System.out
							.println("What address would you like to connect to? ");
					continue;
				} else {
					System.out
							  .println(words[0]
									+ " Is not a valid port number. Please try something else... ");
				}
			} else {
				if (words[0]
						  .matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.)"
								+ "{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|^localhost$")) {
					this.hostName = words[0];
					// TODO
					System.out.println();
					try {
						sock = new Socket(hostName, port);
						in = new BufferedReader(new InputStreamReader(
								sock.getInputStream()));
						out = new BufferedWriter(new OutputStreamWriter(
								sock.getOutputStream()));

					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				} else {
					System.out
							  .println(words[0]
									+ " Is not a valid IP address. Please try something else...");
				}
			}
		}
		sendHello();
		while (running) {
			try {
				String lline;
				while ((lline = in.readLine()) != null) {
					System.out.println("Received: " + lline);
					String[] elements = lline.split(" ");
					switch (elements[0]) {
						case "WELCOME":
							readWelcome(elements);
							break;
						case "NAMES":
							readNames(elements);
							break;
						case "NEXT":
							readNext(elements);
							break;
						case "TURN":
							readTurn(elements);
						// TODO
							System.out.println("TURN is triggered");
							break;
						case "KICK":
							readKick(elements);
							break;
						case "WINNER":
							readWinner(elements);
							break;
						case "NEW":
							readNew(elements);
						default:
							break;
					}
					// implement commands to be send?;
				}
			} catch (SocketException e) {
				e.printStackTrace();
				System.out.println("The server has closed the connection,"
						  + " the client will now shutdown");
				running = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * if run has read NEW as the first element of a received message,
	 * readNew will handle the protocol further. It reads which new Tiles were send and
	 * adds them to the hand of Client's Player.
	 * @param elements
	 */
	// @ requires elements != null && elements > 1;
	// @ loop_invariant 1 <= i && i < elements.getLength;
	// @ loop_invariant (\forall int i; 1<=i && i<elements.lenght;
	// Tile.buildTile(elements[j]) != null;
	// @ loop_invariant (\forall int j; 1<=j && j<i;
	// Tile.buildTile(elements[j]).tileInHand(player.getHand);
	// @ ensures player.getHand().containsAll(\old(player.getHand()));
	private void readNew(String[] elements) {
		if (elements != null && elements.length > 0) {
			for (int i = 1; i < elements.length; i++) {
				player.addToHand(Tile.buildTile(elements[i]));
			}
			System.out.println("Tiles are added to the players hand");
		}
	}

	/**
	 * if run has read WINNER as the first element of a received message,
	 * readWinner will handle the protocol further. It reads which Player has won
	 * and relays it to the Client's player through a println.
	 * @param elements
	 */
	// @ pure;
	// @ requires elements != null && elements.length == 2;
	private void readWinner(String[] elements) {
		if (elements.length == 2) {
			if (Integer.parseInt(elements[1]) == playernum) {
				System.out.println("You won!");
			} else {
				System.out.println("You lost! Winning player: " + elements[1]);
			}
		} else {
			// exception for incomplete or faulty WINNER message
		}
	}

	/**
	 * if run has read KICK as the first element of a received message,
	 * readKick will handle the protocol further. It reads which player has been kicked
	 * and relays it back to the Client's player,
	 * @param elements
	 */
	// @ pure;
	// @ requires elements != null;
	private void readKick(String[] elements) {
		if (elements != null && elements.length >= 3) {
			StringBuilder sb = new StringBuilder();
			for (int i = 3; i < elements.length; i++) {
				sb.append(" " + elements[i]);
			}
			if (Integer.parseInt(elements[1]) == playernum) {
				System.out
						  .println("You have been kick from the server.\n  Reason:"
								+ sb.toString());
			} else {
				System.out.println("Player " + elements[1]
						  + " has been kicked from the server.\n Reason:"
						  + sb.toString());
			}
		} else {
			// exception for incomplete KICK received.
		}
	}

	/**
	 * if run has read TURN as the first element of a received message,
	 * readTurn will handle the protocol further. It reads what moves have been made 
	 * by another player and places them on the client's board.
	 * @param elements
	 */
	// @ requires elements != null &&(elements.length-1)%3 ==0;
	// @ loop_invariant (\forall int i = 1; i<elements.length/3;
	// Tile.buildTile(elements[i])!=null && new
	// Coord(Integer.parseInt(elements[i + 1]),Integer.parseInt(elements[i + 2])
	// != null;
	// @ ensures (\forall int j; 1<j && j<i;
	// player.getBoard.getUsedSpaces.contains(new
	// Move(Tile.buildTile(elements[i]), new Coord(Integer.parseInt(elements[i +
	// 1]),Integer.parseInt(elements[i + 2])))
	private void readTurn(String[] elements) {
		if (elements != null && (elements.length - 2) % 3 == 0) {
			for (int i = 0; i < (elements.length - 2) / 3; i++) {
				Tile tile = Tile.buildTile(elements[2 + (i * 3)]);
				Coord coord = new Coord(
						  Integer.parseInt(elements[3 + (i * 3)]),
						  Integer.parseInt(elements[4 + (i * 3)]));
				player.getBoard().boardAddMove(new Move(tile, coord));
			}
			System.out.println("Another player's turn has been received.");
		} else {
			// exception for incomplete TURN received.
		}
	}

	/**
	 * if run has read NEXT as the first element of a received message,
	 * readNext will handle the protocol further. It reads which player has the next turn,
	 * if it is the Client's player, makeMove will be executed, so the player can make a turn.
	 * @param elements
	 */
	// @ pure;
	// @ requires elements != null && elements.length == 2;
	private void readNext(String[] elements) {
		if (elements != null && elements.length == 2) {
			if (Integer.parseInt(elements[1]) == playernum) {
				makeMove();
			}
		} else {
			// exception for incomplete NEXT received.
		}
	}


	/**
	 * if run has read NAMES as the first element of a received message,
	 * readNames will handle the protocol further. It reads names of the other players and
	 * displays them to the Client's Player through a println.
	 * @param elements
	 */
	// @ requires elements != null && elements.length % 2 == 0 &&
	// elements.length >= 4;
	// @ ensures AItime != null;
	private void readNames(String[] elements) {
		if (elements != null && elements.length % 2 == 0
			  	  && elements.length >= 4) {
			for (int i = 0; i < (elements.length - 2) / 2; i++) {
				System.out.println("Player: " + elements[i * 2 + 2] + " - "
				  		  + elements[i * 2 + 1]);
			}
			gameSize = Integer.parseInt(elements[elements.length - 2]);
			AItime = Integer.parseInt(elements[elements.length - 1]);
			System.out.println("AITime = " + AItime);
		} else {
			// exception for incomplete NAMES.
		}
	}

	/**
	 * if run has read WELCOME as the first element of a received message,
	 * readNames will handle the protocol further. It sets the state joined of the client to true
	 * and displays the player number that is given with the WELCOME message to the client's
	 * player through a println. 
	 * @param elements
	 */
	// @requires elements != null && elements.length ==3;
	private void readWelcome(String[] elements) {
		if (elements != null && elements.length == 3) {
			if (elements[1].equals(player.getName())) {
				joined = true;
				playernum = Integer.parseInt(elements[2]);
				System.out.println("You are player " + playernum);
			}
		} else {
			// exception for incomplete WELCOME.
		}
	}

	/**
	 * lets the player create a Move collection by executing readClientPlayerMove()
	 * and then sends it to the server surrounded by protocol information.
	 */
	// @ pure; (not entirely sure as it does readMove())
	// @ ensures (\forall Move m; sb.indexOf(moveCollection[j].toString())
	// !=null); (this is probably not the correct syntax for this specification)
	private void makeMove() {
		System.out.println(player.getBoard().toString());
		System.out
			   .println("It is your turn! Type the tile with the coordinates x and y. "
					 + "\nTo enter swapping mode type swap."
					 + "\nTo end the turn type end.");
		System.out.println("Your hand contains these tiles.\n"
					 + player.getHand().toString());
		String readUserInput = readClientPlayerMove();
		String[] readUserInputCut = readUserInput.split(" ");
		StringBuilder sb = new StringBuilder();
		if (readUserInputCut[0].matches("SWAP")) {
			for (String s : readUserInputCut) {
				sb.append(" " + s.toString());
			}
		} else {
			sb.append("MOVE");
			if (readUserInputCut.length == 0) {
				sb.append(" empty");
			} else {
				for (String s : readUserInputCut) {
					sb.append(" " + s.toString());
				}
			}
			sb.append(" /n");
			writeMe(sb.toString());
			player.confirmTurn();
		}
	}
	
	/**
	 * reads the input of the Client's player when it is his turn to make one.
	 * if the message follows a certain regex, readClientPlayerMove will create a
	 * move and adds it to the currentMoves list to later send it to the server and 
	 * end the turn if the player types 'end'.
	 * @return
	 */
	// @ ensures \result == player.getCurrentMoves && \result != null;
	public String readClientPlayerMove() {
		Scanner scan = new Scanner(System.in);
		boolean turnEnded = false;
		boolean swapping = false;
		StringBuilder sb = new StringBuilder();
		while (!turnEnded) {
			if (scan.hasNext()) {
				String lline = scan.nextLine();
				String[] words = lline.split(" ");
				if (words[0].matches("swap") && !swapping && 
						  !player.getCurrentMoves().isEmpty()) {
					System.out
							  .println("You cannot make moves and swap at the same turn. "
									+ "\nYou'll have to undo all your moves before swapping");
				} else if (words[0].matches("swap")
						  && !swapping
						  && player.getBoard().getUsedSpaces().size() <= (108 - (gameSize * 6))) {
					swapping = true;
					System.out.println("You have now entered swapping mode.");
					System.out
							  .println("To swap a tiles type the tiles and type 'end' to confirm"
									+ " the swap. \nIf you do not want to swap tiles type 'swap' "
									+ "to exit swapping mode.");
				} else if (swapping && words.length >= 2
								&& words[words.length - 1].matches("end")
					    && Tile.buildTile(words[0]).tileInHand(player.getHand())) {
					boolean correctInput = true;
					for (String checkMe : words) {
						if (!checkMe.matches("^[ROBYGP][odscx\\*]|end")) {
							System.out.println("The input is not correct for swap is incorrect.");
							correctInput = false;
						}
					}
					if (correctInput) {
						Tile t = Tile.buildTile(words[0]);
						player.getCurrentSwap().add(t);
						player.removeFromHand(t);
						System.out.println(player.getBoard().toString());
						System.out.println(player.getHand().toString());
						System.out
								.println("End turn by typing 'end' or swap another tile.");
					}
				
				} else if (words[0].equals("swap") && swapping) {
					swapping = false;
					System.out.println("You have exited swapping mode, proceed to make a move.");
				} else if (words[0].equals("end")) {
					System.out.println("You have ended your move.");
					turnEnded = true;
				} else if (words.length == 3
					    && words[0].matches("^[ROBYGP][odscx\\*]")
					    && words[1].matches("\\d{1,3}")
					    && words[2].matches("\\d{1,3}")
					    && Tile.buildTile(words[0]).tileInHand(player.getHand())) {
					Tile t = Tile.buildTile(words[0]);
					int x = Integer.parseInt(words[1]);
					int y = Integer.parseInt(words[2]);
					Move attempt = new Move(t, new Coord(x, y));
					if (player.getBoard().validMove(attempt,
					    player.getCurrentMoves())) {
						player.makeMove(attempt);
						player.removeFromHand(attempt.getTile());
						System.out.println(player.getBoard().toString());
						System.out.println(player.getHand().toString());
						System.out.println("Your current score is: " + 
						    player.getBoard()
						        .totalTurnScore(new HashSet<Move>(player.getCurrentMoves())));
						System.out
							.println("End turn by typing 'end' or make another move.");
					} else {
						System.out.println("The given move is invalid");
					}
					
				} else if (words[0].equals("undo")) {
					if (player.getCurrentMoves().size() > 0) {
						System.out.println("You undid your previous move.");
						player.undoMove();
						System.out.println(player.getBoard().toString());
						System.out.println(player.getHand().toString());
						System.out
								.println("End turn by typing 'end' or make another move.");
					} else {
						System.out
							    .println("You have not made any moves yet to undo. "
								    	+ "Please try something else.");
					}
				} else {
					System.out.println("The input was not correct, try again.");
				}
			}
		}
		if (!swapping) {
			for (Move move : player.getCurrentMoves()) {
				sb.append(move.toString() + " ");
			}
		} else {
			sb.append("SWAP ");
			for (Tile tile : player.getCurrentSwap()) {
				sb.append(tile.toString() + " ");
			}
		}
		return sb.toString();
	}

	/**
	 * sends a HELLO followed by the players name to the server in order to connect with it.
	 */
	// @ pure;
	public void sendHello() {
		try {
			writeMe("HELLO " + player.getName());
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Client().run();
	}

	/**
	 * sends the String s to the server.
	 * @param s
	 */
	// @ pure;
	public void writeMe(String s) {
		try {
			out.write(s);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
