package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Tile.Color;
import model.Tile.Shape;

public class Board {

	public static Tile[][] boardSpaces;
	public static final int DIM = 183;
	public static final int MID = 91;
	public static final int powerMoveLength = 6;
	public static Set<Move> usedSpaces;

	/**
	 * constructor for Board with an array of arrays simulating the board and a
	 * usedSpaces which contains all Moves on the Board.
	 */
	public Board() {
		boardSpaces = new Tile[DIM + 2][DIM + 2];
		usedSpaces = new HashSet<Move>();
	}

	// @ pure;
	/**
	 * Alternative calling of validMove check.
	 * 
	 * @param move
	 * @return if the Move made is a valid Move
	 */
	public boolean validMove(Move move) {
		return validMove(move, new ArrayList<Move>());
	}

	/**
	 * Checks if the theMove is valid. The spaces adjacent to the Coord of
	 * theMove have to contain Tiles of the same Colour or Shape as the Tile in
	 * theMove. theMove may not connect a line of same coloured Tiles and a line
	 * of same shaped Tiles at once if these Tile lines are on the same axis.
	 * theMove may not be placed next to a line of Tiles in which the Tile in
	 * theMove already exists. theMove has to be aligned and connected to a line
	 * of Tiles which contains previous Moves of the current turn, if those
	 * exist. theMove has to be placed inside the boundaries of the Board.
	 * theMove has to be placed at the center of the Board if no previously
	 * placed Tiles exist on the Board (in other words, if the center of the
	 * Board is empty the Move has to be placed at the center).
	 * 
	 * @param theMove
	 * @param movesMade
	 * @return if the Move is valid according to all the requirements given in
	 *         this documentation
	 */
	// @ pure;
	// @ ensures \result == (inLineV(theMove) && inLineH(theMove)) ||
	// (theMove.getCoord() == new Coord(MID,MID);
	public boolean validMove(Move theMove, List<Move> movesMade) {
		boolean answer = true;
		if (theMove != null && movesMade != null) {
			boolean firstMove = (boardSpaces[MID][MID] == null);
			boolean oldY = true;
			boolean oldX = true;
			if (!firstMove) {
				for (Move m : movesMade) {
					if (m.getCoord().getX() != theMove.getCoord().getX()) {
						oldX = false;
					}
					if (m.getCoord().getY() != theMove.getCoord().getY()) {
						oldY = false;
					}
				}
				if (!oldX && !oldY) {
					answer = false;
				}

				if (boardSpaces[theMove.getCoord().getX()][theMove.getCoord()
						.getY()] != null) {
					answer = false;
				}
				int adjecends = 0;
				for (int i = 0; i < 4; i++) {
					Coord c = theMove.getCoord().getAdjacentCoords()[i];
					if (boardSpaces[c.getX()][c.getY()] != null) {
						adjecends++;
					}
				}
				if (adjecends == 0) {
					answer = false;
				}
				if (!(inLineV(theMove) && inLineH(theMove))) {
					answer = false;
				}
			} else if (theMove.getCoord().getX() != MID
					|| theMove.getCoord().getY() != MID) {
				answer = false;
			}
		}
		return answer;
	}

	/**
	 * Checks if the Move m is valid on the Y axis.
	 * 
	 * @param m
	 * @return if the Move m is valid on the Y axis
	 */
	// @ pure;
	// @ requires m != null;
	// @ ensures \result == (\forall Tile tit; m.getShape == tit.getShape) ^
	// (\forall Tile tit; m.getColor == tit.getColor);
	public boolean inLineV(Move m) {
		Coord c = m.getCoord();
		Tile t = m.getTile();
		ArrayList<Tile> tiles = getConnectedYArray(c);
		boolean answer = true;
		if (!tiles.isEmpty()) {
			Shape shapes = null;
			Color colors = null;
			boolean shapeRelation = (t.getShape() == tiles.get(0).getShape());
			boolean colorRelation = (t.getColor() == tiles.get(0).getColor());
			if (!(shapeRelation ^ colorRelation)) {
				answer = false;
			}
			if (answer) {
				for (Tile tt : tiles) {
					if (t.getColor().equals(tt.getColor())
							&& t.getShape().equals(tt.getShape())) {
						answer = false;
						break;
					}

					if (!t.getColor().equals(tt.getColor()) && !shapeRelation) {
						answer = false;
						break;
					}

					if (!t.getShape().equals(tt.getShape()) && !colorRelation) {
						answer = false;
						break;
					}
				}
			}
		}
		return answer;
	}

