package game;

import java.util.Timer;

import gameBoard.GameBoard;
import gui.GameGUI;

public class Main extends Thread {
	
	private GameGUI graphics;
	private GameBoard game;
	private boolean debug;
	
	public Main(GameGUI graphics, GameBoard game, String[] args) {
		this.graphics = graphics;
		this.game = game;
		debug = false;
		for (String s : args) {
			if (s.toLowerCase().equals("debug")) {
				debug = true;
			}
		}
	}
	
	@Override
	public void run() {
		long lastFrame = System.nanoTime();
		long lastTick = System.nanoTime();
		long lastUpdate = System.nanoTime();
		final long FPS = 60;
		final long TICKS = 1200;
		double gravity = 0.1;
		int ticks = 0;
		int frames = 0;
//		boolean debug = false;
		long lastMessur = System.nanoTime();
		while (isAlive() && !isInterrupted()) {
			if (System.nanoTime() - lastFrame >= 1000000000/FPS) {
				graphics.update();
				lastFrame = System.nanoTime();
				if (debug) {
					frames++;
				}
			} if (System.nanoTime() - lastTick >= 1000000000/TICKS) {
				if (System.nanoTime() - lastUpdate >= 60/(gravity*1200) * 1000000000) {
					game.update();
					lastUpdate = System.nanoTime();
				}
				if (debug) {
					ticks++;
				}
				lastTick = System.nanoTime();
			} if (debug && System.nanoTime() - lastMessur >= 1000000000) {
				System.out.println("FPS: " + 1000000000*((double) frames)/(System.nanoTime() - lastMessur));
				System.out.println("TPS: " + 1000000000*((double) ticks)/(System.nanoTime() - lastMessur));
				frames = 0;
				ticks = 0;
				lastMessur = System.nanoTime();
			}
		}
	}
	
	public static void main(String[] args) {
		Timer t = new Timer();
		GameBoard game = new GameBoard(t);
		GameGUI graphics = new GameGUI(game);
		graphics.getRoot().setSize(800, 600);
		graphics.getRoot().setVisible(true);
		Main mainThread = new Main(graphics, game, args);
		mainThread.start();
	}
}
