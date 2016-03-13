package model;

public class Move {

	private Tile tile;
	private Coord coord;

	/**
	 * constructor for Move, which requires the Tile and the Coord of the Move.
	 * @param tile
	 * @param coord
	 */
	public Move(Tile tile, Coord coord) {
		this.tile = tile;
		this.coord = coord;
	}

	/**
	 * getter for the Tile of the Move.
	 * @return tile
	 */
	public Tile getTile() {
		return tile;
	}

	/**
	 * setter for the Tile of the Move.
	 * @param tile
	 */
	public void setTile(Tile tile) {
		this.tile = tile;
	}

	/**
	 * getter for the Coord of the move.
	 * @return coord
	 */
	public Coord getCoord() {
		return coord;
	}

	/**
	 * setter for the Coord of the move coord.
	 * @param coord
	 */
	public void setCoord(Coord coord) {
		this.coord = coord;
	}

	/**
	 * toString method of Move, should be self explanatory.
	 */
	public String toString() {
		return tile.toString() + " " + coord.toString();
	}

}
