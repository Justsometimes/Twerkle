package tests;

import java.util.ArrayList;
import java.util.HashSet;

import model.Board;
import model.Coord;
import model.Move;
import model.Tile;
import model.Tile.Color;
import model.Tile.Shape;

import org.junit.Before;

import static org.junit.Assert.*;


public class BoardTest {

	private Board board;
	private Tile tileRs;
	private Tile tileRs2;
	private Tile tileRd;
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
		tileRd = new Tile(Color.RED, Shape.DIAMOND);
		tileRc = new Tile(Color.RED, Shape.CIRCLE);
		tileBc = new Tile(Color.BLUE, Shape.CIRCLE);
		tileBs = new Tile(Color.BLUE, Shape.SQUARE);
		tilePd = new Tile(Color.PURPLE, Shape.DIAMOND);
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
		board = new Board();
		HashSet<Move> correctAnswer = new HashSet<Move>();
		correctAnswer.add(new Move(tileRs, mid));
		correctAnswer.add(new Move(tileRc, new Coord(91, 90)));
		board.boardAddMove(correctAnswer);
		System.out.println(correctAnswer.toString());
		System.out.println(board.getUsedSpaces().toString());
		assertEquals(board.getUsedSpaces().toString(), correctAnswer.toString());
		//This assert does not work without the toString() methods for some reason.
	}

	@org.junit.Test
	public void testValidMove() {
		board = new Board();
		assertFalse(board.validMove(new Move(tileRs, new Coord(91, 90))));
		assertTrue(board.validMove(new Move(tileRs, mid)));
		board.boardAddMove(new Move(tileRs, mid));

		assertFalse(board.validMove(new Move(tileRc, mid)));
		assertTrue(board.validMove(new Move(tileRc, new Coord(91, 90))));
		board.boardAddMove(new Move(tileRc, new Coord(91, 90)));

		assertFalse(board.validMove(new Move(tileRs2, new Coord(91, 89))));
		assertTrue(board.validMove(new Move(tileBs, new Coord(90, 91))));
		board.boardAddMove(new Move(tileBs, new Coord(90, 91)));

		assertFalse(board.validMove(new Move(tileBc, new Coord(89, 91))));

		ArrayList<Move> movesOfThisTurn = new ArrayList<Move>();
		movesOfThisTurn.add(new Move(tileRs, mid));
		movesOfThisTurn.add(new Move(tileRc, new Coord(91, 90)));
		assertFalse(board.validMove(new Move(tileBs, new Coord(90, 92)),
				  movesOfThisTurn));
		assertTrue(board.validMove(new Move(tileRd, new Coord(91, 92)),
				  movesOfThisTurn));

	}

	@org.junit.Test
	public void testTotalTurnScore() {
		board = new Board();
		HashSet<Move> movesOfThisTurn = new HashSet<Move>();
		movesOfThisTurn.add(new Move(tileRs, mid));
		movesOfThisTurn.add(new Move(tileRc, new Coord(91, 90)));
		movesOfThisTurn.add(new Move(tileRd, new Coord(91, 92)));
		board.boardAddMove(movesOfThisTurn);
		assertTrue(board.totalTurnScore(movesOfThisTurn) == 3);
		movesOfThisTurn.add(new Move(new Tile(Color.RED, Shape.CROSS),
				  new Coord(91, 93)));
		movesOfThisTurn.add(new Move(new Tile(Color.RED, Shape.STAR),
				  new Coord(91, 94)));
		movesOfThisTurn.add(new Move(new Tile(Color.RED, Shape.CLOVER),
				  new Coord(91, 95)));
		board.boardAddMove(movesOfThisTurn);
		assertTrue(board.totalTurnScore(movesOfThisTurn) == 12);
		board.boardAddMove(new Move(tileBs, new Coord(90, 91)));
		board.boardAddMove(new Move(new Tile(Color.PURPLE ,Shape.SQUARE), new Coord(89, 91)));
		assertTrue(board.totalTurnScore(movesOfThisTurn) == 15);
	}

	@org.junit.Test
	public void testGetConnectedXArrayList() {
		board = new Board();
		HashSet<Move> movesOnX = new HashSet<Move>();
		movesOnX.add(new Move(tileRs, mid));
		movesOnX.add(new Move(tileBs, new Coord(91, 90)));
		movesOnX.add(new Move(tileRc, new Coord(90, 91)));
		movesOnX.add(new Move(tileRd, new Coord(89, 91)));
		board.boardAddMove(movesOnX);
		ArrayList<Tile> correctAnswer = new ArrayList<Tile>();
		correctAnswer.add(tileRc);
		correctAnswer.add(tileRd);
		assertEquals(board.getConnectedXArray(mid),(correctAnswer));

	}

	@org.junit.Test
	public void testGetConnectedYArrayList() {
		board = new Board();
		HashSet<Move> movesOnY = new HashSet<Move>();
		movesOnY.add(new Move(tileRs, mid));
		movesOnY.add(new Move(tileBs, new Coord(90, 91)));
		movesOnY.add(new Move(tileRc, new Coord(91, 90)));
		movesOnY.add(new Move(tileRd, new Coord(91, 89)));
		board.boardAddMove(movesOnY);
		ArrayList<Tile> correctAnswer = new ArrayList<Tile>();
		correctAnswer.add(tileRc);
		correctAnswer.add(tileRd);
		assertEquals(board.getConnectedYArray(mid),(correctAnswer));
	}

}
