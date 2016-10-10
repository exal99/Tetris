package ai.ai;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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
				if (field[row][col]) {
					blockingOnCol++;
				} else {
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
			for (int row = maxRow - getHeight(field, col); row < maxRow - 1; row++) {
				if (field[row][col] && !field[row + 1][col]) {
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
			if (colHeight > height) {
				height = colHeight;
			}
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
			for (int orien = 0; orien < ((game.getControlling().getType() == Tetrads.SQUARE || 
										  game.getControlling().getType() == Tetrads.STRAIGHT) ?  2 : 4);
				orien++) {
				AiGameBoard orienClone = subOneClone.clone();
				orienClone.place();
				double colVal = evalBestMove(orienClone);
				if (colVal > bestMove) {
					bestMove = colVal;
					bestAction.clear();
					bestAction.addAll(currentOrien);
					bestAction.addAll(currentMovement);
				}
				currentOrien.add(g -> g.turnLeft());
				subOneClone.turnLeft();
				if (orien == 2 && col == field[0].length - 1) {
					subOneClone.moveRight();
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
	
	private double evalBestMove(AiGameBoard game) {
		double bestMove = Double.NEGATIVE_INFINITY;
		while (game.checkValidState(-1, 0)) {
			game.moveLeft();
		}
		for (int col = 0; col < game.getColition()[0].length; col++) {
			game.moveRight();
			double colVal = bestMoveInCol(game, col);
			bestMove = (colVal > bestMove) ? colVal : bestMove;
		}
		return bestMove;
	}
	
	private double bestMoveInCol(AiGameBoard board, int col) {
		double bestOrientation = Double.NEGATIVE_INFINITY;
		for (int orientation = 0; orientation < ((board.getControlling().getType() == Tetrads.SQUARE ||
												  board.getControlling().getType() == Tetrads.STRAIGHT) ? 2 : 4);
			orientation++) {
			AiGameBoard currentOrien = board.clone();
			currentOrien.turnLeft();
			if (orientation == 2 && col == board.getField()[0].length - 1) {
				currentOrien.moveRight();
			}
			currentOrien.place();
			
			double orienVal = evalBoard(currentOrien);
			bestOrientation = (orienVal > bestOrientation) ? orienVal : bestOrientation;
			
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
	
	private interface Action {
		public void run(AiGameBoard b);
	}
}
