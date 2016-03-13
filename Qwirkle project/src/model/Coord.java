package model;

public class Coord {

	private int horizontal;
	private int vertical;
	private static final int DIM = 183;

	/**
	 * constructor for Coord. The name coord is abbreviation of coordinates,
	 * it has a horizontal axis 'x' and a vertical axis value 'y'.
	 * @param x
	 * @param y
	 */
	public Coord(int x, int y) {
		horizontal = x;
		vertical = y;
	}

	/**
	 * getter for horizontal axis value of the coord.
	 * @return horizontal
	 */
	public int getX() {
		return horizontal;
	}

	/**
	 * getter for the vertical axis value of the coord.
	 * @return vertical
	 */
	public int getY() {
		return vertical;
	}

	/**
	 * creates an array of 4 coords adjacent to the given coord.
	 * @return Coord[]
	 */
	public Coord[] getAdjacentCoords() {
		Coord[] coords = new Coord[4];
		if (horizontal != DIM) {
			coords[0] = new Coord(horizontal + 1, vertical);
		}
		if (horizontal != 0) {
			coords[1] = new Coord(horizontal - 1, vertical);
		}
		if (vertical != DIM) {
			coords[2] = new Coord(horizontal, vertical + 1);
		}
		if (vertical != 0) {
			coords[3] = new Coord(horizontal, vertical - 1);
		}
		return coords;

	}

	/** toString method for Coord, should be self explanatory
	 * 
	 */
	public String toString() {
		return horizontal + " " + vertical;
	}
}
