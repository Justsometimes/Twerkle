package model;

import java.util.HashSet;
import java.util.Set;

public class RetardedStrategy implements Strategy {
	// /what
	// private Player player;
	// Player p = new ComputerPlayer("IDontKnowWhatImDoing", hand);
	// what
	// public RetardedStrategy(Player player) {
	// this.player = player;
	// }
	private Board board;
	private Set<Tile> hand;

	/**
	 * constructor for the strategy RetardedStrategy, 
	 * it requires a Board to simulate on and a hand h to play with.
	 * @param b
	 * @param h
	 */
	public RetardedStrategy(Board b, Set<Tile> h) {
		board = b;
		hand = h;
	}

	/**
	 * as the name of the method implies, it determines where to place a Tile
	 * by trying to place a one from its hand on every adjacent space of the usedSpaces
	 * taken from the board using tryAdjacents.
	 */
	@Override
	public Set<Move> determineMoves() {
		Set<Move> result = new HashSet<Move>();
		for (Tile hTile : hand) {
			for (Move boardTile : board.getUsedSpaces()) {
				if (boardTile.getTile().getColor() == hTile.getColor()
						|| boardTile.getTile().getShape() == hTile.getShape()) {
					Coord[] attempts = boardTile.getCoord().getAdjacentCoords();
					result = tryAdjacents(hTile, attempts);
				}
			}
		}

		return result;
	}

	/**
	 * Checks if it is possible to place the Tile selectedTile on the Coords in attempts.
	 * If it is possible, 
	 * it will create a move with the selectedTile and the Coord in attempts that is valid.
	 * @param selectedTile
	 * @param attempts
	 * @return set<Move> containing Moves created by combining the selectedTile 
	 * and Coords in attempts that are valid together in relation to the board.
	 */
	public Set<Move> tryAdjacents(Tile selectedTile, Coord[] attempts) {
		Set<Move> result = new HashSet<Move>();
		for (Coord attempt : attempts) {
			Move movie = new Move(selectedTile, attempt);
			if (board.validMove(movie)) {
				result.add(movie);
			}
		}
		return result;
	}

}
