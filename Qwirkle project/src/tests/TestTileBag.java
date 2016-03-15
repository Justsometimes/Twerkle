package tests;
import model.Tile;
import model.TileBag;

import org.junit.Before;

import static org.junit.Assert.*;

public class TestTileBag {
	
	TileBag tileBag;
	
	@Before
	public void setup(){
		tileBag = new TileBag();
	}
	
	@org.junit.Test
	public void testDrawTile(){
		assertTrue(tileBag.drawTile() instanceof Tile);
		tileBag.drawTile();
		assertTrue(tileBag.SIZE > (tileBag.remainingTiles()));
	}
	
	@org.junit.Test
	public void testDrawSix(){
		assertTrue(tileBag.drawSix().size()== tileBag.SIZE_HAND);
		assertTrue(tileBag.drawSix().toArray()[(int)Math.random()*6] instanceof Tile);
	}
	
	@org.junit.Test
	public void testShuffle(){
		TileBag tily = new TileBag();
		assertFalse(tily.toString().equals(tileBag.toString()));
	}
	
	@org.junit.Test
	public void testRemainingTiles(){
		assertEquals(tileBag.remainingTiles(), 108);
		tileBag.drawTile();
		assertEquals(tileBag.remainingTiles(), 107);
	}

}
