package game;

import java.util.Timer;

import javax.swing.JFrame;

import gameBoard.GameBoard;
import gui.StartMenu;
import gui.TwoPlayerGameGUI;
import highscore.HighScore;

public class MainMultiPlayerThread extends Thread {
	private TwoPlayerGameGUI graphics;
	private GameBoard player1;
	private GameBoard player2;
	private JFrame root;
	private boolean debug;
	private String[] args;
	private HighScore highScore;
	
	public MainMultiPlayerThread(TwoPlayerGameGUI graphics, GameBoard player1, GameBoard player2, Timer t, JFrame root, HighScore hscore, String[] args) {
		this.graphics = graphics;
		this.player1 = player1;
		this.player2 = player2;
		this.root = root;
		debug = false;
		for (String s : args) {
			if (s.toLowerCase().equals("debug")) {
				debug = true;
			}
		}
		this.args = args;
		this.highScore = hscore;
	}
	
	@Override
	public void run() {
		long lastFrame = System.nanoTime();
		long lastTick = System.nanoTime();
		long lastPlayer1Update = System.nanoTime();
		long lastPlayer2Update = System.nanoTime();
		final long FPS = 60;
		final long TICKS = 1200;
		int ticks = 0;
		int frames = 0;
		long lastMessur = System.nanoTime();
		boolean running = true;
		while (isAlive() && !isInterrupted() && running) {
			if(player1.isRuning() && player2.isRuning()) {
				if (System.nanoTime() - lastFrame >= 1000000000/FPS) {
					graphics.update();
					if (player1.isSpedUp()) {
						player1.incNumFramesSpedUp();
					} if (player2.isSpedUp()) {
						player2.incNumFramesSpedUp();
					}
					lastFrame = System.nanoTime();
					if (debug) {
						frames++;
					}
				} if (System.nanoTime() - lastTick >= 1000000000/TICKS) {
					if (System.nanoTime() - lastPlayer1Update >= 60/(player1.getGravity()*1200) * 1000000000 && !player1.isPaused()) {
						player1.update();
						lastPlayer1Update = System.nanoTime();
					}
					if (System.nanoTime() - lastPlayer2Update >= 60/(player2.getGravity()*1200) * 1000000000 && !player2.isPaused()) {
						player2.update();
						lastPlayer1Update = System.nanoTime();
					}
					if (debug) {
						ticks++;
					}
					lastTick = System.nanoTime();
				} if (debug && System.nanoTime() - lastMessur >= 1000000000) {
					graphics.setPlayer1Append("FPS: " + 1000000000*((double) frames)/(System.nanoTime() - lastMessur) +
									   "<br>" + "TPS: " + 1000000000*((double) ticks)/(System.nanoTime() - lastMessur));
					graphics.update();
					System.out.println("FPS: " + 1000000000*((double) frames)/(System.nanoTime() - lastMessur));
					System.out.println("TPS: " + 1000000000*((double) ticks)/(System.nanoTime() - lastMessur));
					frames = 0;
					ticks = 0;
					lastMessur = System.nanoTime();
				}
			} else {
//				saveHighScore();
				root.remove(graphics);
				root.add(new StartMenu(player1, player2, root, highScore, args));
				root.revalidate();
				try {
					join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
