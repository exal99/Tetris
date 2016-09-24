package gui;

import java.awt.Dimension;
import java.util.Timer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import gameBoard.GameBoard;

public class TwoPlayerGameGUI extends JPanel {

	private static final long serialVersionUID = 1L;
	private GameGUI player1;
	private GameGUI player2;
	
	public TwoPlayerGameGUI(GameBoard pl1, GameBoard pl2, Timer t, int width, int height) {
		player1 = new GameGUI(pl1, t, (int) Math.round(width/2D), height, false, true);
		player2 = new GameGUI(pl2, t, (int) Math.round(width/2D), height, true, true);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(player1);
		add(Box.createRigidArea(new Dimension(10, 0)));
		add(player2);
	}
	
	public void setPlayer1Append(String text) {
		player1.setAppend(text);
	}
	
	public void setPlayer2Append(String text) {
		player2.setAppend(text);
	}
	
	
	public static void main(String[] args) {
		Timer t = new Timer();
		JFrame frame = new JFrame("Test");
		TwoPlayerGameGUI gui = new TwoPlayerGameGUI(new GameBoard(t), new GameBoard(t), t, 1075, 600);
		frame.add(gui);
		frame.setSize(1075, 600);
		frame.setVisible(true);
	}
	
}
