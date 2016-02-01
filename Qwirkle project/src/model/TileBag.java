package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

//TODO gebruiken voor de server;
public class TileBag {

	// Aantal stenen
	public static final int SIZE = 108;

	// Aantal stenen in hand
	public static final int SIZE_HAND = 6;

	// Tiles dat in de bag zitten
	private ArrayList<Tile> tiles;

	// Creates a bag with 108 tiles in it :)
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

	// Draw the first tile from the bag and delete it
	public Tile drawTile() {
		shuffle();
		return tiles.remove(0);
	}

	public Tile swapThis(Tile t) {
		Tile result = drawTile();
		tiles.add(t);
		return result;
	}

	// Draw first round 6 tiles;
	public Set<Tile> drawSix() {
		Set<Tile> hand = new HashSet<>();
		for (int i = 0; i < SIZE_HAND; i++) {
			hand.add(this.drawTile());
		}
		return hand;
	}

	// shuffles the tiles
	public void shuffle() {
		Collections.shuffle(this.tiles);
	}

	// return amount of tiles
	public int remainingTiles() {
		return this.tiles.size();
	}

	public static void main(String[] args) {
		TileBag tilebag = new TileBag();
		System.out.println(tilebag.tiles.toString());
		System.out.println(tilebag.tiles.size());
		System.out.println(tilebag.drawTile().toString());
		System.out.println(tilebag.tiles.toString());
		tilebag.shuffle();
		System.out.println(tilebag.tiles.toString());
	}

}
