package main.game;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Map.Entry;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.gameBoard.GameBoard;
import main.gui.GameGUI;
import main.gui.StartMenu;
import main.highscore.HighScore;

public class MainSinglePlayerThread extends Thread {
	
	private GameGUI graphics;
	private GameBoard game;
	private GameBoard second;
	private JFrame root;
	private boolean debug;
	private String[] args;
	private HighScore score;
	
	public MainSinglePlayerThread(GameGUI graphics, GameBoard game, GameBoard second, Timer t, JFrame root, HighScore score, String[] args) {
		this.graphics = graphics;
		this.game = game;
		this.second = second;
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
					if (game.isSpedUp()) {
						game.incNumFramesSpedUp();
					}
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
				root.remove(graphics);
				root.add(new StartMenu(game, second, root, score, args));
				root.revalidate();
				try {
					join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void saveHighScore() {
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
	
	private void displayScore(HighScore score) {
		StringBuilder sb = new StringBuilder("Scores:\n----\n");
		for (Entry<String, Long> entry : score.getHighScorer()) {
			sb.append(entry.getKey() + ": " + entry.getValue() + "\n");
		}
		JOptionPane.showMessageDialog(root, sb.toString());
	}
}
