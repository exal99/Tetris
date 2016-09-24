package gui;

import java.awt.Dimension;
import java.util.Timer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import gameBoard.GameBoard;
import tetrads.Tetrad;

public class GameGUI extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private FieldGUI field;
	private InfoGUI info;
	private Tetrad lastQueue;
	private Tetrad lastHold;
	private GameBoard game;
	
	public GameGUI(GameBoard g, Timer t) {
		init(g, t);
		add(info);
		add(field);

	}
	
	public GameGUI(GameBoard g, Timer t, boolean inverted) {
		init(g, t);
		if (inverted) {
			add(field);
			add(info);
		} else {
			add(info);
			add(field);
		}
	}
	
	private void init(GameBoard g, Timer t) {
		game = g;
		
		field = new FieldGUI(g);
		info = new InfoGUI(g, field, field.getXPadding(), field.getYPadding());

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		info.setPreferredSize(new Dimension((int) Math.round(800 * 0.3), 600));
		info.setMaximumSize(info.getPreferredSize());
		
		lastQueue = g.getQueue();
		lastHold = g.getHolding();
	}
	
	public void update() {
		if (lastQueue != game.getQueue() || lastHold != game.getHolding()) {
			info.repaint();
			lastQueue = game.getQueue();
			lastHold = game.getHolding();
		}
		field.repaint();
	}
	
//	public JFrame getRoot() {
//		return root;
//	}
	
	public void setAppend(String append) {
		info.setAppend(append);
	}
	

}
