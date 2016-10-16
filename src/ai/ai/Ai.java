package ai.ai;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import ai.aiGameBoard.AiGameBoard;
import main.tetrads.Tetrads;

public class Ai {
	private static int ID = 0;
	
	private AiGameBoard game;
	private double HEIGHT_CONST;
	private double ROUFNESS_CONST;
	private double HOLES_CONST;
	private double BLOCKING_CONST;
	private double LINES_REMOVED_CONST;
	private int age;
	private int id;
	
	public Ai(AiGameBoard g, double height, double roufness, double holes, double blocking, double lines) {
		game = g;
		age = 0;
		id = ID++;
		
		HEIGHT_CONST = height;
		ROUFNESS_CONST = roufness;
		HOLES_CONST = holes;
		BLOCKING_CONST = blocking;
		LINES_REMOVED_CONST = lines;
	}
	
	public Ai(AiGameBoard g, double... vals) {
		game = g;
		age = 0;
		id = ID++;
		
		HEIGHT_CONST = vals[0];
		ROUFNESS_CONST = vals[1];
		HOLES_CONST = vals[2];
		BLOCKING_CONST = vals[3];
		LINES_REMOVED_CONST = vals[4];
	}
	
	public int getAge() {
		return age;
	}
	
	public void incAge() {
		age++;
	}
	
	public int getId() {
		return id;
	}
	
	public double getHEIGHT_CONST() {
		return HEIGHT_CONST;
	}

	public double getROUFNESS_CONST() {
		return ROUFNESS_CONST;
	}

	public double getHOLES_CONST() {
		return HOLES_CONST;
	}

	public double getBLOCKING_CONST() {
		return BLOCKING_CONST;
	}

	public double getLINES_REMOVED_CONST() {
		return LINES_REMOVED_CONST;
	}
	
	public double[] getConsts() {
		return new double[]{HEIGHT_CONST, ROUFNESS_CONST, HOLES_CONST, BLOCKING_CONST, LINES_REMOVED_CONST};
	}
	
	public AiGameBoard getGame() {
		return game;
	}
	
	public void setGame(AiGameBoard newGame) {
		game = newGame;
	}

	private double evalBoard(AiGameBoard board) {
		return getHeightVal(board) + getRoufnessVal(board) + getHolesVal(board) + getBlockingVal(board) + getLinesRemovedVal(board);
	}
	
	private double getLinesRemovedVal(AiGameBoard board) {
		return LINES_REMOVED_CONST * (board.getScore() - game.getScore());
	}

	private double getBlockingVal(AiGameBoard board) {
		boolean[][] field = board.getColition();
		int numBlocking = 0;
		int maxRow = field.length;
		for (int col = 0; col < field[0].length; col++) {
			int blockingOnCol = 0;
			boolean holeOnCol = false;
			for (int row = maxRow - getHeight(field, col); row < maxRow; row++) {
				if (row != maxRow - 1 && field[row][col]) {
					blockingOnCol++;
				} else if (!field[row][col]) {
					holeOnCol = true;
				}
				if (row == maxRow - 1 && !holeOnCol) { //last time if no holes on current coll
					blockingOnCol = 0;
				}
			}
			numBlocking += blockingOnCol;
		}
		return numBlocking * BLOCKING_CONST;
	}

	private double getHolesVal(AiGameBoard board) {
		boolean[][] field = board.getColition();
		int holes = 0;
		int maxRow = field.length;
		for (int col = 0; col < field[0].length; col++) {
			for (int row = maxRow - getHeight(field, col); row < maxRow; row++) {
				if (!field[row][col]) {
					holes++;
				}
			}
		}
		return holes * HOLES_CONST;
	}

	private double getHeightVal(AiGameBoard board) {
		boolean[][] field = board.getColition();
		double height = 0;
		for (int col = 0; col < field[0].length; col++) {	
			int colHeight = getHeight(field, col);
//			if (colHeight > height) {
//				height = colHeight;
//			}
			height+= colHeight;
		}
		return height * HEIGHT_CONST;
	}
	
	private int getHeight(boolean[][] field, int col) {
		int maxRow = field.length;
		for (int row = 0; row < maxRow; row++) {
			if (field[row][col]) {
				return maxRow - row;
			} 
		}
		return 0;
	}
	
	private double getRoufnessVal(AiGameBoard board) {
		boolean[][] field = board.getColition();
		int prevHeight = getHeight(field, 0);
		int roufness = 0;
		for (int col = 1; col < field[0].length; col++) {
			int height = getHeight(field, col); 
			roufness += Math.abs(prevHeight - height);
			prevHeight = height;
		}
		return roufness * ROUFNESS_CONST;
	}
	
