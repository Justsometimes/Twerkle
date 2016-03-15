package tests;

import model.Coord;
import model.Move;
import model.Tile;
import model.Tile.Color;
import model.Tile.Shape;

import org.junit.Before;

import static org.junit.Assert.*;

public class MoveTest {
	
	private Move move;

	@Before
	public void setup() {
		move = new Move(new Tile(Color.RED, Shape.CIRCLE), new Coord(91, 91));
	}
	
	@org.junit.Test
	public void testGetTile() {
		assertEquals(move.getTile().toString(), new Tile(Color.RED, Shape.CIRCLE).toString());
	}
	
	@org.junit.Test
	public void testGetCoord() {
		assertEquals(move.getCoord().toString(), new Coord(91, 91).toString());
	}
	
	@org.junit.Test
	public void testSetTile(){
		move.setTile(new Tile(Color.BLUE, Shape.CLOVER));
		assertEquals(move.getTile().toString(), new Tile(Color.BLUE, Shape.CLOVER).toString());
	}
	
	@org.junit.Test
	public void testSetCoord(){
		move.setCoord(new Coord(90, 90));
		assertEquals(move.getCoord().toString(), new Coord(90, 90).toString());
	}
}
