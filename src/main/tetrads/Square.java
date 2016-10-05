package main.tetrads;


public class Square extends Tetrad {

	public Square() {
		super(3, 0, "SQUARE");
	}

	@Override
	public Tetrads getType(){
		return Tetrads.SQUARE;
	}
	
	@Override
	public void rotateLeft() {
	}
	
	@Override
	public void rotateRight() {
	}

}
