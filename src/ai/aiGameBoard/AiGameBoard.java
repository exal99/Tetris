package ai.aiGameBoard;

import java.util.Timer;

import main.gameBoard.GameBoard;

public class AiGameBoard extends GameBoard {
	
	
	public AiGameBoard(Timer t) {
		super(t);
		DELAY = 10;
		score = 1;
	}
	
	public AiGameBoard(Timer t, GameBoard g) {
		super(t, g);
	}
	
	/**
	 * Returns a clone of the AiGameBoard. The clone cannot be used in place of the parent
	 * since the clone isn't created as a perfect clone but rather as a clone used for simulating all
	 * possibilities. It contains all the necessary information to determine the best possible move for 
	 * the game board.
	 * 
	 * @return a clone of the AiGameBoard
	 */
	public AiGameBoard clone() {
		
		AiGameBoard clone = new AiGameBoard(null);
		clone.combo = combo;
		clone.controlling = controlling.clone();
		clone.field = field.clone();
		clone.hold = hold.clone();
		clone.level = level;
		clone.paused = paused;
		clone.queue = queue.clone();
		clone.running = running;
		clone.score = score;
		clone.typeField = typeField.clone();
		return clone;
	}
	
	@Override
	public boolean checkValidState(int deltaX, int deltaY) {
		return super.checkValidState(deltaX, deltaY);
	}
	
	@Override
	public void fastPlace() {
		while (checkValidState(0,1)) {
			controlling.fall();
		}
		place();
	}
	
	@Override
	public void update() {
		super.update();
		if (level >= 200) {
			running = false;
		}
	}

}
