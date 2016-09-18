package tetrads;

import java.awt.Color;

public class Gamma extends Tetrad {
	
	public Gamma() {
		super(3, 0, createOrientation("GAMMA"));
	}

	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return new Color(0,0,255);
	}

}
