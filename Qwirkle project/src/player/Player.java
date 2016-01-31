package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import Qwirkle.Board;
import Qwirkle.Coord;
import Qwirkle.Move;
import Qwirkle.Tile;

public class Player {
	
 // -- Instance variables -----------------------------------------

	 private String name;
	 
	 private Set<Tile> hand;
	 
	 private Board deepCopy;
	 
	 private Board board;
	 
	 private List<Move> currentMoves;
	 
	 //-----Constructor------
	 
	 public Player(String name, Set<Tile> hand){
		 this.name = name;
		 this.hand = hand;
		 currentMoves = new ArrayList<Move>();
		 board = new Board();
	 }
	 //------Queries-------
	 public String getName(){
		 return name;
	 }
	
	 public Set<Tile> getHand(){
		 return hand;
	 }
	 
	 //----Setters-----
	 
	 public void setHand(Set<Tile> newHand){
		 hand.addAll(newHand);
	 }
	
	 //----Methods-----
	 
	public void addToHand(Tile tile){
		hand.add(tile);
	}
	 
	public void makeMove(Tile tile, Coord coord){
		Move movie = new Move(tile, coord);
		if(currentMoves.size() == 0){
			deepCopy = board;
		}
		if(hand.contains(tile) && board.validMove(movie, currentMoves)){
			board.boardAddMove(movie);
			currentMoves.add(movie);
			hand.remove(movie.getTile());
		}
	}
	
	public void makeMove(Move move){
		makeMove(move.getTile(), move.getCoord());
	}
	
	public List<Move> readMove(){
		Scanner scan = new Scanner(System.in);
		while(scan.hasNext()){
		String lline = scan.nextLine();
		String[] words = lline.split(" ");
		if(words[0].equals("end")){
		System.out.println("You have ended your move.");
		break;
		}else if(words[0].matches("^[ROBYGP][odscx*]$") && Tile.buildTile(words[0]).tileInHand(hand)){
			Tile t = Tile.buildTile(words[0]);
			int x = Integer.parseInt(words[1]);
			int y = Integer.parseInt(words[2]);
			Move attempt = new Move(t, new Coord(x,y));
			if(board.validMove(attempt)){
				makeMove(attempt);
				System.out.println(board.toString());
				System.out.println("End turn by typing 'end' or make another move.");
			}else{
				System.out.println("The given move is invalid");
			}
		} else if(words[0].equals("undo")){
			System.out.println("You undid your previous move.");
			undoMove();
			System.out.println(board.toString());
			System.out.println("End turn by typing 'end' or make another move.");
		} else {
		System.out.println("The input was not correct, try again.");
		}
		}
		System.out.println(currentMoves.toString());
		return currentMoves;
	}
	
	public void undoMove(){
		Move lastMove = currentMoves.get(currentMoves.size()-1);
		board.boardRemove(lastMove.getCoord());
		hand.add(lastMove.getTile());
	    currentMoves.remove(lastMove);
	}
	
	public void confirmTurn(){
//      send board to server
		currentMoves.removeAll(currentMoves);
	}
	
	public Board getBoard(){
		return board;
	}
	

	
}


