package ai.generation;

import java.io.Serializable;
import java.util.Timer;
import java.util.concurrent.ThreadLocalRandom;

import ai.ai.Ai;
import ai.aiGameBoard.AiGameBoard;
import ai.game.MainAiGameThread;

public class Generation implements Serializable{

	private static final long serialVersionUID = 3918496939878532598L;
	
	private static final String DEFAULT_GEN_SIZE = "1000";
	private static final String MIN_VAL = "-100";
	private static final String MAX_VAL = "100";
	private static final String MUTATE_INTERVALL = Double.toString(0.1 * Double.parseDouble(MAX_VAL));
	private static final String MUTATE_PROB = "0.1";
	
	private Ai[] gen;
	private Timer timer;
	
	public Generation(Timer t) {
		int genSize = Integer.parseInt(System.getProperty("default_gen_size", DEFAULT_GEN_SIZE));
		gen = new Ai[genSize];
		timer = t;
		double minVal = Double.parseDouble(System.getProperty("miv_val", MIN_VAL));
		double maxVal = Double.parseDouble(System.getProperty("max_val", MAX_VAL));
		for (int i = 0; i < genSize; i++) {
			ThreadLocalRandom rand = ThreadLocalRandom.current();
			gen[i] = new Ai(new AiGameBoard(timer), rand.nextDouble(minVal, maxVal), 
					rand.nextDouble(minVal, maxVal), rand.nextDouble(minVal, maxVal),
					rand.nextDouble(minVal, maxVal), rand.nextDouble(minVal, maxVal));
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
			ai.incAge();
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
