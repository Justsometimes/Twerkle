package model;

import java.util.Set;

public class ComputerPlayer extends Player {

	private Strategy strat;

	/**
	 * constructor for a computerplayer, without a given strategy.
	 * It will create a computerplayer with the most basic strategy (RetardedStrategy).
	 * it will play under the given name 'name' , using the hand  'hand'.
	 * @param name
	 * @param hand
	 */
	public ComputerPlayer(String name, Set<Tile> hand) {
		super(name, hand);
		strat = new RetardedStrategy(super.getBoard(), hand);
	}

	/**
	 * constructor for a computerplayer with a given strategy strat.
	 * it will play under the given name 'name' , using the hand  'hand'.
	 * @param name
	 * @param hand
	 * @param strat
	 */
	public ComputerPlayer(String name, Set<Tile> hand, Strategy strat) {
		super(name, hand);
		this.strat = strat;
	}

}
