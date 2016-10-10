package ai.game;

import java.util.Timer;

import ai.aiGameBoard.AiGameBoard;

public class MainAiGameThread extends Thread {
	
	private AiGameBoard game;
	private Timer timer;
	
	public MainAiGameThread(Timer t, AiGameBoard g) {
		timer = t;
		game = g;
	}
	
	@Override
	public void run() {
		
	}
}
