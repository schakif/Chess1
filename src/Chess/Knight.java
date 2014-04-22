package Chess;

import info.gridworld.grid.Location;

import java.awt.Color;
import java.util.ArrayList;

public class Knight extends ChessPiece {
	public Knight(ChessBoard<ChessPiece> board, Color color, Location loc){
		super.setColor(color);
		super.setHasMoved(false);
		super.putSelfInChessBoard(board,loc);
	}

	@Override
	public ArrayList<Location> getValidMoveLocations() {
		ArrayList<Location> toReturn = new ArrayList<Location>(8);
		Location myLoc = super.getLocation();
		//1
		Location locToCheck = new Location(myLoc.getRow()-2,myLoc.getCol()-1);
		toReturn.addAll(checkLoc(locToCheck));
		//2
		locToCheck = new Location(myLoc.getRow()-2,myLoc.getCol()+1);
		toReturn.addAll(checkLoc(locToCheck));
		//3
		locToCheck = new Location(myLoc.getRow()-1,myLoc.getCol()+2);
		toReturn.addAll(checkLoc(locToCheck));
		//4
		locToCheck = new Location(myLoc.getRow()+1,myLoc.getCol()+2);
		toReturn.addAll(checkLoc(locToCheck));
		//5
		locToCheck = new Location(myLoc.getRow()+2,myLoc.getCol()+1);
		toReturn.addAll(checkLoc(locToCheck));
		//6
		locToCheck = new Location(myLoc.getRow()+2,myLoc.getCol()-1);
		toReturn.addAll(checkLoc(locToCheck));
		//7
		locToCheck = new Location(myLoc.getRow()+1,myLoc.getCol()-2);
		toReturn.addAll(checkLoc(locToCheck));
		//8
		locToCheck = new Location(myLoc.getRow()-1,myLoc.getCol()-2);
		toReturn.addAll(checkLoc(locToCheck));
		
		if(super.isThisBad()){
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
				toReturn = super.checkLocations(toReturn);
			}
 		}
		
		return toReturn;
	}
	
	private ArrayList<Location> checkLoc(Location loc){
		ArrayList<Location> toReturn = new ArrayList<Location>(1);
		if(!super.getChessBoard().isValid(loc)){
			return toReturn;
		}
		ChessPiece pieceAtLoc = super.getChessBoard().getChessPieceAtLoc(loc);
		if(pieceAtLoc != null){
			if(!pieceAtLoc.getColor().equals(super.getColor())){
				toReturn.add(loc);
				return toReturn;
			}
			return toReturn;
		}else{
			toReturn.add(loc);
			return toReturn;
		}
	}
}
