package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Player {

	private String name;

	private Set<Tile> hand;
	// A deepCopy is made in case we need to reset the board from the player's
	// moves, it is not needed yet.
	private Board deepCopy;

	private Board board;

	private ArrayList<Move> currentMoves;

	/**
	 * constructor for Player, which requires a name and a hand.
	 * This player will play under the given name and use the given hand.
	 * The hand resembles the player's hand with a collection of Tiles in it.
	 * @param name
	 * @param hand
	 */
	public Player(String name, Set<Tile> hand) {
		this.name = name;
		this.hand = hand;
		currentMoves = new ArrayList<Move>();
		board = new Board();
	}

	/**
	 * getter for the name of the player.
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * getter for the hand of the player.
	 * @return hand
	 */
	public Set<Tile> getHand() {
		return hand;
	}
	
	/**
	 * removes the Tile t from the players hand.
	 * @param t
	 */
	public void removeFromHand(Tile t){
		for (Tile inHand : hand) {
			if (inHand.toString().equals(t.toString())) {
				hand.remove(inHand);
				break;
			}
	}
	}

	/**
	 * even though it is called setHand, it is not a setter for the hand. 
	 * It adds the tiles newHand to the hand.
	 * @param newHand
	 */
	public void setHand(Set<Tile> newHand) {
		hand.addAll(newHand);
	}

	/**
	 * adds the one Tile tile to the hand.
	 * @param tile
	 */
	public void addToHand(Tile tile) {
		hand.add(tile);
	}

	/**
	 * creates a Move(tile, coord) by the player on the player's board (if it is valid).
	 * It also removes the used Tile from the player's hand 
	 * and adds the move to the currentMoves list.
	 * @param tile
	 * @param coord
	 */
	public void makeMove(Tile tile, Coord coord) {
		Move movie = new Move(tile, coord);
		if (currentMoves.size() == 0) {
			deepCopy = board;
		}
		System.out.println(hand + " is the hand of the player");
		System.out.println(tile + " is the tile we want to place");
		String handString = hand.toString();
		String tileString = tile.toString();
		if (handString.contains(tileString) && board.validMove(movie, currentMoves)) {
			board.boardAddMove(movie);
			currentMoves.add(movie);
			hand.remove(movie.getTile());
		}
	}

	/**
	 * does the same as the previous makeMove,
	 * only difference being that the move has already been fully created
	 * instead of being given decomposed.
	 * @param move
	 */
	public void makeMove(Move move) {
		makeMove(move.getTile(), move.getCoord());
	}

	/**
	 * undoes the previous Move made by the player.
	 * The last Move is the last index of the currentMoves list,
	 * so by removing the Move from the board,
	 * removing the last index of the currentMoves list and
	 * returning the Tile of the Move back to the player's hand; the move is undone.
	 */
	public void undoMove() {
		Move lastMove = currentMoves.get(currentMoves.size() - 1);
		board.boardRemove(lastMove.getCoord());
		hand.add(lastMove.getTile());
		currentMoves.remove(lastMove);
	}

	/**
	 * getter for the currentMoves list.
	 * @return currentMoves
	 */
	public ArrayList<Move> getCurrentMoves() {
		return currentMoves;
	}

	/**
	 * empties the currentMoves list.
	 */
	public void confirmTurn() {
		currentMoves.removeAll(currentMoves);
	}

	/**
	 * getter for the player's board.
	 * @return board
	 */
	public Board getBoard() {
		return board;
	}

}
