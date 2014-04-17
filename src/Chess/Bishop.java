package Chess;

import info.gridworld.grid.Location;

import java.awt.Color;
import java.util.ArrayList;

public class Bishop extends ChessPiece {
	public Bishop(ChessBoard<ChessPiece> board, Color color, Location loc){
		super.setColor(color);
		super.setHasMoved(false);
		super.putSelfInChessBoard(board,loc);
	}
	
	public ArrayList<Location> getValidMoveLocations() {
		//moves diagonally
		ArrayList<Location> validLocs = new ArrayList<Location>();
		//check in 
		validLocs.addAll(checkValidLocInDirection(Location.NORTHEAST));
		validLocs.addAll(checkValidLocInDirection(Location.SOUTHEAST));
		validLocs.addAll(checkValidLocInDirection(Location.SOUTHWEST));
		validLocs.addAll(checkValidLocInDirection(Location.NORTHWEST));
		
		
		return validLocs;
	}
	
	private ArrayList<Location> checkValidLocInDirection(int direction){
		ArrayList<Location> validLocs = new ArrayList<Location>();
		ChessBoard<ChessPiece> myBoard = super.getChessBoard();
		Location myLoc = super.getLocation();
		Location nextLoc = myLoc.getAdjacentLocation(direction);
		//while still on a valid location on a board loop in the given direction
		while(myBoard != null && myBoard.isValid(nextLoc)){
			ChessPiece pieceAtLoc = (ChessPiece) myBoard.get(nextLoc);
			//if no piece, this can move there
			if(pieceAtLoc == null){
				validLocs.add(nextLoc);
			// if piece stop
			}else{
				//if enemy piece that is a valid location
				if(!(pieceAtLoc.getColor()).equals(super.getColor())){
					validLocs.add(nextLoc);
				}
				//this cannot move further in the given direction
				break;
			}
			//move on to next location in the given direction
			nextLoc = nextLoc.getAdjacentLocation(direction);
		}
		return validLocs;
	}
}
