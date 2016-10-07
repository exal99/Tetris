package ai.ai;

import java.util.ArrayList;
import java.util.List;

import ai.aiGameBoard.AiGameBoard;

public class Ai {
	private AiGameBoard game;
	private final double HEIGHT_CONST;
	private final double ROUFNESS_CONST;
	private final double HOLES_CONST;
	private final double BLOCKING_CONST;
	private final double LINES_REMOVED_CONST;
	
	public Ai(AiGameBoard g, double height, double roufness, double holes, double blocking, double lines) {
		game = g;
		
		HEIGHT_CONST = height;
		ROUFNESS_CONST = roufness;
		HOLES_CONST = holes;
		BLOCKING_CONST = blocking;
		LINES_REMOVED_CONST = lines;
	}
	
	public Ai(AiGameBoard g, List<Double> vals) {
		game = g;
		
		HEIGHT_CONST = vals.get(0);
		ROUFNESS_CONST = vals.get(1);
		HOLES_CONST = vals.get(2);
		BLOCKING_CONST = vals.get(3);
		LINES_REMOVED_CONST = vals.get(4);
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
	
	@SuppressWarnings("unchecked")
	public void makeMove() {
		ArrayList<Action> bestAction = null;
		ArrayList<Action> currentAction = new ArrayList<Action>();
		double bestMove = Double.NEGATIVE_INFINITY;
		boolean[][] field = game.getColition();
		game.turnLeft();
		while (game.checkValidState(-1, 0)) {
			game.moveLeft();
		}
		for (int col = 0; col < field[0].length; col++) {
			for (int i = 0; i < col; i++) {
				currentAction.add(b -> b.moveRight());
			}
			Object[] res = bestMoveInCol(game.clone(), col);
			double colVal = (double) res[0];
			if (colVal > bestMove) {
				bestMove = colVal;
				bestAction = (ArrayList<Action>) res[1];
				bestAction.addAll(currentAction);
			}
			currentAction.clear();
		}
		for (Action a : bestAction) {
			a.run(game);
		}
	}
	
	private Object[] bestMoveInCol(AiGameBoard board, int col) {
		double bestOrientation = Double.NEGATIVE_INFINITY;
		ArrayList<Action> bestAction = new ArrayList<Action>();
		ArrayList<Action> actions = new ArrayList<Action>();
		for (int orientation = 0; orientation < 4; orientation++) {
			AiGameBoard currentOrien = board.clone();
			for (int o = 0; o < orientation; o++) {
				currentOrien.turnLeft();
				actions.add(b -> b.turnLeft());
			}
			currentOrien.place();
			double orienVal = evalBoard(currentOrien);
			if (orienVal > bestOrientation) {
				bestOrientation = orienVal;
				bestAction.clear();
				bestAction.addAll(actions);
			}
			
		}
		return new Object[]{bestOrientation, bestAction};
	}
	
	private interface Action {
		public void run(AiGameBoard b);
	}
}
