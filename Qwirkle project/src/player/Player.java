package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import model.Board;
import model.Coord;
import model.Move;
import model.Tile;

public class Player {
	
 // -- Instance variables -----------------------------------------

	 private String name;
	 
	 private Set<Tile> hand;
	 // A deepCopy is made in case we need to reset the board from the player's moves, it is not needed yet.
	 private Board deepCopy;
	 
	 private Board board;
	 
	 private ArrayList<Move> currentMoves;
	 
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
	
	public void undoMove(){
		Move lastMove = currentMoves.get(currentMoves.size()-1);
		board.boardRemove(lastMove.getCoord());
		hand.add(lastMove.getTile());
	    currentMoves.remove(lastMove);
	}
	
	public ArrayList<Move> getCurrentMoves(){
		return currentMoves;
	}
	
	public void confirmTurn(){
//      send board to server
		currentMoves.removeAll(currentMoves);
	}
	
	public Board getBoard(){
		return board;
	}
	

	
}


