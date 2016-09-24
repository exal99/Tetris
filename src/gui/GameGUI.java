package gui;

import java.awt.Dimension;
import java.util.Timer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import gameBoard.GameBoard;
import keyEvents.PlayerOneKeyListener;
import tetrads.Tetrad;

public class GameGUI extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private FieldGUI field;
	private InfoGUI info;
	private Tetrad lastQueue;
	private Tetrad lastHold;
	private GameBoard game;
	
	public GameGUI(GameBoard g, Timer t, int width, int height, boolean inverted, boolean multiplayer) {
		init(g, t, (multiplayer) ? width : (int) Math.round(width * .75), height);
		int sqareSize = height/(g.getField().length - 2);
		int padding = (multiplayer) ? (int) (width - (sqareSize * 10) - info.getPreferredSize().getWidth()) : 10;
		if (inverted) {
			add(field);
			add(Box.createRigidArea(new Dimension(padding, 0)));
			add(info);
		} else {
			add(info);
			add(Box.createRigidArea(new Dimension(padding, 0)));
			add(field);
		}
	}
	
	private void init(GameBoard g, Timer t, int width, int height) {
		game = g;
		
		field = new FieldGUI(g);
		info = new InfoGUI(g, field, field.getXPadding(), field.getYPadding());

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		info.setPreferredSize(new Dimension((int) Math.round(width * 0.4), height));
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
	
	public void setAppend(String append) {
		info.setAppend(append);
	}
	

}
