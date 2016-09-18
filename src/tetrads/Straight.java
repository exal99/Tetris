package tetrads;

import java.awt.Color;

public class Straight extends Tetrad {

	public Straight() {
		super(3, 0, createOrientation("STRAIGHT"));
	}
	
	public Color getColor() {
		return new Color(0,255,255);
	}

}
