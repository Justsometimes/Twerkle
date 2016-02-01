package model;

import java.util.Set;

public class ComputerPlayer extends Player {

	private Strategy strat;

	public ComputerPlayer(String name, Set<Tile> hand) {
		super(name, hand);
		strat = new RetardedStrategy(super.getBoard(), hand);
	}

	public ComputerPlayer(String name, Set<Tile> hand, Strategy strat) {
		super(name, hand);
		this.strat = strat;
	}

}
