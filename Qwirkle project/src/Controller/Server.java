package Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import player.Player;
import Qwirkle.Game;
import Qwirkle.Tile;

public class Server {

	private static final int GAMESIZE = 4;
	private Game game;
	private boolean running;
	private static int portNumber;
	private ArrayList<PlayerHandler> players;
	private int skipCount;

	public  Server() {
		running = true;
		game = new Game();
		players = new ArrayList<PlayerHandler>();
		skipCount =0;
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
				Socket clientSoc;
				clientSoc = serverSocket.accept();
				PlayerHandler playerHandler = new PlayerHandler(clientSoc, this);
				players.add(playerHandler);
				new Thread(playerHandler).start();
				if(players.size() == GAMESIZE){
					startGame();
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
		if(players.contains(p.getplayer())){
			int tilesBackToBag = p.getplayer().getHand().size();
			broadCast("KICKED " + game.getPlayerNr(p.getplayer()) + " " + tilesBackToBag + " " + reason);
			game.kickFromGame(p.getplayer());
			players.remove(p);	
			p.quit();
		}
	}
	
	public void broadCast(String s){
		for(PlayerHandler p: players){
			p.writeMe(s);
		}
	}
	
	public Game getGame(){
		return game;
	}
	
	public void sendNext(){
		game.nextTurn();
		broadCast("NEXT " + game.getTurn());
	}
	
	public void startGame(){
		TileBag tilebag = game.getTileBag();
		for(PlayerHandler p : players){
			Set<Tile> tiddies = tilebag.drawSix();
			p.getplayer().setHand(tiddies);
			p.sendTiles(tiddies);
		}
		sendNext();
	}
	
	public void skipped(){
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
	
	
	
	
}
