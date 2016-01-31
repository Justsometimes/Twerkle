package view;

import player.Player;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import model.Coord;
import model.Move;
import model.Tile;

public class Client implements Runnable {

	private int port;
	private Socket sock;
	private String hostname;
	private BufferedReader in;
	private BufferedWriter out;
	private boolean running;
	private boolean joined;
	private Player player;
	private int playernum;
	private int AItime;

	public Client() {
		this("localhost", 4444, new Player("Bert", new HashSet<Tile>()));
	}

	public Client(String address, int port, Player player) {
		this.player = player;
		this.hostname = address;
		this.port = port;
		running = true;
		joined = false;

		try {
			sock = new Socket(address, port);
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(
					sock.getOutputStream()));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	//@ requires elements != null && elements > 1;
	//@ loop_invariant 1 <= i && i < elements.getLength;
	//@ loop_invariant (\forall int j; j<=1 && j<i; Tile.buildTile(elements[j]).tileInHand(player.getHand);
	//@ ensures player.getHand() == \old(player.getHand())+ elements
	private void readNew(String[] elements) {
		if (elements != null && elements.length > 0){
			for(int i=1; i<elements.length; i++){
				player.addToHand(Tile.buildTile(elements[i]));
			}
		System.out.println("Tiles are added to the players hand");
		}
	}
	//@ pure
	//@ requires elements != null && elements.length == 2;
	private void readWinner(String[] elements) {
		if (elements.length == 2) {
			if (Integer.parseInt(elements[2]) == playernum) {
				System.out.println("You won!");
			} else {
				System.out.println("You lost! Winning player: " + elements[1]);
			}
		}
	}

	private void readKick(String[] elements) {
		if (Integer.parseInt(elements[1]) == playernum) {
			System.out.println("You have been kick from the server.\n  Reason: "+ elements[3]);
		} else {
			System.out.println("Player " + elements[1]+ " has been kicked from the server.\n Reason: "+ elements[3]);
		}
	}

	private void readTurn(String[] elements) {
		for (int i = 0; i < (elements.length - 1) / 3; i++) {
			Tile tile = Tile.buildTile(elements[i + 1]);
			Coord coord = new Coord(Integer.parseInt(elements[i + 2]),
					Integer.parseInt(elements[i + 3]));
			player.getBoard().boardAddMove(new Move(tile, coord));
		}
		System.out.println("Another player's turn has been received.");
	}

	private void readNext(String[] elements) {
		if (elements.length == 2) {
			if (Integer.parseInt(elements[1]) == playernum) {
				makeMove();
			}
		}
	}

	private void makeMove() {
		System.out.println(player.getBoard().toString());
		System.out.println("It is your turn! Type the tile with the coordinates x and y. To end the turn type end.");
		System.out.println("Your hand contains these tiles.\n"+ player.getHand().toString());
		ArrayList<Move> moveCollection = readMove();
		StringBuilder sb = new StringBuilder();
		sb.append("MOVE ");
		if(moveCollection.isEmpty()){
			sb.append("empty");
		} else {
		for (Move m : moveCollection) {
			sb.append(m);
		}
		}
		writeMe(sb.toString());
	}

	private void readNames(String[] elements) {
		if (elements.length % 2 == 0 && elements.length >= 4) {
			for (int i = 0; i < (elements.length - 2) / 2; i++) {
				System.out.println("Player: " + elements[i * 2 + 2] + " - "+ elements[i * 2 + 1] + " connected");
			}
			AItime = Integer.parseInt(elements[elements.length - 1]);
			System.out.println("AITime = " + AItime);
		}
	}

	private void readWelcome(String[] elements) {
		if (elements.length == 3) {
			if (elements[1].equals(player.getName())) {
				joined = true;
				playernum = Integer.parseInt(elements[2]);
				System.out.println("You are player " + playernum);
			}
		}
	}
	
	public ArrayList<Move> readMove(){
		Scanner scan = new Scanner(System.in);
		while(scan.hasNext()){
		String lline = scan.nextLine();
		String[] words = lline.split(" ");
		if(words[0].equals("end")){
		System.out.println("You have ended your move.");
		break;
		}else if(words[0].matches("^[ROBYGP][odscx*]$") && Tile.buildTile(words[0]).tileInHand(player.getHand())){
			Tile t = Tile.buildTile(words[0]);
			int x = Integer.parseInt(words[1]);
			int y = Integer.parseInt(words[2]);
			Move attempt = new Move(t, new Coord(x,y));
			if(player.getBoard().validMove(attempt)){
				player.makeMove(attempt);
				System.out.println(player.getBoard().toString());
				System.out.println("End turn by typing 'end' or make another move.");
			}else{
				System.out.println("The given move is invalid");
			}
		} else if(words[0].equals("undo")){
			System.out.println("You undid your previous move.");
			player.undoMove();
			System.out.println(player.getBoard().toString());
			System.out.println("End turn by typing 'end' or make another move.");
		} else {
		System.out.println("The input was not correct, try again.");
		}
		}
		System.out.println(player.getCurrentMoves().toString());
		scan.close();
		return player.getCurrentMoves();
	}

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
}
