package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {

	private Map<Player, Integer> scores;
	private Board board;
	private int turn;
	private ArrayList<Player> players;
	private TileBag tilebag;

	/**
	 * constructor for Game, which contains a Board board on which the game is played,
	 * a map 'scores' containing the scores of the players, a 'turn' turn counter,
	 * a list players which contains all the Players and a Tilebag tilebag.
	 */
	public Game() {
		board = new Board();
		scores = new HashMap();
		turn = -1;
		players = new ArrayList<Player>();
		tilebag = new TileBag();
	}

	/**
	 * Increments the turn value by one. 
	 * turn is used to determine which player has the current turn.
	 * It keeps track of how many turns pass.
	 */
	public void nextTurn() {
		turn++;
	}

	/**
	 * adds the player p to the game by adding him to the players list and score map.
	 * @param p
	 */
	public void addPlayer(Player p) {
		if (!scores.containsKey(p)) {
			scores.put(p, 0);
			players.add(p);
		} else {
			System.out.println("Player is already in Game");
		}
	}

	/**
	 * retrieves player p's number by looking up his index in the players list. 
	 * @param p
	 * @return index of player p in the list players.
	 */
	public int getPlayerNr(Player p) {
		return players.indexOf(p);
	}

	/**
	 * gets the amount of players in this game.
	 * @return players list size
	 */
	public int getPlayerAmount() {
		return players.size();
	}

	/**
	 * gets the board of the game.
	 * @return board
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * gets the tilebag of the game.
	 * @return tilebag
	 */
	public TileBag getTileBag() {
		return tilebag;
	}

	/**
	 * determines which player's turn it currently is by calculating turn mod getPlayerAmount. 
	 * @return turn % getPlayerAmount()
	 */
	public int getTurn() {
		return turn % getPlayerAmount();
	}

	/**
	 * kicks the player p from the game by removing him from the player list and the score map.
	 * @param p
	 */
	public void kickFromGame(Player p) {
		if (players.contains(p)) {
			players.remove(p);
		}
	}
	
	/**
	 * getter for the scores map.
	 * @return scores
	 */
	public Map<Player, Integer> getScores() {
		return scores;
	}

}
