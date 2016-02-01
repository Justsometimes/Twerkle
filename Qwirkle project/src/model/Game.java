package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Game {

	private Map<Player, Integer> scores;
	private Board board;
	private int turn;
	private ArrayList<Player> players;
	private TileBag tilebag;

	public Game() {
		board = new Board();
		scores = new HashMap();
		turn = -1;
		players = new ArrayList<Player>();
		tilebag = new TileBag();
	}

	public void nextTurn() {
		turn++;
	}

	public void addPlayer(Player p) {
		if (!scores.containsKey(p)) {
			scores.put(p, 0);
			players.add(p);
		} else {
			System.out.println("Player is already in Game");
		}
	}

	public int getPlayerNr(Player p) {
		return players.indexOf(p);
	}

	public int getPlayerAmount() {
		return players.size();
	}

	public Board getBoard() {
		return board;
	}

	public TileBag getTileBag() {
		return tilebag;
	}

	public int getTurn() {
		return turn % getPlayerAmount();
	}

	public void kickFromGame(Player p) {
		if (players.contains(p)) {
			players.remove(p);
		}
	}

	public Map<Player, Integer> getScores() {
		return scores;
	}

}
