package tetrads;

import java.awt.Color;

public class RightSnake extends Tetrad{
	
	public RightSnake() {
		super(3, 0, createOrientation("RIGHT_SNAKE"));
	}

	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return new Color(0, 255, 0);
	}
	
}
