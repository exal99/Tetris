package tetrads;

import java.awt.Color;

public class LeftSnake extends Tetrad {
	
	public LeftSnake() {
		super(3, 0, createOrientation("LEFT_SNAKE"));
	}

	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return new Color(255, 0, 0);
	}

}
