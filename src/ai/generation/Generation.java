package ai.generation;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import ai.ai.Ai;
import ai.aiGameBoard.AiGameBoard;
import ai.game.MainAiGameThread;
import ai.gui.Terminal;
import main.tetrads.Tetrad;

public class Generation implements Serializable{

	private static final long serialVersionUID = 3918496939878532598L;
	
	private static final String DEFAULT_GEN_SIZE = "1000";
	private static final String MIN_VAL = "-100";
	private static final String MAX_VAL = "100";
	private static final String MUTATE_INTERVALL = Double.toString(0.1 * Double.parseDouble(MAX_VAL));
	private static final String MUTATE_PROB = "0.1";
	private static final String PER_CENT_TO_BREETH = "0.5";
	private static boolean running; 
	
	private Ai[] gen;
	private AiGameBoard game;
	private long genNum;
	private long bestScore;
	private Ai bestAi;
	private long allTimeBestScore;
	private Ai allTimeBestAi;
	private double avrageScore;
	
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
		genNum = 1;
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
		bestScore = -1;
		bestAi = null;
		allTimeBestScore = -1;
		allTimeBestAi = null;
		avrageScore = 0;
		
	}
	
	public Ai[] getGen() {
		return gen;
	}
	
	public Ai getBestAi() {
		return bestAi;
	}
	
	public Ai getAllTimeBestAi() {
		return allTimeBestAi;
	}
	
	public long getBestScore() {
		return bestScore;
	}
	
	public long getAllTimeBestScore() {
		return allTimeBestScore;
	}
	
	public double getAvrageScore() {
		return avrageScore;
	}
	
	public static void setRunning(boolean newVal) {
		running = newVal;
	}
	
	public static boolean getRunning() {
		return running;
	}
	
	public void runSimulation() {
		long[] scores = new long[gen.length];
		bestScore = -1;
		bestAi = null;
		int updateStatus = 0;
		int multiplyer = 1;
		do {
			updateStatus = (multiplyer*gen.length)/10;
			multiplyer++;
		} while (updateStatus == 0);
		for (int i = 0; i < gen.length; i++) {
			if (i % updateStatus == 0) {
				StringBuilder sb = new StringBuilder("[");
				int numDots = (i* 100)/gen.length ;
				for (int a = 0; a < numDots / 10; a++) {
					sb.append('#');
				}
				for (int a = 0; a < 10 - (numDots/10); a++) {
					sb.append(".");
				}
				sb.append("] " + (i*100)/gen.length + " %<br>");
				Terminal.makeAppendRequest(sb.toString());
			}
			Ai ai = gen[i];
			if (MainAiGameThread.getGraphics() != null) {
				MainAiGameThread.getGraphics().setAppend(ai.toString());
			}
			play(ai);
			ai.incAge();
			scores[i] = ai.getGame().getScore();
			if (scores[i] > bestScore) {
				bestScore = scores[i];
				bestAi = ai;
			} if (scores[i] > allTimeBestScore) {
				allTimeBestAi = ai;
				allTimeBestScore = scores[i];
			}
			game.reset();
		}
		double sum = 0;
		for (long score : scores) {
			sum += score;
		}
		avrageScore = sum/scores.length;
		Terminal.makeAppendRequest("[##########] 100 %<br>");
		naturalSelection(scores);
		genNum++;
	}
	
	public static void play(Ai ai) {
		AiGameBoard game = ai.getGame();
		Tetrad previous = ai.getGame().getControlling();
		MainAiGameThread thread = new MainAiGameThread(game);
		thread.start();
		game.start();
		ai.makeMove();
		while (game.isRuning()) {
			if (running && MainAiGameThread.getGraphics() != null) {
				MainAiGameThread.getGraphics().setAppend(ai.toString());
			}
			if (running && previous != game.getControlling()) {
				previous = game.getControlling();
				ai.makeMove();
			}
		}
	}
	
	public long getGenNum() {
		return genNum;
	}
	
	public void runSimulation(int numTimes) {
		setRunning(true);
		for (;numTimes > 0; numTimes--) {
			runSimulation();
		}
		setRunning(false);
	}
	
	public AiGameBoard getGame() {
		return game;
	}

	private void naturalSelection(long[] scores) {
		long sum = 0;
		for (long val : scores) {
			sum += val;
		}
		double[] probs = new double[gen.length];
		HashMap<Integer, Ai> indexMap = new HashMap<Integer, Ai>();
		for (int i = 0; i < gen.length; i++) {
			probs[i] = ((double)scores[i])/sum;
			indexMap.put(i, gen[i]);
		}
		sort(probs, indexMap);
		double[] probIntervall = getProbIntervalls(probs);
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		int numNew = (int) Math.round(gen.length * Double.parseDouble(System.getProperty(VALUE_LABELS.get("percent_new"), PER_CENT_TO_BREETH)));
		Ai[] newChildren = new Ai[numNew];
		for (int i = 0; i < numNew; i++) {
			int ind = index(probIntervall, rand.nextDouble());
			Ai parrentOne = indexMap.get(ind);
			Ai parrentTwo = null;
			do {
				parrentTwo = indexMap.get(index(probIntervall, rand.nextDouble()));
			} while (parrentTwo == parrentOne);
			newChildren[i] = parrentOne.mate(parrentTwo, game);
			if (rand.nextDouble() <= Double.parseDouble(System.getProperty(VALUE_LABELS.get("mutate_prob"), MUTATE_PROB))) {
				newChildren[i].mutate(Double.parseDouble(System.getProperty(VALUE_LABELS.get("max_mutate"), MUTATE_INTERVALL)));
			}
		}
		ArrayList<Ai> killed = new ArrayList<Ai>();
		double[] killProbs = new double[probs.length];
		for (int i = 0; i < probs.length; i++) {
			killProbs[i] = (1 - probs[i])/(scores.length - 1.0);
		}
		sort(killProbs, indexMap);
		probIntervall = getProbIntervalls(killProbs);
		for (int i = 0; i < newChildren.length; i++) {
			Ai toKill = null;
			int index = -1;
			do {
				index = index(probIntervall, rand.nextDouble());
				toKill = indexMap.get(index);
			} while (contains(killed, toKill));
			killed.add(toKill);
			gen[index] = newChildren[i];
		}
	}
	
	private void sort(double[] probs, HashMap<Integer, Ai> indexMap) {
		quickSort(probs, indexMap, 0, probs.length - 1);
	}
	
	private void quickSort(double[] list, HashMap<Integer, Ai> indexMap, int first, int last) {
		if (first < last) {
			int pivIndex = getPivot(list, indexMap, first, last);
			quickSort(list, indexMap, first, pivIndex - 1);
			quickSort(list, indexMap, pivIndex + 1, last);
		}
	}
	
	private double[] getProbIntervalls(double[] probs) {
		double[] probIntervall = new double[probs.length];
		for (int i = 0; i < probs.length; i++) {
			double prevSum = probs[i];
			for (int a = 0; a < i; a++) {
				prevSum += probs[a];
			}
			probIntervall[i] = prevSum;
		}
		return probIntervall;
	}
	
	private int getPivot(double[] list, HashMap<Integer, Ai> indexMap, int first, int last) {
		chooseMiddle(list, indexMap, first, last);
		swap(list, indexMap, first, (first + last) / 2);
		int up = first;
		int down = last;
		int pivot = first;
		do {
			while (up < last && list[pivot] >= list[up]) {
				up++;
			}
			while (list[pivot] < list[down]) {
				down--;
			}
			if (up < down) {
				swap(list, indexMap, up, down);
			}
		} while (up < down);
		swap(list, indexMap, down, first);
		return down;
	}
	
	private void chooseMiddle(double[] list, HashMap<Integer, Ai> indexMap, int first, int last) {
		int middle = (first + last) / 2;
		if (list[middle] < list[first]) {
			swap(list, indexMap, first, middle);
		} if (list[last] < list[middle]) {
			swap(list, indexMap, middle, last);
		} if (list[middle] < list[first]) {
			swap(list, indexMap, first, middle);
		}
	}
	
	private void swap(double[] list, HashMap<Integer, Ai> indexMap, int a, int b) {
		double temp = list[a];
		Ai tempAi = indexMap.get(a);
		list[a] = list[b];
		list[b] = temp;
		indexMap.put(a, indexMap.get(b));
		indexMap.put(b, tempAi);
	}
	
	private int index(double[] list, double val) {
		int low = -1; 
		int high = list.length;
		while (low + 1 < high) {
			int mid = (high + low) / 2;
			if (list[mid] > val) {
				high = mid;
			} else if (list[mid] < val) {
				low = mid;
			} else {
				return mid;
			}
		}
		assert (low == -1 ) || list[low - 1] < val;
		assert (high > list.length) || (list[high] > val);
		return low + 1;
	}

	private boolean contains(ArrayList<Ai> toCheck, Ai toLookFor) {
		for (Ai ai : toCheck) {
			if (ai == toLookFor) {
				return true;
			}
		}
		return false;
	}
	
	public static class GenerationSerializer implements JsonSerializer<Generation> {

		@Override
		public JsonElement serialize(Generation src, Type typeOfSrc, JsonSerializationContext context) {
//			Gson gson = new GsonBuilder().registerTypeAdapter(Ai.class, new Ai.AiSerializer()).create();
			JsonObject retObj = new JsonObject();
			retObj.addProperty("genNum", src.genNum);
			retObj.addProperty("bestScore", src.bestScore);
			retObj.addProperty("allTimeBestScore", src.allTimeBestScore);
			retObj.addProperty("avrageScore", src.avrageScore);
			retObj.add("bestAi", context.serialize(src.bestAi, Ai.class));
			retObj.add("allTimeBestAi", context.serialize(src.allTimeBestAi, Ai.class));
			JsonArray array = new JsonArray();
			for (Ai ai : src.gen) {
				array.add(context.serialize(ai, Ai.class));
			}
			retObj.add("population", array);
			return retObj;
		}
		
	}
	
	public static class GenerationDeserializer implements JsonDeserializer<Generation> {

		@Override
		public Generation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject genObject = json.getAsJsonObject();
			Generation gen = new Generation(new Timer());
			gen.genNum = genObject.get("genNum").getAsLong();
			gen.bestScore = genObject.get("bestScore").getAsLong();
			gen.allTimeBestScore = genObject.get("allTimeBestScore").getAsLong();
			gen.avrageScore = genObject.get("avrageScore").getAsDouble();
			
			gen.bestAi = context.deserialize(genObject.get("bestAi"), Ai.class);
			gen.allTimeBestAi = context.deserialize(genObject.get("allTimeBestAi"), Ai.class);
			if (gen.bestAi != null) {
				gen.bestAi.setGame(gen.getGame());
				gen.allTimeBestAi.setGame(gen.getGame());
			}
			JsonArray population = genObject.get("population").getAsJsonArray();
			Ai[] aiGen = new Ai[population.size()];
			for (int i = 0; i < population.size(); i++) {
				aiGen[i] = context.deserialize(population.get(i), Ai.class);
				aiGen[i].setGame(gen.getGame());
			}
			gen.gen = aiGen;
			
			return gen;
		}
		
	}
}
