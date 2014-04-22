package Chess;

import java.awt.Color;
import java.util.ArrayList;

import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Location;

public class ChessBoard<E extends ChessPiece> extends BoundedGrid<E> {
	/**
	 * Makes a chess board. The board will have null in every space
	 * 
	 * @return an initialized chess board
	 */
	public static ChessBoard<ChessPiece> makeEmptyChessBoard() {
		return new ChessBoard<ChessPiece>(8, 8);
	}

	/**
	 * Makes a chess board. The board will have all pieces placed on it for a
	 * game of chess. The board will contain appropriate black and white tiles
	 * 
	 * @return an initialized chess board will all pieces
	 */
	public static ChessBoard<ChessPiece> makeFullChessBoard() {
		ChessBoard<ChessPiece> board = new ChessBoard<ChessPiece>(8, 8);
		// set up each color
		Color sideColor = Color.WHITE;
		placeLowerSide(board, sideColor);
		sideColor = Color.BLACK;
		placeUpperSide(board, sideColor);
		return board;
	}

	private static void placeUpperSide(ChessBoard<ChessPiece> self,
			Color sideColor) {
		// set up pawns
		for (int i = 0; i < 8; i++) {
			new Pawn(self, sideColor, new Location(1, i));
		}
		// set up Rooks
		new Rook(self, sideColor, new Location(0, 0));
		new Rook(self, sideColor, new Location(0, 7));
		// set up knights
		new Knight(self, sideColor, new Location(0, 1));
		new Knight(self, sideColor, new Location(0, 6));
		// set up bishops
		new Bishop(self, sideColor, new Location(0, 2));
		new Bishop(self, sideColor, new Location(0, 5));
		// set up queen
		new Queen(self, sideColor, new Location(0, 3));
		// set up king
		new King(self, sideColor, new Location(0, 4));
	}

	private static void placeLowerSide(ChessBoard<ChessPiece> self,
			Color sideColor) {
		// set up pawns
		for (int i = 0; i < 8; i++) {
			new Pawn(self, sideColor, new Location(6, i));
		}
		// set up Rooks
		new Rook(self, sideColor, new Location(7, 0));
		new Rook(self, sideColor, new Location(7, 7));
		// set up knights
		new Knight(self, sideColor, new Location(7, 1));
		new Knight(self, sideColor, new Location(7, 6));
		// set up bishops
		new Bishop(self, sideColor, new Location(7, 2));
		new Bishop(self, sideColor, new Location(7, 5));
		// set up queen
		new Queen(self, sideColor, new Location(7, 3));
		// set up king
		new King(self, sideColor, new Location(7, 4));
	}

	private ChessBoard(int rows, int cols) {
		super(rows, cols);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Returns an array of possible move locations for a piece at a location
	 * 
	 * @param loc
	 *            the Location to check
	 * @return null if no piece is there, an arrayList of possible move
	 *         Locations if a piece is there
	 */
	public ArrayList<Location> getMoveLocationsForLocation(Location loc) {
		ChessPiece cCP = getChessPieceAtLoc(loc);
		if (cCP != null) {
			cCP.thisIsBad(true);
			ArrayList<Location> temp =getChessPieceAtLoc(loc).getValidMoveLocations(); 
			cCP.thisIsBad(false);
			return temp;
		}
		return null;
	}

	/**
	 * Use to check if there is at a given location
	 * 
	 * @param loc
	 *            the Location to check
	 * @return true if there is a piece, false if there is not
	 */
	public boolean isChessPieceAtLoc(Location loc) {
		return (super.get(loc) != null);
	}

	/**
	 * 
	 * @param loc
	 * @return
	 */
	public ChessPiece getChessPieceAtLoc(Location loc) {
		return super.get(loc);
	}

	/**
	 * Use to get the color of a piece at the target location
	 * 
	 * @param loc
	 *            the location to find the piece at
	 * @return null if no ChessPiece at that location, the color of the piece otherwise
	 */
	public Color getChessPieceColorAtLoc(Location loc) {
		if(isChessPieceAtLoc(loc)){
			return super.get(loc).getColor();
		}
		return null;
	}
	/**
	 * Returns all of the kings on the board.
	 * @return
	 */
	public ArrayList<King> getKingsOnChessBoard(){
		//probably terrible way of doing this
		ArrayList<Location> allPieces = super.getOccupiedLocations();
		ArrayList<King> allKings = new ArrayList<King>();
		for(Location curLoc: allPieces){
			ChessPiece curPiece = getChessPieceAtLoc(curLoc);
			if(curPiece instanceof King){
				allKings.add( (King) curPiece);
			}
		}
		return allKings;
	}
}
