package gui;

import java.awt.Graphics;
import java.awt.Label;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import gameBoard.GameBoard;
import tetrads.Tetrad;

public class InfoGUI extends JPanel{
	private static final long serialVersionUID = 1L;
	private GameBoard board;
	private QueueDisplayer queue;
	private Label level;
	private Label score;
	
	public InfoGUI(GameBoard b, FieldGUI size, int xPadding, int yPadding) {
		super();
		board = b;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		queue = new QueueDisplayer(board.getQueue(), size, xPadding, yPadding);
		add(queue);
		level = new Label("Level: " + board.getLevel());
		add(level);
		score = new Label("Score: " + board.getScore());
		add(score);
	}
	
	public void updateLevel() {
		level.setText("Level: " + board.getLevel());
	}
	
	public void updateScore() {
		score.setText("Score: " + board.getScore());
	}
	
	public void updateQueue() {
		queue.changeQueue(board.getQueue());
	}
	
	@Override
	public void update(Graphics g) {
		updateLevel();
		updateScore();
		updateQueue();
		super.update(g);
	}
	
	@Override
	public void paint(Graphics g) {
		updateLevel();
		updateScore();
		updateQueue();
		queue.paint(g);
		level.paint(g);
		score.paint(g);
	}
	
	private class QueueDisplayer extends JPanel {

		private static final long serialVersionUID = 1L;
		
		private Tetrad queue;
		private FieldGUI size;
		private int xPadding;
		private int yPadding;
		
		public QueueDisplayer(Tetrad queued, FieldGUI size, int xPadding, int yPadding) {
			super();
			queue = queued;
			this.size = size;
			this.xPadding = xPadding;
			this.yPadding = yPadding;
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			int size = this.size.getSquareSize();
			g.setColor(queue.getType().getColor());
			boolean[][] orien = queue.getOrientation();
			int startX =(int) (0.5 * (getWidth() - (orien[0].length * (size + xPadding))) / 2);
			int startY = (getHeight() - (orien.length * (size + yPadding))) / 2;
			for (int x = 0; x < orien[0].length; x++) {
				for (int y = 0; y < orien.length; y++) {
					if (orien[y][x]) {
						g.fillRect(startX + (size + xPadding) * x,
								   startY + (size + yPadding) * y, size, size);
					}
				}
			}
			
		}
		
		public void changeQueue(Tetrad newQueue) {
			queue = newQueue;
			repaint();
		}
		
	}
}
