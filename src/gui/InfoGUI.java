package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gameBoard.GameBoard;
import tetrads.Tetrad;

public class InfoGUI extends JPanel{
	private static final long serialVersionUID = 1L;
	private GameBoard board;
	private TetradDisplayer queue;
	private TetradDisplayer hold;
	private JLabel text;
	private String append;
	
	public InfoGUI(GameBoard b, FieldGUI size, int xPadding, int yPadding) {
		super();
		
		board = b;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel next = new JLabel("Next:");
		next.setFont(new Font(next.getFont().getName(), Font.PLAIN, 20));
		add(next);
		queue = new TetradDisplayer(board.getQueue(), size, xPadding, yPadding);
		queue.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		add(queue);
		text = new JLabel("<html>Level: " + board.getLevel() + "<br>Score: " + board.getScore() + "<br></html>");
		add(text);
		JLabel holdLabel = new JLabel("<html>Hold:</html>");
		holdLabel.setFont(new Font(holdLabel.getFont().getName(), Font.PLAIN, 20));
		add(holdLabel);
		hold = new TetradDisplayer(board.getHolding(), size, xPadding, yPadding);
		hold.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		add(hold);
		append = "";
		
		
	}
	
	public void updateText() {
		text.setText("<html>Level: " + board.getLevel() + "<br>Score: " + board.getScore() + "<br>" + append + "</html>");
	}
	
	public void setAppend(String newAppend) {
		append = newAppend;
	}
	
	public void updateQueue() {
		queue.changeDisplayed(board.getQueue());
	}
	
	public void updateHold() {
		hold.changeDisplayed(board.getHolding());
	}
	
	@Override
	public void paint(Graphics g) {
		updateText();
		updateQueue();
		updateHold();
		super.paint(g);
	}
	
	private class TetradDisplayer extends JPanel {

		private static final long serialVersionUID = 1L;
		
		private Tetrad toDisplay;
		private FieldGUI size;
		private int xPadding;
		private int yPadding;
		
		public TetradDisplayer(Tetrad toDisplay, FieldGUI size, int xPadding, int yPadding) {
			super();
			this.toDisplay = toDisplay;
			this.size = size;
			this.xPadding = xPadding;
			this.yPadding = yPadding;
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if (toDisplay != null) {
				int size = this.size.getSquareSize();
				
				boolean[][] orien = toDisplay.getOrientation();
				int startX =(int) (0.5 * (getWidth() - (orien[0].length * (size + xPadding))) / 2);
				int startY = (getHeight() - (orien.length * (size + yPadding))) / 2;
				g.setColor(toDisplay.getType().getColor());
				for (int x = 0; x < orien[0].length; x++) {
					for (int y = 0; y < orien.length; y++) {
						if (orien[y][x]) {
							g.fillRect(startX + (size + xPadding) * x,
									   startY + (size + yPadding) * y, size, size);
						}
					}
				}
			}
		}
		
		public void changeDisplayed(Tetrad newQueue) {
			toDisplay = newQueue;
			repaint();
		}
		
	}
}
