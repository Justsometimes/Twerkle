package tests;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import model.Board;
import model.Coord;
import model.Move;
import model.Tile;
import model.Tile.Color;
import model.Tile.Shape;

import org.junit.Before;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoardTest {

	private Board board;
	private Tile tileRs;
	private Tile tileRs2;
	private Tile tileBc;
	private Tile tileBs;
	private Tile tileRc;
	private Tile tilePd;
	private Coord mid;
	
	@Before
	public void setup() {
		board = new Board();
		tileRs = new Tile(Color.RED, Shape.SQUARE);
		tileRs2 = new Tile(Color.RED, Shape.SQUARE);
		tileBc = new Tile(Color.BLUE, Shape.CIRCLE);
		tileBs = new Tile(Color.BLUE, Shape.SQUARE);
		tileRc = new Tile(Color.RED, Shape.CIRCLE);
		tilePd  = new Tile(Color.PURPLE, Shape.DIAMOND);
		mid = new Coord(91, 91);
	}
	
	@org.junit.Test
	public void testboardAddMove() {
		board.boardAddMove(new Move(tilePd, mid));
		assertEquals(board.getBoardSpaces()[mid.getX()][mid.getY()], tilePd);
	}
	
	@org.junit.Test
	public void testboardRemove() {
		board.boardAddMove(new Move(tilePd, mid));
		board.boardRemove(mid);
		assertEquals(board.getBoardSpaces()[mid.getX()][mid.getY()], null);
	}
	
	@org.junit.Test
	public void testGetUsedSpaces() {
		board.boardAddMove(new Move(tileRs, mid));
		board.boardAddMove(new Move(tileRc, new Coord(91, 90)));
		Set correctAnswer = new HashSet<Move>();
		correctAnswer.add(new Move(tileRc, new Coord(91, 90)));
		correctAnswer.add(new Move(tileRs, mid));
		assertEquals(board.getUsedSpaces(), correctAnswer);
	}
	
	@org.junit.Test
	public void testValidMove(){
		assertFalse(board.validMove(new Move(tileRs, new Coord(91, 90))));
		assertTrue(board.validMove(new Move(tileRs, mid)));
		assertFalse(board.validMove(new Move(tileRc, mid)));
		assertTrue(board.validMove(new Move(tileRc, new Coord(91, 90))));
		assertFalse(board.validMove(new Move(tileRs, new Coord(91, 89))));
		assertTrue(board.validMove(new Move(tileBs, new Coord(90, 91))));
		assertFalse(board.validMove(new Move(tileBc, new Coord(89, 91))));
		ArrayList<Move> movesOfThisTurn = new ArrayList<Move>();
		movesOfThisTurn.add(e)
	}
	
}
