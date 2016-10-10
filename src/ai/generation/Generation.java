package ai.generation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
	private static final String PER_CENT_TO_BREETH = "0.5";
	
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
		long sum = 0;
		for (long val : scores) {
			sum += val;
		}
		
		Ai[] toPickFrom = new Ai[1000];
		int currIndex = 0;
		for (int i = 0; i<scores.length; i++) {
			int toAdd = (int) Math.round(1000 * ((double) scores[i])/sum);
			assert toAdd < 1000;
			for (int a = 0; a < toAdd; a++) {
				toPickFrom[currIndex] = gen[i];
				currIndex++;
			}
		}
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		int numNew = (int) Math.round(gen.length * Double.parseDouble(System.getProperty("percent_new", PER_CENT_TO_BREETH)));
		Ai[] newChildren = new Ai[numNew];
		for (int i = 0; i < numNew; i++) {
			Ai parrentOne = toPickFrom[rand.nextInt(toPickFrom.length)];
			Ai parrentTwo = null;
			do {
				parrentTwo = toPickFrom[rand.nextInt(toPickFrom.length)];
			} while (parrentTwo == parrentOne);
			
			newChildren[i] = parrentOne.mate(parrentTwo, new AiGameBoard(timer));
			if (rand.nextDouble() <= Double.parseDouble(System.getProperty("mutate_prob", MUTATE_PROB))) {
				newChildren[i].mutate(Double.parseDouble(System.getProperty("max_mutate", MUTATE_INTERVALL)));
			}
		}
		
		HashMap<Ai, Integer> aiIndex = new HashMap<Ai, Integer>();
		toPickFrom = new Ai[1000];
		ArrayList<Ai> killed = new ArrayList<Ai>();
		currIndex = 0;
		for (int i = 0; i < scores.length; i++) {
			int toAdd = (int) Math.round(1000 * (1 - (((double) scores[i])/sum)));
			assert toAdd < 1000;
			for (int a = 0; a < toAdd; a++) {
				toPickFrom[currIndex] = gen[i];
				currIndex++;
			}
			aiIndex.put(gen[i], i);
		}
		
		for (int i = 0; i < newChildren.length; i++) {
			
		}
	}
	
	private boolean contains(ArrayList<Ai> toCheck, Ai toLookFor) {
		for (Ai ai : toCheck) {
			if (ai == toLookFor) {
				return true;
			}
		}
		return false;
	}

}
