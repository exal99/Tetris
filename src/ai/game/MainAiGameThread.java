package ai.game;


import ai.aiGameBoard.AiGameBoard;
import main.gui.GameGUI;

public class MainAiGameThread extends Thread {
	
	private AiGameBoard game;
	private static boolean updateGraphics;
	private static GameGUI graphics;
	
	public MainAiGameThread(AiGameBoard g) {
		game = g;
	}
	
	public static void setGraphicsUpdate(boolean value) {
		updateGraphics = value;
	}
	
	public static void setGraphics(GameGUI newGraphics) {
		setGraphicsUpdate(true);
		graphics = newGraphics;
	}
	
	public static GameGUI getGraphics() {
		return graphics;
	}
	
	@Override
	public void run() {
		long lastFrame = System.nanoTime();
		long lastTick = System.nanoTime();
		long lastUpdate = System.nanoTime();
		final long FPS = 60;
		final long TICKS = 1200;
		
		while (isAlive() && !isInterrupted() && game.isRuning()) {
			if (updateGraphics && System.nanoTime() - lastFrame >= 1000000000/FPS) {
				if (graphics != null) {
					graphics.update();
				}
				lastFrame = System.nanoTime();
			}
			if (System.nanoTime() - lastTick >= 1000000000/TICKS) {
				if (System.nanoTime() - lastUpdate >= 60/(game.getGravity()*1200) * 1000000000 && !game.isPaused() && game.isRuning()) {
					game.update();
					lastUpdate = System.nanoTime();
				}
				lastTick = System.nanoTime();
			}
		}
		try {
			join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
