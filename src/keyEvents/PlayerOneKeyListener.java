package keyEvents;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import gameBoard.GameBoard;

public class PlayerOneKeyListener implements KeyListener {
	
	private GameBoard game;
	private TimerTask moveRight;
	private TimerTask moveLeft;
	private Timer timer;
	private final static long DELAY = 250;
	
	public PlayerOneKeyListener(GameBoard game, Timer t) {
		this.game = game;
		moveRight = null;
		moveLeft = null;
		timer = t;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (game.isRuning() && !game.isPaused()) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				game.hold();
				break;
			case KeyEvent.VK_LEFT:
				game.moveLeft();
				if (moveLeft == null && moveRight == null) {
					moveLeft();
				} else if (moveRight != null) {
					moveRight.cancel();
					moveRight = null;
				}
				break;
			case KeyEvent.VK_RIGHT:
				game.moveRight();
				if (moveRight == null && moveLeft == null) {
					moveRight();
				} else if (moveLeft != null) {
					moveLeft.cancel();
					moveLeft = null;
				}
				break;
			case KeyEvent.VK_N:
				game.turnLeft();
				break;
			case KeyEvent.VK_M:
				game.turnRight();
				break;
			case KeyEvent.VK_B:
				game.fastPlace();
				break;
			case KeyEvent.VK_DOWN:
				game.setIncSpeed(true);
				break;
			case KeyEvent.VK_P:
				game.setPause(true);
				break;
			}
		} else {
			if (e.getKeyCode() == KeyEvent.VK_P) {
				game.setPause(false);
			}
		}
	}
	
	private void moveRight() {
		moveRight = new TimerTask() {
			@Override
			public void run() {
				game.moveRight();
			}
		};
		timer.scheduleAtFixedRate(moveRight, DELAY, Math.round(1/30D * 1000));
	}
	
	private void moveLeft() {
		moveLeft = new TimerTask() {
			@Override
			public void run() {
				game.moveLeft();
			}
		};
		timer.scheduleAtFixedRate(moveLeft, DELAY, Math.round(1/30D * 1000));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (game.isRuning() && !game.isPaused()) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_DOWN:
				game.setIncSpeed(false);
				break;
			case KeyEvent.VK_LEFT:
				if (moveLeft == null) { //both keys where pressed down and left released first
					moveRight();
				} else {
					moveLeft.cancel();
					moveLeft = null;
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (moveRight == null) { //both keys where pressed down and right released first
					moveLeft();
				} else {
					moveRight.cancel();
					moveRight = null;
				}
			}	
		}
	}

}
