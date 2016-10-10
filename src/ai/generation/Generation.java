package ai.generation;

import java.io.Serializable;
import java.util.Timer;
import java.util.concurrent.ThreadLocalRandom;

import ai.ai.Ai;
import ai.aiGameBoard.AiGameBoard;
import ai.game.MainAiGameThread;

public class Generation implements Serializable{

	private static final long serialVersionUID = 3918496939878532598L;
	private static final int DEFAULT_GEN_SIZE = 100;
	private static final double MIN_VAL = -100D;
	private static final double MAX_VAL = 100D;
	
	private Ai[] gen;
	private Timer timer;
	
	public Generation(Timer t) {
		gen = new Ai[DEFAULT_GEN_SIZE];
		timer = t;
		for (int i = 0; i < DEFAULT_GEN_SIZE; i++) {
			ThreadLocalRandom rand = ThreadLocalRandom.current();
			gen[i] = new Ai(new AiGameBoard(timer), rand.nextDouble(MIN_VAL, MAX_VAL), 
					rand.nextDouble(MIN_VAL, MAX_VAL), rand.nextDouble(MIN_VAL, MAX_VAL),
					rand.nextDouble(MIN_VAL, MAX_VAL), rand.nextDouble(MIN_VAL, MAX_VAL));
		}
		
	}
	
	
	public void runSimulation() {
		long[] scores = new long[gen.length];
		for (int i = 0; i < gen.length; i++) {
			Ai ai = gen[i];
			MainAiGameThread thread = new MainAiGameThread(timer, ai.getGame());
			thread.start();
			ai.makeMove();
			while (thread.isAlive() && !thread.isInterrupted()) {
				ai.makeMove();
			}
			scores[i] = ai.getGame().getScore();
		}
		naturalSelection(scores);
	}
	
	public void runSimulation(int numTimes) {
		for (;numTimes > 0; numTimes--) {
			runSimulation();
		}
	}

	private void naturalSelection(long[] scores) {
	}

}
