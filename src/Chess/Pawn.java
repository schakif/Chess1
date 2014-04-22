package Chess;

import info.gridworld.grid.Location;

import java.awt.Color;
import java.util.ArrayList;

public class Pawn extends ChessPiece {
	private int direction;

	public Pawn(ChessBoard<ChessPiece> board, Color color, Location loc) {
		super.setColor(color);
		super.setHasMoved(false);
		super.putSelfInChessBoard(board, loc);

		if (color.equals(Color.WHITE)) {
			direction = Location.NORTH;
		} else {
			direction = Location.SOUTH;
		}
	}

	@Override
	public ArrayList<Location> getValidMoveLocations() {
		// TODO Auto-generated method stub
		ArrayList<Location> validLocs = new ArrayList<Location>();
		// get possible move locations
		validLocs.addAll(checkValidLocInDirection(direction));
		// get capture locations
		validLocs.addAll(getCaptureDirections());
		
		if (super.isThisBad()) {
			//Check Mate Purposes....
			Color c = super.getColor();
			King k;
			//if you are in check
			
			ChessBoard<ChessPiece> b = super.getChessBoard();
			ArrayList<King> kings = b.getKingsOnChessBoard();
			if (kings.get(0).getColor() == c) {
				k = kings.get(0);
			}
			else {
				k = kings.get(1);
			}
			if (k.isInCheck()){
				validLocs = super.checkLocations(validLocs);
			}
		}
		return validLocs;
		

	}

	public ArrayList<Location> getCaptureDirections() {
		ArrayList<Location> validLocs = new ArrayList<Location>(2);
		
		int[] captureDirections = new int[2];
		captureDirections[0] = direction + 45;
		captureDirections[1] = direction + 315;
		ChessBoard<ChessPiece> myBoard = super.getChessBoard();
		for (int i = 0; i < captureDirections.length; i++) {
			Location possibleLocation = getLocation().getAdjacentLocation(
					captureDirections[i]);
			if (myBoard.isValid(possibleLocation)) {
				ChessPiece posPiece = myBoard.get(possibleLocation);
				if (posPiece != null
						&& !(posPiece.getColor().equals(getColor()))) {
					validLocs.add(possibleLocation);
				}
			}

		}
		return validLocs;
	}

	private ArrayList<Location> checkValidLocInDirection(int direction) {
		ArrayList<Location> validLocs = new ArrayList<Location>();
		ChessBoard<ChessPiece> myBoard = super.getChessBoard();
		Location myLoc = super.getLocation();
		Location nextLoc = myLoc.getAdjacentLocation(direction);
		// check 1 space ahead
		if (myBoard != null && myBoard.isValid(nextLoc)) {
			ChessPiece pieceAtLoc = myBoard.get(nextLoc);
			// if no piece, this can move there
			if (pieceAtLoc == null) {
				validLocs.add(nextLoc);
				// IF NOT MOVED, CHECKS TWO AHEAD
				if (!super.hasMoved()) {
					nextLoc = nextLoc.getAdjacentLocation(direction);
					pieceAtLoc = (ChessPiece) myBoard.get(nextLoc);
					if (pieceAtLoc == null) {
						validLocs.add(nextLoc);
					}
				}
			}

		}
		return validLocs;
	}

	/**
	 * @throws IllegalMoveException
	 *             if this Pawn was not able to move to the loc specified
	 * @throws PieceNeedsToBeReplacedException
	 *             if this Pawn is at either edge of the board where it needs to
	 *             be replaced
	 */
	@Override
	public ChessPiece moveTo(Location loc) throws IllegalMoveException,
			PieceNeedsToBeReplacedException {
		ChessPiece potentialTakenPiece = super.moveTo(loc);
		// if pawn needs to be replaced by a piece throw an IllegalMoveException
		if (super.getLocation().getRow() == 7
				|| super.getLocation().getRow() == 0) {
			throw new PieceNeedsToBeReplacedException(
					"Pawn needs to be replaced", potentialTakenPiece);
		}
		return potentialTakenPiece;
	}
}
