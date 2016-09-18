package tetrads;

import java.awt.Color;

public class Alpha extends Tetrad {
	
	public Alpha() {
		super(3, 0, createOrientation("ALPHA"));
	}

	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return new Color(255, 165, 0);
	}

}
