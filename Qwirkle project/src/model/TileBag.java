package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TileBag {

	/**
	 * amount of Tiles in the bag.
	 */
	public static final int SIZE = 108;

	/**
	 * amount of Tiles a hand can contain.
	 */
	public static final int SIZE_HAND = 6;

	/**
	 * The Tiles in the TileBag. In other words, it resembles a tilebag.
	 */
	private ArrayList<Tile> tiles;

	/**
	 * Constructor for TileBag, it adds three Tiles of every Color and Shape combination.
	 * When done adding Tiles, it shuffles the array.
	 */
	public TileBag() {
		this.tiles = new ArrayList<>();

		for (Tile.Color c : Tile.Color.values()) {
			for (Tile.Shape s : Tile.Shape.values()) {
				this.tiles.add(new Tile(c, s));
				this.tiles.add(new Tile(c, s));
				this.tiles.add(new Tile(c, s));
			}
		}
		shuffle();
	}

	/**
	 * After shuffling the tiles array it returns the first Tile in the tiles array 
	 * and removes from tiles.
	 * @return the drawn Tile
	 */
	public Tile drawTile() {
		shuffle();
		return tiles.remove(0);
	}

	/**
	 * uses drawTile to draw a Tile from tiles, puts the given Tile t back into tiles 
	 * and returns the drawn Tile.
	 * @param t
	 * @return the drawn Tile
	 */
	public Tile swapThis(Tile t) {
		Tile result = drawTile();
		tiles.add(t);
		return result;
	}

	/**
	 * even though it is called drawSix, it does not necessarily draw six Tiles form the TileBag,
	 * it draws an amount of Tiles equal to the SIZE_HAND 
	 * (the set amount of Tiles a hand can contain).
	 * used at the start of the game every player needs a hand containing six Tiles.
	 * @return
	 */
	public Set<Tile> drawSix() {
		Set<Tile> hand = new HashSet<>();
		for (int i = 0; i < SIZE_HAND; i++) {
			hand.add(this.drawTile());
		}
		return hand;
	}

	/**
	 * shuffles the tiles list.
	 */
	public void shuffle() {
		Collections.shuffle(this.tiles);
	}

	/**
	 * retrieves the amount of Tiles left in tiles.
	 * @return this.tiles.size() 
	 */
	public int remainingTiles() {
		return this.tiles.size();
	}

}
