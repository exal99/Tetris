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
		g.drawRect(0, 0, field[0].length * squareSize + xPadding, getHeight());
		for (int row = 2; row < field.length; row++) {
			for (int col = 0; col < field[0].length; col++) {
				if (field[row][col] != null) {
					g.setColor(field[row][col].getColor());
					g.fillRect(col * (squareSize) + xPadding,
							  (row - 2) * (squareSize),
							  squareSize - xPadding, squareSize - yPadding);
				}
			}
		}
		Tetrad controlling = board.getControlling();
		paintTetrad(g, controlling, true);
		
		Tetrad ghost = board.getPlacement();
		paintTetrad(g, ghost, false);
	}
	
	private void paintTetrad(Graphics g, Tetrad t, boolean fill) {
		boolean[][] orien = t.getOrientation();
		int xPos = t.getXPos();
		int yPos = t.getYPos();
		int squareSize = getSquareSize();
		g.setColor(t.getType().getColor());
		for (int x = xPos; x < xPos + orien[0].length; x++) {
			for (int y = yPos; y < yPos + orien.length; y++) {
				if (orien[y - yPos][x - xPos]) {
					if (fill) {
						g.fillRect(x * (squareSize) + X_PADDING,
								  (y - 2) * (squareSize),
								  squareSize - X_PADDING, squareSize - Y_PADDING);
					} else {
						g.drawRect(x * (squareSize) + X_PADDING,
								  (y - 2) * (squareSize),
								  squareSize - X_PADDING, squareSize - Y_PADDING);
					}
				}
			}
		}
	}
}