	/**
	 * Checks if the Move m is valid on the X axis.
	 * 
	 * @param m
	 * @return if the Move m is valid on the X axis
	 */
	// @ pure;
	// @ requires m != null;
	// @ ensures \result == (\forall Tile tit; m.getShape == tit.getShape) ||
	// (\forall Tile tit; m.getColor == tit.getColor);
	public boolean inLineH(Move m) {
		Coord c = m.getCoord();
		Tile t = m.getTile();
		ArrayList<Tile> tiles = getConnectedXArray(c);
		boolean answer = true;
		if (!tiles.isEmpty()) {
			boolean shapeRelation = (t.getShape() == tiles.get(0).getShape());
			boolean colorRelation = (t.getColor() == tiles.get(0).getColor());
			if (!(shapeRelation ^ colorRelation)) {
				answer = false;
			}
			if (answer) {
				for (Tile tt : tiles) {
					if (tt.equals(t)) {
						answer = false;
						break;
					}
					if (!t.getColor().equals(tt.getColor()) && !shapeRelation) {
						answer = false;
						break;
					}
					if (!t.getShape().equals(tt.getShape()) && !colorRelation) {
						answer = false;
						break;
					}
				}
			}
		}
		return answer;
	}

	/**
	 * determines the score a set of moves is worth by looking at all the Tiles
	 * arrays connected to the Moves in turninfo.
	 * 
	 * @param turninfo
	 * @return score of the set of moves
	 */
	// @ pure;
	// @ requires turninfo != null;
	// @ ensures result > 1;
	public int totalTurnScore(Set<Move> turninfo) {
		ArrayList<Move> moves = new ArrayList<Move>();
		moves.addAll(turninfo);
		int result = 0;
		if (moves.size() > 1) {
			if (moves.get(0).getCoord().getY() == moves.get(1).getCoord()
					.getY()) {
				result = result + HTurnScore(moves.get(0));
				for (Move move : moves) {
					result = result + VTurnScore(move);
				}
			} else {
				result = result + VTurnScore(moves.get(0));
				for (Move move : moves) {
					result = result + HTurnScore(move);
				}
			}
		} else {
			result = +VTurnScore(moves.get(0)) + HTurnScore(moves.get(0));

		}
		return result;
	}

	/**
	 * determines the score of move on its horizontal axis.
	 * 
	 * @param move
	 * @return horizontal caused score of move
	 */
	// @ pure;
	// @ requires move != null;
	public int HTurnScore(Move move) {
		int scoreForTurn = 0;
		scoreForTurn = getConnectedXArray(move.getCoord()).size();
		if (scoreForTurn > 0) {
			scoreForTurn++;
		}
		if (scoreForTurn == 6) {
			scoreForTurn = scoreForTurn + 6;
		}
		return scoreForTurn;
	}

	/**
	 * determines the score of move on its vertical axis.
	 * 
	 * @param move
	 * @return vertical caused score of move
	 */
	// @ pure;
	// @ requires move != null;
	public int VTurnScore(Move move) {
		int scoreForTurn = 0;
		scoreForTurn = getConnectedYArray(move.getCoord()).size();
		if (scoreForTurn > 0) {
			scoreForTurn++;
		}
		if (scoreForTurn == 6) {
			scoreForTurn = scoreForTurn + 6;
		}
		return scoreForTurn;
	}

