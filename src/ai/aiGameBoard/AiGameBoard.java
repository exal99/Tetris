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
		return null;
	}
	
	public boolean checkValidState(int deltaX, int deltaY) {
		return super.checkValidState(deltaX, deltaY);
	}

}
