package gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import gameBoard.GameBoard;
import keyEvents.UserInput;

public class GameGUI{
	
	private FieldGUI field;
	private InfoGUI info;
	private JFrame root;
	
	public GameGUI(GameBoard g) {
		root = new JFrame("Tetris");
		
		field = new FieldGUI(g);
		info = new InfoGUI(g, field, field.getXPadding(), field.getYPadding());
		
		root.setLayout(new BoxLayout(root.getContentPane(), BoxLayout.X_AXIS));;
		root.add(info);
		root.add(field);
		
		root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UserInput keyListener = new UserInput(g);
		root.addKeyListener(keyListener);
	}
	
	public void update() {
		info.repaint();
		field.repaint();
		root.repaint();
	}
	
	public JFrame getRoot() {
		return root;
	}
	

}