	/**
	 * Adds the Move move to the board. The Tile in move will be placed on the
	 * board at the Coord of move.
	 * 
	 * @param move
	 */
	// @ requires move != null;
	// @ requires 0 >= move.getCoord().getX() >= DIM;
	// @ requires 0 >= move.getCoord().getY() >= DIM;
	// @ requires move.getTile != null;
	// @ ensures boardSpaces[move.getCoord().getX()][move.getCoord().getY()] ==
	// move.getTile();
	public void boardAddMove(Move move) {
		if (move != null) {
			boardSpaces[move.getCoord().getX()][move.getCoord().getY()] = move
					.getTile();
			usedSpaces = getUsedSpaces();
		} else {
			// exception for empty move cannot be placed
		}
	}

	/**
	 * Adds multiple Moves to the board by executing the boardAddMove (for one
	 * Move) method for each of the moves inside the set move.
	 * 
	 * @param move
	 */
	// @ requires move != null;
	// @ ensures (\forall movie;
	// [movie.getCoord().getX()][movie.getCoord().getY()] == movie.getTile());
	public void boardAddMove(Set<Move> move) {
		for (Move movie : move) {
			boardAddMove(movie);
		}
	}

	/**
	 * Removes a Tile from the Board at the given coordinates. (Used when
	 * undoing a move)
	 * 
	 * @param coord
	 */
	// @ requires coord != null;
	// @ requires 0 >= coord.getX() >= DIM;
	// @ requires 0 >= coord.getY() >= DIM;
	// @ ensures boardSpaces[coord.getX()][coord.getY()] == null
	public void boardRemove(Coord coord) {
		boardSpaces[coord.getX()][coord.getY()] = null;
	}

	/**
	 * Creates a set of Moves that exist on the board.
	 * 
	 * @return
	 */
	// @ pure;
	// @ ensures (\forall boardSpaces[i][j] != null; result.contains(new
	// Move(boardSpaces[i][j], new Coord(i, j)));
	public Set<Move> getUsedSpaces() {
		Set<Move> result = new HashSet<Move>();
		for (int i = 0; i < DIM; i++) {
			for (int j = 0; j < DIM; j++) {
				if (boardSpaces[i][j] != null) {
					result.add(new Move(boardSpaces[i][j], new Coord(i, j)));
				}
			}
		}
		return result;
	}

	/**
	 * getter for the boardSpaces.
	 * 
	 * @return boardSpaces
	 */
	// @ pure
	public Tile[][] getBoardSpaces() {
		return boardSpaces;
	}

	/**
	 * creates an ArrayList containing all Tiles that are connected to the given
	 * coordinates c horizontally.
	 * 
	 * @param c
	 * @return all Tiles that are connected to the given coordinates c
	 *         horizontally
	 */
	// @ pure
	// @ ensures (\forall int c.getX()-5<i<c.getX()+5 &&
	// boardSpaces[i][c.getY()]
	// != null; tiles.contains(boardSpaces[i][c.getY()]);
	public ArrayList<Tile> getConnectedXArray(Coord c) {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		int x = c.getX();
		int y = c.getY();

		for (int i = 1; i < powerMoveLength; i++) {
			Tile tit = boardSpaces[x + i][y];
			if (tit == null) {
				break;
			}
			tiles.add(tit);
		}
		for (int i = 1; i < powerMoveLength; i++) {
			Tile tit = boardSpaces[x - i][y];
			if (tit == null) {
				break;
			}
			tiles.add(tit);
		}
		return tiles;
	}

