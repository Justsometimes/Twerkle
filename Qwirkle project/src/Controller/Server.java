package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Game;
import model.Tile;
import player.Player;

public class Server {

	private static final int GAMESIZE = 4;
	private Game game;
	private boolean running;
	private boolean setup;
	private static int portNumber = 4444;
	private ArrayList<PlayerHandler> players;
	private int skipCount;

	public  Server() {
		running = true;
		game = new Game();
		players = new ArrayList<PlayerHandler>();
		skipCount =0;
		setup = false;
	}

	// TODO public void broadcast send message to all players
	// TODO public void kick (moet dit in game of in de server?)

	public void run() {
		ServerSocket serverSocket = null;
		try {
			int p = 0;
			serverSocket = new ServerSocket(portNumber);
			portNumber = serverSocket.getLocalPort();
			// if connection is closed, use serverSocket.close();

			while (running) {
				while(!setup){
					Socket clientSoc;
					if(players.size() < GAMESIZE){
						clientSoc = serverSocket.accept();
						System.out.println("ready");
						PlayerHandler playerHandler = new PlayerHandler(clientSoc, this);
						players.add(playerHandler);
						new Thread(playerHandler).start();
					}
					boolean allSet = true;
					for(PlayerHandler pH : players){
						System.out.println("player " + pH.getplayer() + " is selected");
						if(pH.getplayer() == null){
							allSet = false;
							System.out.println("allSet= " + allSet);
						}
					}
					if(allSet){
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
	public void kickplayerFromLobby(PlayerHandler p){
		kickPlayerFromLobby(p, "Fock u");
	}
	
	public void kickPlayerFromLobby(PlayerHandler p, String reason){
		System.out.println("Kicking player " + p + " for " + reason);
		if(players.contains(p.getplayer())){
			int tilesBackToBag = p.getplayer().getHand().size();
			broadCast("KICKED " + game.getPlayerNr(p.getplayer()) + " " + tilesBackToBag + " " + reason);
			game.kickFromGame(p.getplayer());
			players.remove(p);	
			p.quit();
		}
	}
	
	public void broadCast(String s){
		System.out.println("Broadcasting: " + s);
		for(PlayerHandler p: players){
			p.writeMe(s);
		}
	}
	
	public Game getGame(){
		return game;
	}
	
	public void sendNext(){
		System.out.println("NEXT send");
		game.nextTurn();
		broadCast("NEXT " + game.getTurn());
	}
	
	public void sendTurn(Player p, Set<Tile> set){
		System.out.println("Sending TURN");
		StringBuilder sb = new StringBuilder();
		for(Tile tile : set){
			sb.append(" " + tile.toString());
		}
		broadCast("TURN " + game.getPlayerNr(p) + " " +sb.toString());
	}
	
	public void startGame(){
		TileBag tilebag = game.getTileBag();
		for(PlayerHandler p : players){
			Set<Tile> tiddies = tilebag.drawSix();
			p.getplayer().setHand(tiddies);
			p.sendTiles(tiddies);
		}
		setup = true;
		sendNext();
	}
	
	public void skipped(){
		System.out.println("SKIP received");
		if(skipCount >= game.getPlayerAmount()){
			int score =0;
			ArrayList<Player> peoples = new ArrayList<Player>();
			
			for(Map.Entry<Player, Integer> p : game.getScores().entrySet()){
				if(p.getValue() > score){
			   peoples = new ArrayList<Player>();
			   peoples.add(p.getKey());
			   score = p.getValue();
				} else if(p.getValue() == score){
					peoples.add(p.getKey());
				}
			}
			if(peoples.size() != 1){
				broadCast("WINNER " + -1);
			} else {
			broadCast("WINNER " + game.getPlayerNr(peoples.get(0)));
			}
		}else{
		skipCount++;
		}
	}
	
	public void skipReset(){
		skipCount = 0;
	}
	
	public static void main(String[] args) {
		new Server().run();
	}
	
	
}
