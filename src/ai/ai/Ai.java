package ai.ai;

import ai.aiGameBoard.AiGameBoard;

public class Ai {
	private AiGameBoard game;
	private long score;
	private final double HEIGHT_CONST;
	private final double ROUFNESS_CONST;
	private final double HOLES_CONST;
	private final double BLOCKING_CONST;
	private final double LINES_REMOVED_CONST;
	
	public Ai(AiGameBoard g, double height, double roufness, double holes, double blocking, double lines) {
		game = g;
		score = game.getScore();
		
		HEIGHT_CONST = height;
		ROUFNESS_CONST = roufness;
		HOLES_CONST = holes;
		BLOCKING_CONST = blocking;
		LINES_REMOVED_CONST = lines;
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

	private double evalBoard() {
		return 0D;
	}
	
	public void makeMove() {
		
	}
}
