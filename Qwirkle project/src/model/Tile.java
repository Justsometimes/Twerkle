package model;

import java.util.Set;

public class Tile {

	/**
	 * the enum Color determines the color of the Tile.
	 */
	public static enum Color {
		RED('R'), ORANGE('O'), BLUE('B'), YELLOW('Y'), GREEN('G'), PURPLE('P');
		public final char c;

		Color(char ch) {
			this.c = ch;
		}
	}

	// the second char (u) used to be a UTF-8 character, but it turned out that
	// the UTF-8 caused several errors.
	/**
	 *the enum Shape determines the shape of the Tile. 
	 */
	public static enum Shape {
		CIRCLE('o', 'o'), DIAMOND('d', 'd'), SQUARE('s', 's'), CLOVER('c', 'c'), CROSS(
				'x', 'x'), STAR('*', '*');
		public final char c;
		public final char u;

		Shape(char c, char u) {
			this.c = c;
			this.u = u;
		}
	}

	private final Shape shape;
	private final Color color;

	/**
	 * constructor for Tile, which has a Color color and a Shape shape.
	 * @param color
	 * @param shape
	 */
	public Tile(Color color, Shape shape) {
		this.shape = shape;
		this.color = color;
	}

	/**
	 * builds a Tile from a String, checking if the String resembles one of the possible tiles.
	 * @param s
	 * @return the Tile which the String resembles (if it resembles one)
	 */
	public static Tile buildTile(String s) {
		if (s.matches("^[ROBYGP][odscx*]$")) {
			Color c = null;
			switch (s.toCharArray()[0]) {
				case 'R':
					c = Color.RED;
					break;
				case 'O':
					c = Color.ORANGE;
					break;
				case 'B':
					c = Color.BLUE;
					break;
				case 'Y':
					c = Color.YELLOW;
					break;
				case 'G':
					c = Color.GREEN;
					break;
				case 'P':
					c = Color.PURPLE;
					break;
			}
			Shape sh = null;
			switch (s.toCharArray()[1]) {
				case 'o':
					sh = Shape.CIRCLE;
					break;
				case 'd':
					sh = Shape.DIAMOND;
					break;
				case 's':
					sh = Shape.SQUARE;
					break;
				case 'c':
					sh = Shape.CLOVER;
					break;
				case 'x':
					sh = Shape.CROSS;
					break;
				case '*':
					sh = Shape.STAR;
					break;
			}

			return new Tile(c, sh);
		} else {
			return null;
		}
	}

	/**
	 * getter for the Color of the Tile.
	 * @return color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * getter for the Shape of the Tile.
	 * @return shape
	 */
	public Shape getShape() {
		return shape;
	}

	/**
	 * toString method for Tile, should be self explanatory
	 */
	public String toString() {
		return (String.valueOf(color.c) + String.valueOf(shape.c));
	}

	public boolean tileInHand(Set<Tile> hand) {
		boolean result = false;
		for (Tile inHand : hand) {
			if (inHand.toString().equals(this.toString())) {
				result = true;
				break;
			}
		}
		return result;
	}

}
