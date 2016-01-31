package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import player.Player;
import Qwirkle.Board;
import Qwirkle.Coord;
import Qwirkle.Game;
import Qwirkle.Move;
import Qwirkle.Tile;

public class PlayerHandler implements Runnable {
	private Socket soc;
	private Game game;
	private Player player;
	private Server server;
	private boolean running;

	BufferedReader in;
	BufferedWriter out;

	public PlayerHandler(Socket soc, Server server) throws IOException {
		this.soc = soc;
		this.game =server.getGame();
		this.server = server;
		in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
		running = true;
	}
	
	public void writeMe(String s){
		try {
			out.write(s);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		while (running) {

			try {
				String lline;
				while ((lline = in.readLine()) != null) {
					System.out.println(lline);
					String[] elements = lline.split(" ");
					switch (elements[0]) {
					case "HELLO":
						System.out.println("HELLO read");
						if (elements.length > 1) {
							if (elements[1].length() <= 16
									&& elements[1].matches("^[A-Za-z]+$")) {
								player = new Player(elements[1], new HashSet<Tile>());
								game.addPlayer(player);
								sendWelcome();
							} else {
								// send error message
							}
						} else {
							// send error message
						}
						break;
					case "MOVE":
						System.out.println("MOVE read");
						readMove(elements);
						server.sendNext();
						break;
					case "SWAP":
						System.out.println("SWAP read");
						readSwap(elements);
						server.sendNext();
						break;
					default:
						break;
					}
					// implement commands to be send?;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void sendWelcome() {
		System.out.println("WELCOME "+player.getName()+" " + game.getPlayerNr(player));
		writeMe("WELCOME "+player.getName()+" " + game.getPlayerNr(player));
	}
	
	public void sendNames(){
		StringBuilder sb = new StringBuilder();
		sb.append("NAMES ");
		for(Player p : game.getScores().keySet()){
		sb.append(p.getName() + " " + game.getPlayerNr(p));
		}
		writeMe(sb.toString());
	}
	
	public void readSwap(String[] elements){
		if(game.getTurn() == game.getPlayerNr(player)){						
			if (elements.length > 1){
				Set<Tile> swapped = new HashSet<Tile>();
					for(int i =0; i<((elements.length-1)); i++){
						if(elements[1+(i)].matches("^[ROBYGP][odscx*]$")){
							if(Tile.buildTile(elements[1+(i)]).tileInHand(player.getHand())){ //TODO maybe bug
								player.getHand().remove(Tile.buildTile(elements[1+(i)]));
								if(game.getTileBag().remainingTiles() > 0){
									Tile tit = game.getTileBag().swapThis(Tile.buildTile(elements[1+(i)]));
								player.addToHand(tit);
								swapped.add(tit);
								}
							}else {
								//send error message player does not have that tile
								break;
							}
						} else {
							//send error message not a tile
							break;
						}
					} 
					sendTiles(swapped);
				
			} else {
				//send error message no SWAP arguments
			}
		} else {
			//send error message wrong player not his turn
		}
		
	}
	
	//TODO ontnest en gooi excepties met de kick reason en split in methodes met duidelijke namen
	
	public void readMove(String[] elements){
		if(game.getTurn() == game.getPlayerNr(player)){						
			if (elements.length > 1){
				if(elements[1] == "empty"){
					server.skipped();
				} else {
				if((elements.length-1)%3 == 0){
					Set<Tile> moved = new HashSet<Tile>();
					for(int i =0; i<(elements.length-1)/3; i++){
						if(elements[1+(i*3)].matches("^[ROBYGP][odscx*]$") && elements[1+(i*3)].matches("[0-9]{1-3}") && elements[1+(i*3)].matches("[0-9]{1-3}")){
							if(Tile.buildTile(elements[1+(i*3)]).tileInHand(player.getHand())){ //TODO possible bug
								if(game.getBoard().validMove(new Move(Tile.buildTile(elements[1+(i*3)]), new Coord(Integer.parseInt(elements[2+(i*3)]), Integer.parseInt(elements[3+(i*3)]))))){
									game.getBoard().boardAddMove(new Move(Tile.buildTile(elements[1+(i*3)]), new Coord(Integer.parseInt(elements[2+(i*3)]), Integer.parseInt(elements[3+(i*3)]))));
									player.getHand().remove(Tile.buildTile(elements[1+(i*3)]));
									if(game.getTileBag().remainingTiles() > 0){
									Tile tit = game.getTileBag().swapThis(Tile.buildTile(elements[1+(i)]));
									player.addToHand(tit);
									player.addToHand(game.getTileBag().drawTile());
									moved.add(tit);
									}
								}else {
									break;
									//send error message the move itself is illegal
								}
							} else {
								//send error message player does not have that tile
							}
						} else {
							break;
							//send error message the move arguments are incorrect
						}
					}
					server.sendTurn(player, moved);
					sendTiles(moved);
				} else {
					//send error message the arguments are not complete
				}
				}
			} else {
				//send error message no MOVE arguments
			}
		} else {
			//send error message wrong player not his turn
		}
		
	}
	public void sendTiles(Set<Tile> tiles){
		String news = "NEW";
		for(Tile t : tiles){
			news += " " + t.toString();
		}
		writeMe(news);
	}
	
	public Player getplayer(){
		return player;
	}
	
	public void quit(){
		running = false;
		try {
			in.close();
			out.close();
			soc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
