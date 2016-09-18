package tetrads;

import java.awt.Color;

public class TTurn extends Tetrad {
	
	public TTurn() {
		super(3, 0, createOrientation("T_TURN"));
	}

	@Override
	public Color getColor() {
		return new Color(255, 0, 255);
	}

}
