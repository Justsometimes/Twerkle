package tests;

import java.util.ArrayList;
import java.util.HashSet;

import model.Board;
import model.Coord;
import model.Move;
import model.Player;
import model.Tile;
import model.Tile.Color;
import model.Tile.Shape;

import org.junit.Before;

import static org.junit.Assert.*;

public class PlayerTest {

	Player mikon;
	HashSet<Tile> hand;
	Tile Bd;
	Tile Bc;
	Tile Rx;
	Move move1;
	Move move2;
	Board board;

	@Before
	public void setup() {
		hand = new HashSet<Tile>();
		Bd = new Tile(Color.BLUE, Shape.DIAMOND);
		Bc = new Tile(Color.BLUE, Shape.CLOVER);
		Rx = new Tile(Color.RED, Shape.CROSS);
		board = new Board();
		hand.add(Bd);
		mikon = new Player("MIKON", hand);
		move1 = new Move(Bd, new Coord(91, 91));
		move2 = new Move(Bc, new Coord(91, 90));
	}

	@org.junit.Test
	public void testGetName() {
		assertEquals(mikon.getName(), "MIKON");
	}

	@org.junit.Test
	public void testGetHand() {
		assertEquals(mikon.getHand(), hand);
	}

	@org.junit.Test
	public void testRemoveFromHand() {
		mikon.removeFromHand(Bd);
		System.out.println(mikon.getHand().toString());
		assertTrue(mikon.getHand().isEmpty());
	}

	@org.junit.Test
	public void testSetHand() {
		HashSet<Tile> handje = new HashSet<Tile>();
		handje.add(Bc);
		handje.add(Rx);
		mikon.setHand(handje);
		assertTrue(mikon.getHand().containsAll(handje));
	}

	@org.junit.Test
	public void testAddToHand() {
		mikon.addToHand(Rx);
		assertTrue(mikon.getHand().contains(Rx));
	}

	@org.junit.Test
	public void testMakeMove() {
		mikon.makeMove(move1);
		assertEquals(board.getBoardSpaces()[move1.getCoord().getX()][move1
				  .getCoord().getY()], move1.getTile());
		mikon.makeMove(move2);
		assertEquals(board.getBoardSpaces()[move2.getCoord().getX()][move2
				  .getCoord().getY()], null);
		mikon.addToHand(move2.getTile());
		mikon.makeMove(move2);
		assertEquals(board.getBoardSpaces()[move2.getCoord().getX()][move2
				  .getCoord().getY()], move2.getTile());
	}

	@org.junit.Test
	public void testUndoMove() {
		mikon.makeMove(move1);
		mikon.undoMove();
		assertEquals(board.getBoardSpaces()[move1.getCoord().getX()][move1
				  .getCoord().getY()], null);
	}

	@org.junit.Test
	public void testGetCurrentMoves() {
		mikon.addToHand(Bc);
		mikon.makeMove(move1);
		mikon.makeMove(move2);
		ArrayList<Move> currently = new ArrayList<Move>();
		currently.add(move1);
		currently.add(move2);
		assertEquals(mikon.getCurrentMoves().toString(), currently.toString());
	}
	@org.junit.Test
	public void testConfirmTurn() {
		mikon.addToHand(Bc);
		mikon.makeMove(move1);
		mikon.makeMove(move2);
		mikon.confirmTurn();
		assertTrue(mikon.getCurrentMoves().isEmpty());
	}

}
