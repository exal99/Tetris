package main.tetrads;


public class TTurn extends Tetrad {
	
	public TTurn() {
		super(3, 0, "T_TURN");
	}
	
	@Override
	public Tetrads getType() {
		return Tetrads.T_TURN;
	}
}
