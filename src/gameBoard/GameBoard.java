package gameBoard;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import constants.Constants;
import tetrads.Alpha;
import tetrads.Gamma;
import tetrads.LeftSnake;
import tetrads.RightSnake;
import tetrads.Square;
import tetrads.Straight;
import tetrads.TTurn;
import tetrads.Tetrad;
import tetrads.Tetrads;

public class GameBoard {
	private Tetrad hold;
	private Tetrad controlling;
	private Tetrad queue;
	
	private boolean canHold;
	private boolean incSpeed;
	private int framesSpedUp;
	
	private boolean[][] field;
	private Tetrads[][] typeField;
	
	private boolean running;
	private int score;
	private int level;
	private int combo;
	private int gravity;
	
	private Timer timer;
	private TimerTask task;
	
	private final Random rand;
	private final long DELAY = 500;
	private final int MAX_Y = 22;
	private final int MAX_X = 10;
	
	public GameBoard(Timer t) {
		hold = null;
		canHold = true;
		rand = new Random();
		controlling = getRandomTetrad();
		queue = getRandomTetrad();
		field = new boolean[MAX_Y][MAX_X];
		typeField = new Tetrads[MAX_Y][MAX_X];
		score = 0;
		running = true;
		level = 0;
		timer = t;
		task = null;
		incSpeed = false;
		combo = 0;
		gravity = Constants.GRAVITY.get(0);
		framesSpedUp = 0;
	}
	
	public void reset(Timer t) {
		hold = null;
		canHold = true;
		controlling = getRandomTetrad();
		queue = getRandomTetrad();
		field = new boolean[MAX_Y][MAX_X];
		typeField = new Tetrads[MAX_Y][MAX_X];
		score = 0;
		running = true;
		level = 0;
		timer = t;
		task = null;
		incSpeed = false;
		combo = 0;
		gravity = Constants.GRAVITY.get(0);
		framesSpedUp = 0;
	}
	
	public void setIncSpeed(boolean newValue) {
		incSpeed = newValue;
	}
	
	public void incNumFramesSpedUp() {
		framesSpedUp++;
	}
	
	public double getGravity() {
		return (incSpeed) ? 20 : gravity/256D;
	}
	
	private Tetrad getRandomTetrad() {
		switch (rand.nextInt(7)) {
		case 0: 
			return new Alpha();
		case 1:
			return new Gamma();
		case 2:
			return new LeftSnake();
		case 3:
			return new RightSnake();
		case 4:
			return new Square();
		case 5:
			return new Straight();
		case 6:
			return new TTurn();
		default:
			throw new RuntimeException();
		}
	}
	
	private void spawnNew() {
		controlling = queue;
		if (!checkValidState(0, 0) && !checkValidState(0, 1)) {
			running = false;
		} else {
			queue = getRandomTetrad();
			canHold = true;
		}
		if (level + 1 % 100 != 0) {
			level++;
		}
	}
	
	private void spawnNew(Tetrads type) {
		/**
		 * Used when changing holding
		 */
		switch (type) {
		case ALPHA:
			controlling = new Alpha();
			break;
		case GAMMA:
			controlling = new Gamma();
			break;
		case LEFT_SNAKE:
			controlling = new LeftSnake();
			break;
		case RIGHT_SNAKE:
			controlling = new RightSnake();
			break;
		case SQUARE:
			controlling = new Square();
			break;
		case STRAIGHT:
			controlling = new Straight();
			break;
		case T_TURN:
			controlling = new TTurn();
			break;
		default:
			throw new RuntimeException();
		}
	}
	
	public void hold() {
		if (canHold) {
			if (hold != null) {
				Tetrad temp = hold;
				hold = controlling;
				spawnNew(temp.getType());
				
			} else {
				hold = controlling;
				controlling = queue;
				queue = getRandomTetrad();
			}
			canHold = false;
		}
	}
	
