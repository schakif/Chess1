package Chess;

import java.awt.Color;

public class ChessPlayer{
	private ChessClock clock;
	private King king;
	private Color color;
	
	public ChessPlayer(Color color, King king, long clockTime){
		if(king.getColor() != color){
			throw new RuntimeException("The king is not the same color as the player");
		}
		this.color = color;
		this.king = king;
		this.clock = new ChessClock(clockTime);
	}
	/**
	 * 
	 * @return true if is in check, false if not
	 */
	public boolean isInCheck() {
		return king.isInCheck();
	}

	/**
	 * 
	 * @return true if is in checkmate, false if not
	 */
	public boolean isInCheckMate() {
		
		//this is really hard
		//just going to let players concede
		return false;
	}

	/**
	 * Returns the players status at the start of a turn
	 * 
	 * @return CHECK,CHECKMATE,or NOSTATUS
	 */
	public TURNSTATUS startTurn() {
		if(isInCheck()){
			clock.start();
			return TURNSTATUS.CHECK;
		}
		clock.start();
		return TURNSTATUS.NOSTATUS;
	}

	/**
	 * Make sure conditions for ending turn are satisfied (not in check)
	 * Then Stop the chess clock
	 * @return true if turn was stopped, false if not (ie, player was in check)
	 */
	public boolean endTurn(){
		clock.stop();
		if(isInCheck()){
			clock.start();
			return false;
			//the move that ended the turn should be undone 
			//- (game class will handle it, but player probably really should
		}
		return true;
	}

	/**
	 * Returns the time left on a players clock in milliSeconds
	 * 
	 * @return long representing seconds
	 */
	public long getTimeLeftInMilliSeconds() {
		return clock.getTimeLeft();
	}

	/**
	 * 
	 * @return
	 */
	public ChessClock getChessClock() {
		return clock;
	}	
	/**
	 * 
	 * @return
	 */
	public Color getColor() {
		return color;
	}
	/**
	 * 
	 * @param myColor
	 */
	public void setColor(Color myColor) {
		this.color = myColor;
	}
	public void startClock(){
		clock.start();
	}
	public void stopClock(){
		clock.stop();
	}
}
