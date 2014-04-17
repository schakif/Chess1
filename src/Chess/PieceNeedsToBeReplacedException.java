package Chess;

public class PieceNeedsToBeReplacedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ChessPiece piecePotentiallyDeleted;
	
	public PieceNeedsToBeReplacedException(String s, ChessPiece p){
		super(s);
		piecePotentiallyDeleted = p;
	}
	public ChessPiece getPiecePotentiallyDeleted() {
		return piecePotentiallyDeleted;
	}
	public void setPiecePotentiallyDeleted(ChessPiece piecePotentiallyDeleted) {
		this.piecePotentiallyDeleted = piecePotentiallyDeleted;
	}
	
}
