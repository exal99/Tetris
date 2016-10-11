package ai.generation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.ThreadLocalRandom;

import ai.ai.Ai;
import ai.aiGameBoard.AiGameBoard;
import ai.game.MainAiGameThread;
import main.tetrads.Tetrad;

public class Generation implements Serializable{

	private static final long serialVersionUID = 3918496939878532598L;
	
	private static final String DEFAULT_GEN_SIZE = "1000";
	private static final String MIN_VAL = "-100";
	private static final String MAX_VAL = "100";
	private static final String MUTATE_INTERVALL = Double.toString(0.1 * Double.parseDouble(MAX_VAL));
	private static final String MUTATE_PROB = "0.1";
	private static final String PER_CENT_TO_BREETH = "0.5";
	
	private Ai[] gen;
	private AiGameBoard game;
	
	public static final HashMap<String, String> VALUE_LABELS = new HashMap<String, String>();
	static {
		VALUE_LABELS.put("min_val", "min");
		VALUE_LABELS.put("max_val", "max");
		VALUE_LABELS.put("default_gen_size", "default_generation_size");
		VALUE_LABELS.put("percent_new", "percent_new");
		VALUE_LABELS.put("mutate_prob", "mutate_probability");
		VALUE_LABELS.put("max_mutate", "max_mutate_interval");
	}
	
	public Generation(Timer t) {
		int genSize = Integer.parseInt(System.getProperty(VALUE_LABELS.get("default_gen_size"), DEFAULT_GEN_SIZE));
		gen = new Ai[genSize];
		Timer timer = t;
		double minVal = Double.parseDouble(System.getProperty(VALUE_LABELS.get("min_val"), MIN_VAL));
		double maxVal = Double.parseDouble(System.getProperty(VALUE_LABELS.get("max_val"), MAX_VAL));
		game = new AiGameBoard(timer);
		for (int i = 0; i < genSize; i++) {
			ThreadLocalRandom rand = ThreadLocalRandom.current();
			gen[i] = new Ai(game, rand.nextDouble(minVal, maxVal), 
					rand.nextDouble(minVal, maxVal), rand.nextDouble(minVal, maxVal),
					rand.nextDouble(minVal, maxVal), rand.nextDouble(minVal, maxVal));
		}
		
	}
	
	public void runSimulation() {
		long[] scores = new long[gen.length];
		for (int i = 0; i < gen.length; i++) {
			Ai ai = gen[i];
			Tetrad controlling = ai.getGame().getControlling();
			MainAiGameThread thread = new MainAiGameThread(ai.getGame());
			thread.start();
			ai.makeMove();
			while (thread.isAlive() && !thread.isInterrupted()) {
				if (controlling != ai.getGame().getControlling()) {
					ai.makeMove();
					controlling = ai.getGame().getControlling();
				}
			}
			ai.incAge();
			scores[i] = ai.getGame().getScore();
			game.reset();
		}
		naturalSelection(scores);
	}
	
	public void runSimulation(int numTimes) {
		for (;numTimes > 0; numTimes--) {
			runSimulation();
		}
	}
	
	public AiGameBoard getGame() {
		return game;
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
		int numNew = (int) Math.round(gen.length * Double.parseDouble(System.getProperty(VALUE_LABELS.get("percent_new"), PER_CENT_TO_BREETH)));
		Ai[] newChildren = new Ai[numNew];
		for (int i = 0; i < numNew; i++) {
			Ai parrentOne = toPickFrom[rand.nextInt(toPickFrom.length)];
			Ai parrentTwo = null;
			do {
				parrentTwo = toPickFrom[rand.nextInt(toPickFrom.length)];
			} while (parrentTwo == parrentOne);
			
			newChildren[i] = parrentOne.mate(parrentTwo, game);
			if (rand.nextDouble() <= Double.parseDouble(System.getProperty(VALUE_LABELS.get("mutate_prob"), MUTATE_PROB))) {
				newChildren[i].mutate(Double.parseDouble(System.getProperty(VALUE_LABELS.get("max_mutate"), MUTATE_INTERVALL)));
			}
		}
		
		HashMap<Ai, Integer> aiIndex = new HashMap<Ai, Integer>();
		toPickFrom = new Ai[1000];
		ArrayList<Ai> killed = new ArrayList<Ai>();
		currIndex = 0;
		for (int i = 0; i < scores.length; i++) {
			int size = gen.length;
			int toAdd = (int) Math.round(size * (((((double) scores[i])/sum) - 1)/(1.0-size)));
			assert toAdd < 1000;
			for (int a = 0; a < toAdd; a++) {
				toPickFrom[currIndex] = gen[i];
				currIndex++;
			}
			aiIndex.put(gen[i], i);
		}
		
		for (int i = 0; i < newChildren.length; i++) {
			int indToKill = -1;
			int pickFromInd = -1;
			do {
				pickFromInd = rand.nextInt(toPickFrom.length);
				indToKill = aiIndex.get(toPickFrom[pickFromInd]);
			} while (!contains(killed, toPickFrom[pickFromInd]));
			killed.add(toPickFrom[pickFromInd]);
			gen[indToKill] = newChildren[i];
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
