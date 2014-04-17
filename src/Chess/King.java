package Chess;

import info.gridworld.grid.Location;

import java.awt.Color;
import java.util.ArrayList;

public class King extends ChessPiece {
	public King(ChessBoard<ChessPiece> board, Color color, Location loc) {
		super.setColor(color);
		super.setHasMoved(false);
		super.putSelfInChessBoard(board, loc);
	}

	@Override
	public ArrayList<Location> getValidMoveLocations() {
		ArrayList<Location> moveLocs = new ArrayList<Location>(8);
		ArrayList<Location> adjLocs = super.getChessBoard()
				.getValidAdjacentLocations(getLocation());
		for (Location adjLoc : adjLocs) {
			// should return true if the color is returned as null or another
			// color and the king won't be in check there
			if (super.getChessBoard().getChessPieceColorAtLoc(adjLoc) != super
					.getColor() && !wouldBeInCheck(adjLoc)) {
				moveLocs.add(adjLoc);
			}
		}
		//Remove king
		
		Location curr = super.getLocation();
		Color c = super.getColor();
		
		ArrayList<Location> dumbLocs = new ArrayList<Location>(8);
		
		for (Location testing : moveLocs) {
			ChessBoard<ChessPiece> b = super.getChessBoard();
			if (b.isChessPieceAtLoc(testing) && b.getChessPieceColorAtLoc(testing) != c){
				ChessPiece derp = b.getChessPieceAtLoc(testing);
				derp.removeSelfFromChessBoard();
				super.removeSelfFromChessBoard();
				super.putSelfInChessBoard(b, testing);
				if (!wouldBeInCheck(testing)) {
					dumbLocs.add(testing);
					//moveLocs.remove(testing);
				}
				super.removeSelfFromChessBoard();
				super.putSelfInChessBoard(b, curr);
				derp.putSelfInChessBoard(b, testing);
			} else {
				super.removeSelfFromChessBoard();
				super.putSelfInChessBoard(b, testing);
				if (!wouldBeInCheck(testing)) {
					dumbLocs.add(testing);
					//moveLocs.remove(testing);
				}
				super.removeSelfFromChessBoard();
				super.putSelfInChessBoard(b, curr);
			}
		}
		
		
		// don't forget castle locations
		dumbLocs.addAll(getValidCastleLocations());
		return dumbLocs;
	}

	/**
	 * Use this to see if this King is in check
	 * 
	 * @return true if this King is in check, false otherwise
	 */
	public boolean isInCheck() {
		return wouldBeInCheck(super.getLocation());
	}

	private boolean wouldBeInCheck(Location potentialLoc) {
		// this is terrible brute force
		ArrayList<Location> allPieces = super.getChessBoard()
				.getOccupiedLocations();
		for (Location curLoc : allPieces) {
			ChessPiece curPiece = super.getChessBoard().getChessPieceAtLoc(
					curLoc);
			// don't check friendly pieces
			if (curPiece.getColor() != super.getColor()) {
				// if any other piece can move at potentialLoc then king would
				// be in check
				if (!(curPiece instanceof King) && !(curPiece instanceof Pawn)) {// try
																					// to
																					// prevent
																					// infinite
																					// loop
					if (curPiece.getValidMoveLocations().contains(potentialLoc)) {
						return true;
					}
				} else {
					if (curPiece instanceof Pawn) {
						if (((Pawn) curPiece).getCaptureDirections().contains(
								potentialLoc)) {
							return true;
						}
					} else {
						// don't move next to an opposing king
						for (int i = 0; i < 360; i += 45) {
							if (potentialLoc.equals(curLoc
									.getAdjacentLocation(i))) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * This method returns the potential move locations for a castling move
	 * performed by This King
	 * 
	 * @return The possible locations that this King will end up in after
	 *         castling
	 */
	private ArrayList<Location> getValidCastleLocations() {
		// see rules of chess (make sure that king is not castling through
		// check)
		// both king and rook to perform castle with
		// have had to not move
		ArrayList<Location> castleLocs = new ArrayList<Location>(2);
		if (super.hasMoved() || isInCheck()) {
			return castleLocs;
		}

		// check left then right
		ArrayList<Object> locToAdd = checkCastleDirection(Location.WEST);
		if (locToAdd.size() == 2) {
			castleLocs.add((Location) locToAdd.get(0));
		}
		locToAdd = checkCastleDirection(Location.EAST);
		if (locToAdd.size() == 2) {
			castleLocs.add((Location) locToAdd.get(0));
		}
		return castleLocs;
	}

	// needs checking
	private ArrayList<Object> checkCastleDirection(int direction) {
		ArrayList<Object> returnPair = new ArrayList<Object>(2);
		ChessBoard<ChessPiece> br = super.getChessBoard();
		Location curLoc = super.getLocation().getAdjacentLocation(direction);
		// check that there is an empty path to a rook that has not moved
		while (br.isValid(curLoc)) {
			ChessPiece curPiece = br.getChessPieceAtLoc(curLoc);
			if (curPiece != null) {
				// if you run into a rook you might be able to castle, else you
				// can't
				if (curPiece instanceof Rook) {
					// if rook hasn't moved
					if (!curPiece.hasMoved()) {
						// now check the king is not castling through check
						Location kingMoveThrough = super.getLocation()
								.getAdjacentLocation(direction);
						Location kingFinalLocation = kingMoveThrough
								.getAdjacentLocation(direction);
						// if not castling through or ending up in check
						if (!wouldBeInCheck(kingMoveThrough)
								&& !wouldBeInCheck(kingFinalLocation)) {
							returnPair.add(kingFinalLocation);
							returnPair.add(curPiece);
							return returnPair;
						}
					}
				}
				break;// if there was a piece there either castling was possible
						// or it isn't
			}
			curLoc = curLoc.getAdjacentLocation(direction);
		}
		return returnPair;
	}

	/**
	 * 
	 * @param loc
	 *            the location that the king ends up in after castling
	 * @return
	 */
	public boolean castleTo(Location loc) {
		// make sure is loc is a valid castle location
		// get the rook that is going to castle with the king
		// move the king, then move the rook
		int castleDir = super.getLocation().getDirectionToward(loc);
		ArrayList<Object> locAndRook = checkCastleDirection(castleDir);

		if (!loc.equals((Location) locAndRook.get(0))) {
			return false;
		}
		if (castleDir < 180) {
			castleDir += 180;
		} else {
			castleDir -= 180;
		}
		((Rook) locAndRook.get(1)).doMove(loc.getAdjacentLocation(castleDir));

		super.doMove(loc);
		return true;
	}

	public ChessPiece moveTo(Location loc) throws IllegalMoveException,
			PieceNeedsToBeReplacedException {
		if (getValidCastleLocations().contains(loc)) {
			castleTo(loc);
			return null;
		}
		if (getValidMoveLocations().contains(loc)) {
			return super.moveTo(loc);
		}
		throw new IllegalMoveException("This is not a valid move location.");
	}

}
