package player;

import java.util.Set;

import model.*;

public interface Strategy {
	
	public Set<Move> determineMoves();

}
