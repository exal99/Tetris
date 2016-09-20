package gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import gameBoard.GameBoard;
import keyEvents.UserInput;
import tetrads.Tetrad;

public class GameGUI{
	
	private FieldGUI field;
	private InfoGUI info;
	private JFrame root;
	private Tetrad lastQueue;
	private Tetrad lastHold;
	private GameBoard game;
	
	public GameGUI(GameBoard g) {
		game = g;
		root = new JFrame("Tetris");
		
		field = new FieldGUI(g);
		info = new InfoGUI(g, field, field.getXPadding(), field.getYPadding());
		
		root.setLayout(new BoxLayout(root.getContentPane(), BoxLayout.X_AXIS));;
		root.add(info);
		root.add(field);
		lastQueue = g.getQueue();
		lastHold = g.getHolding();
		
		root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UserInput keyListener = new UserInput(g);
		root.addKeyListener(keyListener);
	}
	
	public void update() {
		if (lastQueue != game.getQueue() || lastHold != game.getHolding()) {
			info.repaint();
			lastQueue = game.getQueue();
			lastHold = game.getHolding();
		}
		field.repaint();
	}
	
	public JFrame getRoot() {
		return root;
	}
	

}
