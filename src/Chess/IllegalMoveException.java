package Chess;

public class IllegalMoveException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean display;
	
	public IllegalMoveException(String s){
		super(s);
		this.display = false;
	}
	
	public boolean shouldDisplay(){
		return display;
	}
	
	public void setDisplay(boolean display){
		this.display = display;
	}
}
