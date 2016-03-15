package tests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import model.Game;
import model.Player;
import model.Tile;

import org.junit.Before;

import static org.junit.Assert.*;

public class GameTest {

	private Game game;
	Player player1; 
	Player player2;
	Player player3;
	Player player4;

	@Before
	public void setup() {
		game = new Game();
		 player1 = new Player("MIKON", new HashSet<Tile>());
		 player2 = new Player("UMU", new HashSet<Tile>());
		 player3 = new Player("ert", new HashSet<Tile>());
		 player4 = new Player("neen", new HashSet<Tile>());
	}

	@org.junit.Test
	public void addPlayerTest() {
		Player player1 = new Player("MIKON", new HashSet<Tile>());
		Player player2 = new Player("UMU", new HashSet<Tile>());
		Map<Player, Integer> scores = new HashMap();
		scores.put(player1, 0);
		game.addPlayer(player1);
		assertEquals(game.getScores(), scores);
		game.addPlayer(player1);
		assertEquals(game.getScores(), scores);
		game.addPlayer(player2);
		scores.put(player2, 0);
		assertEquals(game.getScores(), scores);
	}

	@org.junit.Test
	public void nextTurnTest() {
		game.addPlayer(player1);
		game.addPlayer(player2);
		game.addPlayer(player3);
		game.addPlayer(player4);
		
		int i = 0;
		game.nextTurn();
		assertEquals(game.getTurn(), i);
	}
	
	@org.junit.Test
	public void testGetTurn(){
		game.addPlayer(player1);
		game.addPlayer(player2);
		game.addPlayer(player3);
		game.addPlayer(player4);
		
		int i = 1;
		game.nextTurn();
		game.nextTurn();
		game.nextTurn();
		game.nextTurn();
		game.nextTurn();
		game.nextTurn();
		assertEquals(game.getTurn(), 1);
	}
	
	@org.junit.Test
	public void testGetPlayerNr(){
		game.addPlayer(player1);
		game.addPlayer(player2);
		game.addPlayer(player3);
		game.addPlayer(player4);
		
		assertEquals(game.getPlayerNr(player3), 2);
	}
	
	@org.junit.Test
	public void testGetPlayerAmount(){
		game.addPlayer(player1);
		game.addPlayer(player2);
		game.addPlayer(player3);
		game.addPlayer(player4);
		
		assertEquals(game.getPlayerAmount(), 4);
	}
	
	@org.junit.Test
	public void testGetScores(){
		Map<Player, Integer> scoreCompare = new HashMap<Player, Integer>();
		
		scoreCompare.put(player1, 0);
		scoreCompare.put(player2, 0);
		scoreCompare.put(player3, 0);
		scoreCompare.put(player4, 0);
		
		game.addPlayer(player1);
		game.addPlayer(player2);
		game.addPlayer(player3);
		game.addPlayer(player4);
		
		assertEquals(game.getScores(), scoreCompare);
	}

}
