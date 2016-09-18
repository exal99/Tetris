package tetrads;

import java.awt.Color;

public class Square extends Tetrad {

	public Square() {
		super(3, 0, createOrientation("SQUARE"));
	}

	@Override
	public Color getColor() {
		return new Color(255, 255, 0);
	}
	
	@Override
	public void rotateLeft() {
	}
	
	@Override
	public void rotateRight() {
	}

}
