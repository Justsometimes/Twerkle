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

	public RetardedStrategy(Board b, Set<Tile> h) {
		board = b;
		hand = h;
	}

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
