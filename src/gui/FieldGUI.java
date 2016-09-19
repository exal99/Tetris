package gui;

import java.awt.Component;
import java.awt.Graphics;
import gameBoard.GameBoard;
import tetrads.Tetrad;
import tetrads.Tetrads;

public class FieldGUI extends Component{
	private static final long serialVersionUID = 1L;
	private GameBoard board;
	private static final int X_PADDING = 2;
	private static final int Y_PADDING = 2;
	
	public FieldGUI(GameBoard b) {
		super();
		board = b;
	}
	
	public int getSquareSize() {
		return getHeight() / (board.getField().length - 2);
	}
	
	public int getXPadding() {
		return X_PADDING;
	}
	
	public int getYPadding() {
		return Y_PADDING;
	}
	
	@Override
	public void paint(Graphics g) {
		Tetrads[][] field = board.getField();
		int squareSize = getSquareSize();
		int xPadding = X_PADDING;
		int yPadding = Y_PADDING;
		g.drawRect(0, 0, field[0].length * squareSize, getHeight());
		for (int row = 2; row < field.length; row++) {
			for (int col = 0; col < field[0].length; col++) {
				if (field[row][col] != null) {
					g.setColor(field[row][col].getColor());
					g.fillRect(col * (squareSize),
							  (row - 2) * (squareSize),
							  squareSize - xPadding, squareSize - yPadding);
				}
			}
		}
		Tetrad controlling = board.getControlling();
		g.setColor(controlling.getType().getColor());
		int xPos = controlling.getXPos();
		int yPos = controlling.getYPos();
		boolean[][] orien = controlling.getOrientation();
		for (int x = xPos; x < xPos + orien[0].length; x++) {
			for (int y = yPos; y < yPos + orien.length; y++) {
				if (orien[y - yPos][x - xPos]) {
					g.fillRect(x * (squareSize),
							   (y - 2) * (squareSize),
							   squareSize - xPadding, squareSize - yPadding);
				}
			}
		}
	}
}