	private boolean checkValidState(Tetrad t, int deltaX, int deltaY) {
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				if ((t.getXPos() + col + deltaX >= MAX_X ||
					 t.getYPos() + row + deltaY >= MAX_Y ||
					 t.getXPos() + col + deltaX < 0 ||
					 t.getYPos() + row + deltaY < 0)) {
					if (t.colide(t.getXPos() + col, t.getYPos() + row)) {
						return false;
					} else {
						continue;
					}
				} if (field[t.getYPos() + row + deltaY][t.getXPos() + col + deltaX] &&
					t.colide(t.getXPos() + col, t.getYPos() + row)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean checkValidState(int deltaX, int deltaY) {
		return checkValidState(controlling, deltaX, deltaY);
	}
	
	public void update() {
		if (running) {
			if (checkValidState(0, 1)) {
				controlling.fall();
			} else {
				if (task == null) {
					task = new TimerTask() {
						@Override
						public void run() {
							place();
						}
					};
					timer.schedule(task, DELAY);
				}
			}
		}
	}
	
	public void place() {
		if (!checkValidState(0, 1)) {
			boolean[][] orientation = controlling.getOrientation();
			int x = controlling.getXPos();
			int y = controlling.getYPos();
			Tetrads type = controlling.getType();
			for (int row = 0; row < orientation.length; row++) {
				for (int col = 0; col < orientation[0].length; col++) {
					if (y + row < MAX_Y && x + col < MAX_X && x+col >= 0) {
						if (orientation[row][col]) {
							typeField[y + row][x + col] = type;
							field[y + row][x + col] = orientation[row][col];
						}
					}
				}
			}
			checkTetris();
			spawnNew();
		}
		if (task != null) {
			task.cancel();
			task = null;
		}
	}
	
	public boolean isSpedUp() {
		return incSpeed;
	}

	private void checkTetris() {
		int rowsRemoved = 0;
		for (int row = 0; row < MAX_Y; row++) {
			boolean tetrisOnRow = true;
			for (int col = 0; col < MAX_X; col++) {
				if (!field[row][col]) {
					tetrisOnRow = false;
					break;
				}
			}
			if (tetrisOnRow) {
				moveDown(row);
				rowsRemoved++;
			}
		}
		for (int i = 0; i < rowsRemoved; i++) {
			level++;
		}
		score += (Math.ceil((level + rowsRemoved)/4.0) + framesSpedUp) * rowsRemoved * combo;
		if (rowsRemoved != 0) {
			combo = combo + (2*rowsRemoved) - 2;
			gravity = (Constants.GRAVITY.get(level) != null) ? Constants.GRAVITY.get(level) : gravity;
			framesSpedUp = 0;
		} else {
			combo = 1;
		}
	}

	private void moveDown(int row) {
		for (; row >= 0; row--) {
			for (int col = 0; col < MAX_X; col++) {
				if (row != 0) {
					field[row][col] = field[row - 1][col];
					typeField[row][col] = typeField[row - 1][col];
			
				} else {
					field[row][col] = false;
					typeField[row][col] = null;
				}
			}
		}
		
	}
	
	public void moveLeft() {
		if (checkValidState(-1, 0)) {
			if (task != null) {
				task.cancel();
				task = null;
			}
			controlling.moveLeft();
		}
	}
	
	public void moveRight() {
		if (checkValidState(1, 0)) {
			if (task != null) {
				task.cancel();
				task = null;
			}
			controlling.moveRight();
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int row = 0; row < 2; row++) {
			for (int col = 0; col < MAX_X; col++) {
				sb.append(field[row][col] ? "o" : " ");
			}
			sb.append("\n");
		}
		for (int col = 0; col < MAX_X; col++) {
			sb.append("-");
		}
		sb.append("\n");
		for (int row = 2; row < MAX_Y; row++) {
			for (int col = 0; col < MAX_X; col++) {
				sb.append(field[row][col] ? "o" : " ");
			}
			sb.append("\n");
		}
		for (int col = 0; col < MAX_X; col++) {
			sb.append("-");
		}
		
		return sb.toString();
	}
	
	public void turnLeft() {
		controlling.rotateLeft();
		if (!checkValidState(0, 0)) {
			if (!checkValidState(0,-1)) {
				controlling.rotateRight();
				return;
			} else {
				controlling.moveUp();
			}
		}
		if (task != null) {
			task.cancel();
			task = null;
		}
	}
	
	public Tetrad getPlacement() {
		Tetrad ghost = null;
		try {
			ghost = controlling.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		ghost.copyAll(controlling);
		while (checkValidState(ghost, 0, 1)) {
			ghost.fall();
		}
		return ghost;
	}
	
	public void turnRight() {
		controlling.rotateRight();
		if (!checkValidState(0, 0)) {
			if (!checkValidState(0, -1)) {
				controlling.rotateLeft();
				return;
			} else {
				controlling.moveUp();
			}
		}
		if (task != null) {
			task.cancel();
			task = null;
		}
	}
	
	public void fastPlace() {
		while (checkValidState(0,1)) {
			controlling.fall();
		}
	}
	
	public int getScore() {
		return score;
	}
	
	public int getLevel() {
		return level;
	}
	
	public Tetrad getQueue() {
		return queue;
	}
	
	public Tetrad getControlling() {
		return controlling;
	}
	
	public Tetrads[][] getField() {
		return typeField;
	}
	
	public boolean isRuning() {
		return running;
	}
	
	public Tetrad getHolding() {
		return hold;
	}
}
