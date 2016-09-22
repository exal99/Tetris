package game;

import java.awt.event.WindowEvent;
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

import javax.swing.JOptionPane;

import gameBoard.GameBoard;
import gui.GameGUI;
import highscore.HighScore;
import soundPlayer.SoundPlayer;

public class Main extends Thread {
	
	private GameGUI graphics;
	private GameBoard game;
	private boolean debug;
	private Timer timer;
	
	public Main(GameGUI graphics, GameBoard game, Timer t, String[] args) {
		this.graphics = graphics;
		this.game = game;
		debug = false;
		for (String s : args) {
			if (s.toLowerCase().equals("debug")) {
				debug = true;
			}
		}
		timer = t;
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
					if (System.nanoTime() - lastUpdate >= 60/(game.getGravity()*1200) * 1000000000) {
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
				switch(JOptionPane.showConfirmDialog(graphics.getRoot(), "You died a horrible death...\nPlay again?")) {
				case JOptionPane.YES_OPTION:
					game.reset(timer);
					break;
				default:
					try {
						running = false;
						graphics.getRoot().dispatchEvent(new WindowEvent(graphics.getRoot(), WindowEvent.WINDOW_CLOSING));
						join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}
	
	private void saveHighScore() {
		InputStream in = getClass().getResourceAsStream("/high_score.score");
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
		String name = JOptionPane.showInputDialog("Your name:");
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
		StringBuilder sb = new StringBuilder("Scores:\n----\n");
		for (Entry<String, Long> entry : score.getHighScorer()) {
			sb.append(entry.getKey() + ": " + entry.getValue() + "\n");
		}
		JOptionPane.showMessageDialog(graphics.getRoot(), sb.toString());
	}
	
	public static void main(String[] args) {
		Timer t = new Timer();
		GameBoard game = new GameBoard(t);
		GameGUI graphics = new GameGUI(game, t);
		graphics.getRoot().setSize(800, 600);
		graphics.getRoot().setVisible(true);
		Main mainThread = new Main(graphics, game, t, args);
		SoundPlayer sound = new SoundPlayer("music.wav");
		mainThread.start();
		sound.playSound();
		
	}
}
