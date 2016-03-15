package tests;

import java.util.Arrays;

import model.Coord;

import org.junit.Before;

import static org.junit.Assert.*;

public class CoordTest {
	private Coord coord;

	@Before
	public void setup() {
		coord = new Coord(90, 91);
	}

	@org.junit.Test
	public void testGetX() {
		assertEquals(coord.getX(), 90);
	}

	@org.junit.Test
	public void testGetY() {
		assertEquals(coord.getY(), 91);
	}

	@org.junit.Test
	public void testGetAdjacentCoords() {
		Coord[] coords = new Coord[4];
		coords[0] = new Coord(91, 91);
		coords[1] = new Coord(89, 91);
		coords[2] = new Coord(90, 92);
		coords[3] = new Coord(90, 90);
		System.out.println(Arrays.toString(coords));
		System.out.println(Arrays.toString(coord.getAdjacentCoords()));
		assertEquals(Arrays.toString(coord.getAdjacentCoords()),
				  Arrays.toString(coords));
	}
}
