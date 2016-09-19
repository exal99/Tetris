package gui;

import java.awt.Component;
import java.awt.Frame;
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
	
	public InfoGUI(GameBoard b, int size, int xPadding, int yPadding) {
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
		queue.paint(g);
		level.paint(g);
		score.paint(g);
	}
	
	private class QueueDisplayer extends Component {

		private static final long serialVersionUID = 1L;
		
		private Tetrad queue;
		private int size;
		private int xPadding;
		private int yPadding;
		
		public QueueDisplayer(Tetrad queued, int size, int xPadding, int yPadding) {
			super();
			queue = queued;
			this.size = size;
			this.setSize((size + xPadding) * 4, (size + yPadding) * 4);
			this.xPadding = xPadding;
			this.yPadding = yPadding;
		}
		
		@Override
		public void paint(Graphics g) {
			g.setColor(queue.getType().getColor());
			for (int x = 0; x < 4; x++) {
				for (int y = 0; y < 4; y++) {
					g.fillRect((size + xPadding) * x,
							   (size + yPadding) * y, size, size);
				}
			}
		}
		
		public void changeQueue(Tetrad newQueue) {
			queue = newQueue;
			repaint();
		}
		
	}
}
