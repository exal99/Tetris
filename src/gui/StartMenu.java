package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import game.MainSinglePlayerThread;
import gameBoard.GameBoard;
import highscore.HighScore;
import keyEvents.PlayerOneKeyListener;

public class StartMenu extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int FONT_SIZE = 30;
	private static final int PADDING = 40;
	
	private GameBoard game;
	private JFrame root;
	private String[] args;
	private MainSinglePlayerThread thread;
	private HighScore score;
	
	public StartMenu(GameBoard game, JFrame root, HighScore hscore, String[] args) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JButton single = new JButton("Play Single Player");
		JButton multi = new JButton("Play Two Players");
		JButton score = new JButton("High Scores");
		JButton quit = new JButton("Quit");
		
		
		single.setAlignmentX(Component.CENTER_ALIGNMENT);
		multi.setAlignmentX(Component.CENTER_ALIGNMENT);
		score.setAlignmentX(Component.CENTER_ALIGNMENT);
		quit.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		single.setFont(new Font(single.getFont().getName(), Font.PLAIN, FONT_SIZE));
		multi.setFont(new Font(multi.getFont().getName(), Font.PLAIN, FONT_SIZE));
		score.setFont(new Font(score.getFont().getName(), Font.PLAIN, FONT_SIZE));
		quit.setFont(new Font(quit.getFont().getName(), Font.PLAIN, FONT_SIZE));
		
		single.addActionListener(e -> singleButtonClick());
		multi.addActionListener(e -> multiButtonClick());
		score.addActionListener(e -> scoreButtonClick());
		quit.addActionListener(e -> quitButtonClick());
		
		add(Box.createVerticalGlue());
		add(single);
		add(Box.createRigidArea(new Dimension(0, PADDING)));
		add(multi);
		add(Box.createRigidArea(new Dimension(0, PADDING)));
		add(score);
		add(Box.createRigidArea(new Dimension(0, PADDING)));
		add(quit);
		add(Box.createVerticalGlue());
		
		this.game = game;
		this.args = args;
		this.root = root;
		this.score = hscore;
		
		root.addKeyListener(new PlayerOneKeyListener(game, game.getTimer()));
	}
	
	private void singleButtonClick() {
		game.reset();
		root.remove(this);
		root.setSize(800, 600);
		GameGUI gui = new GameGUI(game, game.getTimer(), 800, 600, false, false);
		root.add(gui);
		thread = new MainSinglePlayerThread(gui, game, game.getTimer(), root, score, args);
		thread.start();
		game.start();
		root.revalidate();
		
	}
	
	private void multiButtonClick() {
		System.out.println("not implemented yet");
	}
	
	private void scoreButtonClick() {
		StringBuilder sb = new StringBuilder("Scores:\n----\n");
		for (Entry<String, Long> entry : score.getHighScorer()) {
			sb.append(entry.getKey() + ": " + entry.getValue() + "\n");
		}
		JOptionPane.showMessageDialog(root, sb.toString());
	}
	
	private void quitButtonClick() {
		try {
			if (thread != null) {
				thread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		root.dispatchEvent(new WindowEvent(root, WindowEvent.WINDOW_CLOSING));
	}
}
