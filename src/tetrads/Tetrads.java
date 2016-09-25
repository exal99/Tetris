package tetrads;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.ini4j.Wini;

public enum Tetrads {
	ALPHA (255, 165, 0, "alpha"),
	GAMMA (0, 0, 255, "gamma"),
	LEFT_SNAKE (255, 0, 0, "left_snake"),
	RIGHT_SNAKE (0, 255, 0, "right_snake"),
	SQUARE (255, 255, 0, "square"),
	STRAIGHT (0, 255, 255, "straight"),
	T_TURN (255, 0, 255, "t_turn"),
	TRASH (105, 105, 105, "trash");
	
	
	private Color color;
	Tetrads(int r, int g, int b, String name) {
		try {
			Wini config = new Wini(new File("config.ini"));
			String value = config.get("tetrominoes_color", name);
			if (value != null) {
				String[] values = value.split(", ");
				color = new Color(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
			} else {
				color = new Color(r, g, b);
			}
		} catch (IOException e) {
			color = new Color(r, g, b);
		}
	}
	
	public Color getColor() {
		return color;
	}
}
