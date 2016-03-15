package model;

import java.util.ArrayList;
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
	private Player player;

	/**
	 * constructor for the strategy RetardedStrategy, 
	 * it requires a Board to simulate on and a hand h to play with.
	 * @param pl
	 */
	public RetardedStrategy(Player pl) {
		player = pl;
	}

	/**
	 * as the name of the method implies, it determines where to place a Tile
	 * by trying to place a one from its hand on every adjacent space of the usedSpaces
	 * taken from the board using tryAdjacents.
	 */
	@Override
	public ArrayList<Move> determineMoves() {
		ArrayList<Move> res = new ArrayList<>();
		Set<Move> used = player.getBoard().getUsedSpaces();
		if (used.size() == 0) {
			res.add(new Move((Tile) player.getHand().toArray()[0], new Coord(91, 91)));
		}
		for (Tile hTile : player.getHand()) {
			for (Move boardTile : used) {
				if (boardTile.getTile().getColor() == hTile.getColor()
						|| boardTile.getTile().getShape() == hTile.getShape()) {
					Coord[] attempts = boardTile.getCoord().getAdjacentCoords();
					Move movie = tryAdjacents(hTile, attempts);
					if(movie != null) {
						res.add(movie);
						player.makeMove(movie);
						player.removeFromHand(movie.getTile());
						return res;
					}
				}
			}
		}

		return res;
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
	public Move tryAdjacents(Tile selectedTile, Coord[] attempts) {
		for (Coord attempt : attempts) {
			Move movie = new Move(selectedTile, attempt);
			if (player.getBoard().validMove(movie)) {
				return movie;
			}
		}
		return null;
	}

}
