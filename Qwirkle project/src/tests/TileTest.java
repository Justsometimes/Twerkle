package tests;

import java.util.HashSet;

import model.Tile;
import model.Tile.Color;
import model.Tile.Shape;

import org.junit.Before;

import static org.junit.Assert.*;
public class TileTest {

	Tile Bd;
	HashSet<Tile> hand;
	
	@Before
	public void setup(){
		Bd = new Tile(Color.BLUE, Shape.DIAMOND);
		
		hand = new HashSet<Tile>();
		hand.add(Bd);
	}
	
	@org.junit.Test
	public void testGetColor(){
		assertEquals(Bd.getColor(), Color.BLUE);
	}
	
	@org.junit.Test
	public void testGetShape(){
		assertEquals(Bd.getShape(),Shape.DIAMOND);
	}
	
	@org.junit.Test
	public void testBuildTile(){
		assertEquals(Tile.buildTile("Bd").getColor(), Bd.getColor());
		assertEquals(Tile.buildTile("Bd").getShape(), Bd.getShape());
	}
	
	@org.junit.Test
	public void testTileInHand(){
		Tile Rc = Tile.buildTile("Rc");
		assertTrue(Bd.tileInHand(hand));
		assertFalse(Rc.tileInHand(hand));
	}
	
}
