package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {

	public static Tile[][] boardSpaces;
	public static final int DIM = 183;
	public static final int MID = 91;
	public static final int powerMoveLength = 6;
	public static Set<Move> usedSpaces;

	public Board() {
		boardSpaces = new Tile[DIM + 2][DIM + 2];
		usedSpaces = new HashSet<Move>();
	}

	// @ pure;
	/**
	 * Alternative calling of validMove check
	 * 
	 * @param move
	 * @return if the move made is a valid move
	 */
	public boolean validMove(Move move) {
		return validMove(move, new ArrayList<Move>());
	}

	/**
	 * Checks if the current move is valid. The spaces adjacent to the
	 * coordinates of theMove have to contain tiles of the same colour or shape
	 * as the tile in theMove. theMove may not connect a line of same coloured
	 * tiles and a line of same shaped tiles at once if these tile lines are on
	 * the same axis. theMove may not be placed next to a line of tiles in which
	 * the Tile in theMove already exists. theMove has to be aligned and
	 * connected to a line of tiles which contains previous moves of the current
	 * turn, if those exist. theMove has to be placed inside the boundaries of
	 * the board. theMove has to be placed at the center of the board if no
	 * previously placed tiles exist on the board (in other words, if the center
	 * of the board is empty the move has to be placed at the center).
	 * @param theMove
	 * @param movesMade
	 * @return if the move is valid according to all the requirements given in this documentation
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
//				TODO /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				if (!(inLineV(theMove) && inLineH(theMove))) {
					answer = false;
				}
			} else if (theMove.getCoord().getX() != MID
					|| theMove.getCoord().getY() != MID) {
				answer = false;
			}
		}
		System.out.println("isValid result = " + answer);
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
	// @ ensures \result == (\forall Tile tit; m.getShape == tit.getShape) ||
	// (\forall Tile tit; m.getColor == tit.getColor);
	public boolean inLineV(Move m) {
		// TODO
		System.out.println("inLineV is activated");
		// TODO
		Coord c = m.getCoord();
		Tile t = m.getTile();
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
		boolean answer = true;
		//TODO
		System.out.println("The vertical row of the move contains these tiles: " + tiles);
		//TODO
		if (!tiles.isEmpty()) {
			boolean shapeRelation = (t.getShape() == tiles.get(0).getShape());
			boolean colorRelation = (t.getColor() == tiles.get(0).getColor());
			if (!(shapeRelation ^ colorRelation)) {
				answer = false;
			}
			if (answer) {
				for (Tile tt : tiles) {
					if (tt.getShape() == t.getShape() && !shapeRelation) {
						answer = false;
						break;
					} else if (tt.getColor() == t.getColor() && !colorRelation) {
						answer = false;
						break;
					}
				}
			}
		}
		System.out.println("InlineV result = " + answer);
		return answer;
	}

	// @ pure;
	// @ requires m != null;
	// @ ensures \result == (\forall Tile tit; m.getShape == tit.getShape) ||
	// (\forall Tile tit; m.getColor == tit.getColor);
	public boolean inLineH(Move m) {
		//TODO
		System.out.println("inLineH is activated");
		//TODO
		Coord c = m.getCoord();
		Tile t = m.getTile();
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
		boolean answer = true;
		//TODO
		System.out.println("The horizontal row of the move contains these tiles: " + tiles);
		//TODO
		if (!tiles.isEmpty()) {
			boolean shapeRelation = (t.getShape() == tiles.get(0).getShape());
			boolean colorRelation = (t.getColor() == tiles.get(0).getColor());
			if (!(shapeRelation ^ colorRelation)) {
				answer = false;
			}
			if (answer) {
				for (Tile tt : tiles) {
					if (tt.getShape() == t.getShape() && !shapeRelation) {
						answer = false;
						break;
					} else if (tt.getColor() == t.getColor() && !colorRelation) {
						answer = false;
						break;
					}
				}
			}
		}
		System.out.println("InlineH result = " + answer);
		return answer;
	}

	// @ requires move != null;
	// @ requires 0 >= move.getCoord().getX() >= DIM;
	// @ requires 0 >= move.getCoord().getY() >= DIM;
	// @ requires move.getTile != null;
	// @ ensures boardSpaces[move.getCoord().getX()][move.getCoord().getY()] ==
	// move.getTile();
	public void boardAddMove(Move move) {
		System.out.println("The value of move is: " + move.toString());
		if (move != null) {
			boardSpaces[move.getCoord().getX()][move.getCoord().getY()] = move
					.getTile();
			System.out.println(boardSpaces[91][91]
					+ " is the value of field 91 91");
			usedSpaces = getUsedSpaces();
		} else {
			// exception for empty move cannot be placed
		}
	}

	// @ requires move != null;
	// @ ensures (\forall movie;
	// [movie.getCoord().getX()][movie.getCoord().getY()] == movie.getTile());
	public void boardAddMove(Set<Move> move) {
		for (Move movie : move) {
			boardAddMove(movie);
		}
	}

	// @ requires coord != null;
	// @ requires 0 >= coord.getX() >= DIM;
	// @ requires 0 >= coord.getY() >= DIM;
	// @ ensures boardSpaces[coord.getX()][coord.getY()] == null
	public void boardRemove(Coord coord) {
		boardSpaces[coord.getX()][coord.getY()] = null;
	}

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