	public void makeMove() {
		ArrayList<Action> bestAction = new ArrayList<Action>();
		
		double bestMove = Double.NEGATIVE_INFINITY;
		boolean[][] field = game.getColition();
		game.turnLeft();
		while (game.checkValidState(-1, 0)) {
			game.moveLeft();
		}
		AiGameBoard superClone = game.clone();
		ArrayList<Action> currentMovement = new ArrayList<Action>();
		for (int col = 0; col < field[0].length; col++) {
			ArrayList<Action> currentOrien = new ArrayList<Action>();
			AiGameBoard subOneClone = superClone.clone();
			assert subOneClone.getColition() != game.getColition();
			for (int orien = 0; orien < ((game.getControlling().getType() == Tetrads.SQUARE || 
										  game.getControlling().getType() == Tetrads.STRAIGHT) ?  2 : 4);
				orien++) {
				AiGameBoard orienClone = subOneClone.clone();
				assert orienClone.getControlling() != subOneClone.getControlling();
				for (int i = 0; i < orien; i++) {
					orienClone.turnLeft();
				}
				if (orien == 2 && col == field[0].length - 1) {
					orienClone.moveRight();
				}
				orienClone.fastPlace();
				double colVal = evalBestMove(orienClone);
				if (colVal > bestMove) {
					bestMove = colVal;
					bestAction.clear();
					bestAction.addAll(currentMovement);
					bestAction.addAll(currentOrien);
				}
				currentOrien.add(g -> g.turnLeft());
				if (orien == 2 && col == field[0].length - 1) {
					currentOrien.add(g -> g.moveRight());
				}
				
			}
			currentMovement.add(g -> g.moveRight());
			superClone.moveRight();
			
		}
		for (Action a : bestAction) {
			a.run(game);
		}
		game.fastPlace();
	}
	
	private double evalBestMove(AiGameBoard localGame) {
		double bestMove = Double.NEGATIVE_INFINITY;
		while (localGame.checkValidState(-1, 0)) {
			localGame.moveLeft();
		}
		for (int col = 0; col < localGame.getColition()[0].length; col++) {
			double colVal = bestMoveInCol(localGame.clone(), col);
			bestMove = (colVal > bestMove) ? colVal : bestMove;
			localGame.moveRight();
		}
		return bestMove;
	}
	
	private double bestMoveInCol(AiGameBoard board, int col) {
		double bestOrientation = Double.NEGATIVE_INFINITY;
		for (int orientation = 0; orientation < ((board.getControlling().getType() == Tetrads.SQUARE ||
												  board.getControlling().getType() == Tetrads.STRAIGHT) ? 2 : 4);
			orientation++) {
			AiGameBoard currentOrien = board.clone();
			currentOrien.fastPlace();
			double orienVal = evalBoard(currentOrien);
			bestOrientation = (orienVal > bestOrientation) ? orienVal : bestOrientation;
			
			board.turnLeft();
			if (orientation == 2 && col == board.getField()[0].length - 1) {
				board.moveRight();
			}
			
		}
		return bestOrientation;
	}
	
	public Ai mate(Ai otherParrent, AiGameBoard newGame) {
		double[] consts = new double[getConsts().length];
		double[] selfConsts = getConsts();
		double[] otherConsts = otherParrent.getConsts();
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		for (int i = 0; i < selfConsts.length; i++) {
			consts[i] = (rand.nextBoolean()) ? selfConsts[i] : otherConsts[i];
		}
		return new Ai(newGame, consts);
	}
	
	public void mutate(double maxMutate) {
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		switch (rand.nextInt(0, 5)) {
		case 0:
			HEIGHT_CONST += rand.nextDouble(-maxMutate, maxMutate);
			break;
		case 1:
			ROUFNESS_CONST += rand.nextDouble(-maxMutate, maxMutate);
			break;
		case 2:
			HOLES_CONST += rand.nextDouble(-maxMutate, maxMutate);
			break;
		case 3:
			BLOCKING_CONST += rand.nextDouble(-maxMutate, maxMutate);
			break;
		case 4:
			LINES_REMOVED_CONST += rand.nextDouble(-maxMutate, maxMutate);
			break;
		default:
			assert false;
			break;
		}
	}
	
	@Override
	public String toString() {
		return id + ": [" + HEIGHT_CONST + ", " + ROUFNESS_CONST + ", " + HOLES_CONST + ", " + BLOCKING_CONST + ", " + LINES_REMOVED_CONST + "]"; 
	}
	
	private interface Action {
		public void run(AiGameBoard b);
	}
	
	public static class AiSerializer implements JsonSerializer<Ai> {

		@Override
		public JsonElement serialize(Ai src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject aiObject = new JsonObject();
			aiObject.addProperty("height", src.HEIGHT_CONST);
			aiObject.addProperty("roufness", src.ROUFNESS_CONST);
			aiObject.addProperty("holes", src.HOLES_CONST);
			aiObject.addProperty("blocking", src.BLOCKING_CONST);
			aiObject.addProperty("removal", src.LINES_REMOVED_CONST);
			aiObject.addProperty("age", src.age);
			aiObject.addProperty("id", src.id);
			aiObject.addProperty("ID", Ai.ID);
			return aiObject;
		}
		
	}
	
	public static class AiDeserializer implements JsonDeserializer<Ai> {

		@Override
		public Ai deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			JsonObject aiObject = json.getAsJsonObject();
			Ai ai = new Ai(null, 0, 0, 0, 0, 0);
			Ai.ID = aiObject.get("ID").getAsInt();
			ai.age = aiObject.get("age").getAsInt();
			ai.id = aiObject.get("id").getAsInt();
			ai.HEIGHT_CONST = aiObject.get("height").getAsDouble();
			ai.ROUFNESS_CONST = aiObject.get("roufness").getAsDouble();
			ai.HOLES_CONST = aiObject.get("holes").getAsDouble();
			ai.BLOCKING_CONST = aiObject.get("blocking").getAsDouble();
			ai.LINES_REMOVED_CONST = aiObject.get("removal").getAsDouble();
			return ai;
		}
		
	}
}
