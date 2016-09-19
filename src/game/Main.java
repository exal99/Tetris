package game;

import java.util.Timer;

import gameBoard.GameBoard;
import gui.GameGUI;

public class Main extends Thread {
	
	private GameGUI graphics;
	private GameBoard game;
	
	public Main(GameGUI graphics, GameBoard game) {
		this.graphics = graphics;
		this.game = game;
	}
	
	@Override
	public void run() {
		long lastFrame = System.nanoTime();
		long lastTick = System.nanoTime();
		final long FPS = 60;
		final long TICKS = 240;
		long moveSpeed = 100000000;
		long timeToUpdate = moveSpeed;
		while (isAlive() && !isInterrupted()) {
			if (System.nanoTime() - lastFrame >= 1000000000/FPS) {
				graphics.update();
				lastFrame = System.nanoTime();
			} if (System.nanoTime() - lastTick >= 1000000000/TICKS) {
				timeToUpdate = timeToUpdate - (System.nanoTime() - lastTick);
				if (timeToUpdate <= 0) {
					game.update();
					timeToUpdate = moveSpeed;
				}
				lastTick = System.nanoTime();
			}
		}
	}
	
	public static void main(String[] args) {
		Timer t = new Timer();
		GameBoard game = new GameBoard(t);
		GameGUI graphics = new GameGUI(game);
		graphics.getRoot().setSize(800, 600);
		graphics.getRoot().setVisible(true);
		Main mainThread = new Main(graphics, game);
		mainThread.start();
	}
}
