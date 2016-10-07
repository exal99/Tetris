package ai.aiGameBoard;

import java.util.Timer;

import main.gameBoard.GameBoard;

public class AiGameBoard extends GameBoard {
	
	public AiGameBoard(Timer t) {
		super(t);
		// TODO Auto-generated constructor stub
	}
	
	public AiGameBoard(Timer t, GameBoard g) {
		super(t, g);
	}
	
	public AiGameBoard clone() {
		/**
		 * Returns a clone of the AiGameBoard. The clone cannot be used in place of the parent
		 * since the clone isn't created as a perfect clone but rather as a clone used for simulating all
		 * possibilities. It contains all the necessary information to determine the best possible move for 
		 * the game board.
		 * 
		 * @return a clone of the AiGameBoard
		 */
		AiGameBoard clone = new AiGameBoard(null);
		clone.combo = combo;
		clone.controlling = controlling;
		clone.field = field;
		clone.hold = hold;
		clone.level = level;
		clone.paused = paused;
		clone.queue = queue;
		clone.running = running;
		clone.score = score;
		clone.typeField = typeField;
		return clone;
	}
	
	public boolean checkValidState(int deltaX, int deltaY) {
		return super.checkValidState(deltaX, deltaY);
	}

}
