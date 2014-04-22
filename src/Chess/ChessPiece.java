package Chess;

import java.awt.Color;
import java.util.ArrayList;

import info.gridworld.grid.Location;

public abstract class ChessPiece{
	private ChessBoard<ChessPiece> chessboard;
	private boolean hasMoved;
	private Location location;
	private Color color;
	private boolean thisIsBad = false;//fix this eventually
	/**
	 * Returns all valid move locations for a Chess Piece
	 * @return an ArrayList of Valid Move Locations
	 */
	public abstract ArrayList<Location> getValidMoveLocations();
	
	public ArrayList<Location> checkLocations(ArrayList<Location> validLocs) {
		Location curr = getLocation();
		Color c = getColor();
		King k;
		ArrayList<Location> dumbLocs = new ArrayList<Location>();
		//if you are in check
		
		ChessBoard<ChessPiece> b = getChessBoard();
		ArrayList<King> kings = b.getKingsOnChessBoard();
		
		if (kings.get(0).getColor() == c) {
			k = kings.get(0);
		}
		else {
			k = kings.get(1);
		}
		
		if (k.isInCheck()){
			
			for (Location testing : validLocs) {
				b = getChessBoard();
				
				if (b.isChessPieceAtLoc(testing) && b.getChessPieceColorAtLoc(testing) != c){
					ChessPiece derp = b.getChessPieceAtLoc(testing);
					derp.removeSelfFromChessBoard();
					removeSelfFromChessBoard();
					putSelfInChessBoard(b, testing);
					//if your own player is in check
					if (!k.isInCheck()) {
						dumbLocs.add(testing);
						//moveLocs.remove(testing);
					}
					removeSelfFromChessBoard();
					putSelfInChessBoard(b, curr);
					derp.putSelfInChessBoard(b, testing);
				} else {
					removeSelfFromChessBoard();
					putSelfInChessBoard(b, testing);
					//if your own player is in check
					if (!k.isInCheck()) {
						dumbLocs.add(testing);
						//moveLocs.remove(testing);
					}
					removeSelfFromChessBoard();
					putSelfInChessBoard(b, curr);
				}
			}
			validLocs = dumbLocs;
		}
		
		return validLocs;
	}
	
	
	public void setHasMoved(boolean hasMoved){
		this.hasMoved = hasMoved;
	}
	public boolean hasMoved(){
		return hasMoved;
	}
	/**
	 * Moves this ChessPiece to a new location. If there is another ChessPiece at the
	 * given location, it is removed. <br />
	 * Precondition: (1) This ChessPiece is contained in a ChessBoard (2)
	 * <code>newLocation</code> is valid in the ChessBoard of this ChessPiece
	 * Postcondition: (1) If a piece was contained at loc, that piece was removed from the ChessBoard
	 * @param newLocation
	 *            the new location
	 * @return true if this Chess Piece moved, false if it did not
	 * @throws Exception if the location was not valid move location for the piece
	 */
	public ChessPiece moveTo(Location loc) throws IllegalMoveException, PieceNeedsToBeReplacedException{
		if(getValidMoveLocations().contains(loc)){
			ChessPiece toReturn = doMove(loc);
			hasMoved = true;
			return toReturn;
		}
		throw new IllegalMoveException("Not a valid move location");
	}
	protected ChessPiece doMove(Location newLocation) {
		if (chessboard == null)
			throw new IllegalStateException("This chesspiece is not in a chessboard.");
		if (chessboard.get(location) != this)
			throw new IllegalStateException(
					"The chessboard contains a different chesspiece at location "
							+ location + ".");
		if (!chessboard.isValid(newLocation))
			throw new IllegalArgumentException("Location " + newLocation
					+ " is not valid.");

		if (newLocation.equals(location))
			return null;
		chessboard.remove(location);
		ChessPiece other = chessboard.get(newLocation);
		if (other != null)
			other.removeSelfFromChessBoard();
		location = newLocation;
		chessboard.put(location, this);
		return other;
	}
	

	/**
	 * Gets the color of this ChessPiece.
	 * 
	 * @return the color of this ChessPiece
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color of this ChessPiece.
	 * 
	 * @param newColor
	 *            the new color
	 */
	public void setColor(Color newColor) {
		color = newColor;
	}

	/**
	 * Gets the ChessBoard in which this ChessPiece is located.
	 * 
	 * @return the ChessBoard of this ChessPiece, or <code>null</code> if this ChessPiece is
	 *         not contained in a ChessBoard
	 */
	public ChessBoard<ChessPiece> getChessBoard() {
		return chessboard;
	}

	/**
	 * Gets the location of this ChessPiece.
	 * 
	 * @return the location of this ChessPiece, or <code>null</code> if this ChessPiece is
	 *         not contained in a ChessBoard
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Puts this ChessPiece into a ChessBoard. If there is another ChessPiece at the given
	 * location, it is removed. <br />
	 * Precondition: (1) This ChessPiece is not contained in a ChessBoard (2)
	 * <code>loc</code> is valid in <code>br</code>
	 * 
	 * @param br
	 *            the ChessBoard into which this ChessPiece should be placed
	 * @param loc
	 *            the location into which the ChessPiece should be placed
	 */
	public void putSelfInChessBoard(ChessBoard<ChessPiece> br, Location loc) {
		if (chessboard != null)
			throw new IllegalStateException(
					"This chesspiece is already contained in a chessboard.");

		ChessPiece chesspiece = br.get(loc);
		if (chesspiece != null)
			chesspiece.removeSelfFromChessBoard();
		br.put(loc, this);
		chessboard = br;
		location = loc;
	}

	/**
	 * Removes this ChessPiece from its ChessBoard. <br />
	 * Precondition: This ChessPiece is contained in a ChessBoard
	 */
	public void removeSelfFromChessBoard() {
		if (chessboard == null)
			throw new IllegalStateException(
					"This chesspiece is not contained in a chessboard.");
		if (chessboard.get(location) != this)
			throw new IllegalStateException(
					"The chessboard contains a different chesspiece at location "
							+ location + ".");

		chessboard.remove(location);
		chessboard = null;
		location = null;
	}

	/**
	 * Creates a string that describes this ChessPiece.
	 * 
	 * @return a string with the location, direction, and color of this ChessPiece
	 */
	public String toString() {
		return getClass().getName() + "[location=" + location + ",color="
				+ color + "]";
	}

	public boolean isThisBad() {
		return thisIsBad;
	}
	public void thisIsBad(boolean i){
		thisIsBad = i;
	}
}