	/**
	 * creates an ArrayList containing all Tiles that are connected to the given
	 * coordinates c vertically.
	 * 
	 * @param c
	 * @return all Tiles that are connected to the given coordinates c
	 *         vertically
	 */
	// @ pure
	// @ ensures (\forall int c.getY()-5<i<c.getY()+5 &&
	// boardSpaces[c.getX()][i]
	// != null; tiles.contains(boardSpaces[c.getX()][i]);
	public ArrayList<Tile> getConnectedYArray(Coord c) {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		int x = c.getX();
		int y = c.getY();

		for (int i = 1; i < powerMoveLength; i++) {
			Tile tit = boardSpaces[x][y + i];
			if (tit == null) {
				break;
			}
			tiles.add(tit);
		}
		for (int i = 1; i < powerMoveLength; i++) {
			Tile tit = boardSpaces[x][y - i];
			if (tit == null) {
				break;
			}
			tiles.add(tit);
		}
		return tiles;
	}

	/**
	 * toSting method for Board, should be self explanatory. (dynamic)
	 */
	// @ pure;
	public String toString() {
		String sideBox = "|";
		String topBox = "----";
		// The specialBox characters first were good looking UTF-8 boxdrawing
		// characters
		// But since UTF-8 causes numerous problems with saving and reading, I
		// had to replace them with +
		String specialBox[] = { "+\n", "   +", "+\n", "   +", "+", "+", "+",
				"+\n", "   +" };
		int dynamicDimension = 0;
		StringBuilder sb = new StringBuilder();
		for (Move m : usedSpaces) {
			dynamicDimension = Math.max(dynamicDimension,
					Math.abs(MID - m.getCoord().getX()));
			dynamicDimension = Math.max(dynamicDimension,
					Math.abs(MID - m.getCoord().getY()));
		}
		dynamicDimension++;
		sb.append("   ");
		for (int i = 0; i < ((dynamicDimension * 2) + 1); i++) {
			int x = (MID + i - dynamicDimension);
			if (x < 10) {
				sb.append("-  " + x + " ");
			} else if (x < 100) {
				sb.append("- " + x + " ");
			} else {
				sb.append("-" + x + " ");
			}
		}
		sb.append("\n");
		int totalDimension = ((((dynamicDimension * 2) + 1) * 2) + 1);
		for (int i = 0; i < totalDimension; i++) {
			for (int j = 0; j < totalDimension; j++) {
				if (i == 0 && j == 0) {
					sb.append(specialBox[1]);
				} else if (i == 0 && j == totalDimension - 1) {
					sb.append(specialBox[0]);
				} else if (i == totalDimension - 1 && j == 0) {
					sb.append(specialBox[3]);
				} else if (i == totalDimension - 1 && j == totalDimension - 1) {
					sb.append(specialBox[2]);
				} else if (i == 0 && j % 2 == 0) {
					sb.append(specialBox[4]);
				} else if (i == totalDimension - 1 && j % 2 == 0) {
					sb.append(specialBox[5]);
				} else if (i % 2 == 0 && j == 0) {
					sb.append(specialBox[8]);
				} else if (i % 2 == 0 && j == totalDimension - 1) {
					sb.append(specialBox[7]);
				} else if (i % 2 == 0 && j % 2 == 0) {
					sb.append(specialBox[6]);
				} else if (i % 2 == 0 && j % 2 == 1) {
					sb.append(topBox);
				} else if (i % 2 == 1 && j == 0) {
					int y = (MID + ((i) / 2) - dynamicDimension);
					if (y < 10) {
						sb.append("  " + y + sideBox);
					} else if (y < 100) {
						sb.append(" " + y + sideBox);
					} else {
						sb.append("" + y + sideBox);
					}
				} else if (i % 2 == 1 && j == totalDimension - 1) {
					sb.append(sideBox + "\n");
					// sb.append("\n");
				} else if (i % 2 == 1 && j % 2 == 0) {
					sb.append(sideBox);
				} else if (i % 2 == 1 && j % 2 == 1) {
					if (boardSpaces[MID - dynamicDimension + j / 2][MID
							- dynamicDimension + i / 2] != null) {
						sb.append(" "
								+ boardSpaces[MID - dynamicDimension + j / 2][MID
										- dynamicDimension + i / 2].toString()
								+ " ");
					} else {
						sb.append("    ");
					}
				}
			}
		}
		return sb.toString();
	}
}
