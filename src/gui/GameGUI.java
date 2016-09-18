package gui;

import java.awt.Frame;
import java.awt.Graphics;

import javax.swing.BoxLayout;

import gameBoard.GameBoard;

public class GameGUI extends Frame{
	
	private FieldGUI field;
	private InfoGUI info;

	private static final long serialVersionUID = 1L;
	
	public GameGUI(GameBoard g) {
		super();
		field = new FieldGUI(g);
		info = new InfoGUI(g, field.getSquareSize(), field.getXPadding(), field.getYPadding());
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(info);
		add(field);
	}
	
	@Override
	public void update(Graphics g) {
		info.update(g);
		field.update(g);
		super.update(g);
	}
	
	@Override
	public void paint(Graphics g) {
		info.paint(g);
		field.paint(g);
	}
	

}
