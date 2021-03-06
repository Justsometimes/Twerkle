package model;

import java.util.ArrayList;
import java.util.Set;

import model.*;

public interface Strategy {

	/**
	 * determines what Move the strategy favors making on a given board.
	 * @return
	 */
	public ArrayList<Move> determineMoves();

}
