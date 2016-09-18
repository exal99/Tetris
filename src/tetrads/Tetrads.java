package tetrads;

import java.awt.Color;

public enum Tetrads {
	ALPHA (255, 165, 0),
	GAMMA (0, 0, 255),
	LEFT_SNAKE (255, 0, 0),
	RIGHT_SNAKE (0, 255, 0),
	SQUARE (255, 255, 0),
	STRAIGHT (0, 255, 255),
	T_TURN (255, 0, 255);
	
	
	private Color color;
	Tetrads(int r, int g, int b) {
		color = new Color(r, g, b);
	}
	
	public Color getColor() {
		return color;
	}
}
