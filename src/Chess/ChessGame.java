package Chess;

import info.gridworld.grid.Location;

import java.awt.Color;
import java.util.ArrayList;

public class ChessGame {
	private ChessPlayer inactivePlayer; // the player who is waiting for their
										// turn
	private ChessPlayer activePlayer; // the player whose turn it is
	private ChessBoard<ChessPiece> gameBoard;

	/**
	 * Creates a chess game with filled board and with players PostCondition:
	 * (1) The game is ready to play
	 */
	public ChessGame(long playerClockTime) {
		gameBoard = ChessBoard.makeFullChessBoard();
		ArrayList<King> kings = gameBoard.getKingsOnChessBoard();
		ChessPlayer player1 = new ChessPlayer(kings.get(0).getColor(),
				kings.get(0), playerClockTime);
		ChessPlayer player2 = new ChessPlayer(kings.get(1).getColor(),
				kings.get(1), playerClockTime);
		if (player1.getColor().equals(Color.WHITE)) {
			activePlayer = player1;
			inactivePlayer = player2;
		} else {
			activePlayer = player2;
			inactivePlayer = player1;
		}
	}

	/**
	 * 
	 * @param loc
	 * @return
	 */
	public ArrayList<Location> getMoveLocationsAtLocation(Location loc) {
		return gameBoard.getMoveLocationsForLocation(loc);
	}

	/**
	 * 
	 * @return
	 */
	public Color getPieceColorAtLocation(Location loc) {
		return gameBoard.getChessPieceColorAtLoc(loc);
	}

	public Color getActivePlayerColor() {
		return activePlayer.getColor();
	}

	public Color getInactivePlayerColor() {
		return inactivePlayer.getColor();
	}

	/**
	 * Move a piece from one location to another location PostCondition: (1) the
	 * active player is now the inactive player and vice versa
	 * 
	 * @param from
	 *            Location that the piece is in before moving
	 * @param to
	 *            Location to move piece to
	 * @return return true if a piece was moved and turn ended successfully
	 */
	public boolean moveAndEndActivePlayerTurn(Location from, Location to)
			throws IllegalMoveException, PieceNeedsToBeReplacedException {
		/* this is pretty terrible, could use some cleanup */
		activePlayer.stopClock();// so time doesn't run as calculations happen
		ChessPiece movingPiece = gameBoard.getChessPieceAtLoc(from);
		ChessPiece potentialMovedPiece = gameBoard.getChessPieceAtLoc(to);
		if (movingPiece != null) {
			// no need to display, (get a response) if to wasn't a valid move
			// location
			// if its not the active players piece don't do anything
			if (!movingPiece.getColor().equals(activePlayer.getColor())) {
				return false;
			}
			try {
				movingPiece.moveTo(to);
				if (!endActivePlayerTurn()) {// illegal move was made
					// activePlayer.startClock(); clock already moving if turn
					// was not ended
					throw revertMove(movingPiece, potentialMovedPiece, from, to);
				}
			} catch (IllegalMoveException e) {// no move was made
				activePlayer.startClock();
				throw e;
			} catch (PieceNeedsToBeReplacedException e) {// move was made, but
															// more input
															// required
				if (!endActivePlayerTurn()) {// if move made was illegal
					// activePlayer.startClock(); clock already moving if turn
					// not ended
					throw revertMove(movingPiece, potentialMovedPiece, from, to);
				}
				// turn was ended but more input required
				throw e;
			}
			// turn was ended successfully, a piece was moved and the player
			// ended its turn
			return true;
		}
		// if moving piece was null, no move was made, so don't end turn or do
		// anything
		return false;
	}

	private IllegalMoveException revertMove(ChessPiece movingPiece,
			ChessPiece potentialMovedPiece, Location from, Location to)
			throws IllegalMoveException {

		// revert move
		movingPiece.doMove(from);
		if (potentialMovedPiece != null) {
			potentialMovedPiece.putSelfInChessBoard(gameBoard, to);
		}
		// now throw exception, display this
		IllegalMoveException e = new IllegalMoveException(
				"Player cannot end turn, in check");
		e.setDisplay(true);
		return e;
	}

	/**
	 * Ends active players turn (if its legal) PostCondition: (1) The active
	 * player is now the inactive player and vice versa
	 * 
	 * @return true if players turn was ended, false otherwise
	 */
	public boolean endActivePlayerTurn() throws IllegalMoveException {
		if (activePlayer.endTurn()) {
			ChessPlayer t = activePlayer;
			activePlayer = inactivePlayer;
			inactivePlayer = t;
			return true;
		}
		return false;
	}

	/**
	 * Use to start the players turn
	 * 
	 * @return returns the TURNSTATUS of the player whose turn starts
	 */
	public TURNSTATUS startActivePlayerTurn() {
		return activePlayer.startTurn();
	}

	/**
	 * Stops the Active players clock
	 * 
	 * @return true if clock was stopped, false otherwise
	 */
	public void stopActivePlayerClock() {
		activePlayer.stopClock();
	}

	/**
	 * Starts the Active players clock
	 * 
	 * @return true if clock was started, false otherwise
	 */
	public void startActivePlayerClock() {
		activePlayer.startClock();
	}

	/**
	 * This method returns the time left in milliseconds on the ChessClock of
	 * the Active Player
	 * 
	 * @return The time remaining in milliseconds
	 */
	public long getActivePlayerTimeLeft() {
		return activePlayer.getTimeLeftInMilliSeconds();
	}

	public ChessBoard<ChessPiece> getChessBoard() {
		return gameBoard;
	}

	/**
	 * Use this to find if a piece is at a loc
	 * 
	 * @param loc
	 *            location to check
	 * @return true if a piece was at loc, false otherwise
	 */
	public boolean isPieceAtLoc(Location loc) {
		if (gameBoard.getChessPieceAtLoc(loc) != null) {
			return true;
		}
		return false;
	}

	public boolean endGame(Color winningColor) {
		return false;
	}

	/**
	 * Adds piece to board
	 * 
	 * @param loc
	 *            the loc to place the piece
	 * @param pieceNum
	 *            the piece id (0=Queen,1=Rook,2=Bishop,3=Knight
	 * @param pieceOwnerColor
	 *            the color of the player whose piece it will become
	 * @return
	 */
	public boolean addPieceAtLoc(Location loc, int pieceNum,
			Color pieceOwnerColor) {
		if (pieceNum == 0) {
			new Queen(gameBoard, pieceOwnerColor, loc);
		}
		if (pieceNum == 1) {
			new Rook(gameBoard, pieceOwnerColor, loc);
		}
		if (pieceNum == 2) {
			new Bishop(gameBoard, pieceOwnerColor, loc);
		}
		if (pieceNum == 3) {
			new Knight(gameBoard, pieceOwnerColor, loc);
		}
		return true;
	}

}
