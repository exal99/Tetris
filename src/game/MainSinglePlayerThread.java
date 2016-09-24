package game;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Map.Entry;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gameBoard.GameBoard;
import gui.GameGUI;
import gui.StartMenu;
import highscore.HighScore;
import soundPlayer.SoundPlayer;

public class MainSinglePlayerThread extends Thread {
	
	private GameGUI graphics;
	private GameBoard game;
	private JFrame root;
	private boolean debug;
	private String[] args;
	private HighScore score;
	
	public MainSinglePlayerThread(GameGUI graphics, GameBoard game, Timer t, JFrame root, HighScore score, String[] args) {
		this.graphics = graphics;
		this.game = game;
		this.root = root;
		debug = false;
		for (String s : args) {
			if (s.toLowerCase().equals("debug")) {
				debug = true;
			}
		}
		this.args = args;
		this.score = score;
	}
	
	@Override
	public void run() {
		long lastFrame = System.nanoTime();
		long lastTick = System.nanoTime();
		long lastUpdate = System.nanoTime();
		final long FPS = 60;
		final long TICKS = 1200;
		int ticks = 0;
		int frames = 0;
		long lastMessur = System.nanoTime();
		boolean running = true;
		while (isAlive() && !isInterrupted() && running) {
			if(game.isRuning()) {
				if (System.nanoTime() - lastFrame >= 1000000000/FPS) {
					graphics.update();
					game.incNumFramesSpedUp();
					lastFrame = System.nanoTime();
					if (debug) {
						frames++;
					}
				} if (System.nanoTime() - lastTick >= 1000000000/TICKS) {
					if (System.nanoTime() - lastUpdate >= 60/(game.getGravity()*1200) * 1000000000 && !game.isPaused() && game.isRuning()) {
						game.update();
						lastUpdate = System.nanoTime();
					}
					if (debug) {
						ticks++;
					}
					lastTick = System.nanoTime();
				} if (debug && System.nanoTime() - lastMessur >= 1000000000) {
					graphics.setAppend("FPS: " + 1000000000*((double) frames)/(System.nanoTime() - lastMessur) +
									   "<br>" + "TPS: " + 1000000000*((double) ticks)/(System.nanoTime() - lastMessur));
					graphics.update();
					System.out.println("FPS: " + 1000000000*((double) frames)/(System.nanoTime() - lastMessur));
					System.out.println("TPS: " + 1000000000*((double) ticks)/(System.nanoTime() - lastMessur));
					frames = 0;
					ticks = 0;
					lastMessur = System.nanoTime();
				}
			} else {
				saveHighScore();
//				switch(JOptionPane.showConfirmDialog(graphics.getRoot(), "You died a horrible death...\nPlay again?")) {
//				case JOptionPane.YES_OPTION:
//					game.reset();
//					break;
//				default:
//					try {
//						running = false;
//						graphics.getRoot().dispatchEvent(new WindowEvent(graphics.getRoot(), WindowEvent.WINDOW_CLOSING));
//						join();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					break;
//				}
				root.remove(graphics);
				root.add(new StartMenu(game, root, score, args));
				root.revalidate();
				try {
					join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void saveHighScore() {
//		HighScore score = getScore();
		String name = JOptionPane.showInputDialog("Your name:");
		if (name != null) {
			score.addScore(name, game.getScore());
			
			try {
				OutputStream out = new FileOutputStream("high_score.score");
				OutputStream buf = new BufferedOutputStream(out);
				ObjectOutput output = new ObjectOutputStream(buf);
				output.writeObject(score);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		displayScore(score);
		
	}
	
	private static HighScore getScore() {
		InputStream in = (new Object()).getClass().getResourceAsStream("/high_score.score");
		InputStream buffer = new BufferedInputStream(in);
		HighScore score = null;
		try {
			ObjectInput object = new ObjectInputStream(buffer);
			score = (HighScore) object.readObject();
		} catch (IOException e) {
			System.out.println("file_notFoud");
			score = new HighScore();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return score;
	}
	
	private void displayScore(HighScore score) {
		StringBuilder sb = new StringBuilder("Scores:\n----\n");
		for (Entry<String, Long> entry : score.getHighScorer()) {
			sb.append(entry.getKey() + ": " + entry.getValue() + "\n");
		}
		JOptionPane.showMessageDialog(root, sb.toString());
	}
	
	public static void main(String[] args) {
		Timer t = new Timer();
		GameBoard game = new GameBoard(t);
		JFrame root = new JFrame("Tetris");
		root.setFocusable(true);
		StartMenu menu = new StartMenu(game, root, getScore(), args);
		root.add(menu);
		root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		root.setSize(800, 600);
		root.setVisible(true);
		SoundPlayer sound = new SoundPlayer("music.wav");
		sound.playSound();
		
	}
}
