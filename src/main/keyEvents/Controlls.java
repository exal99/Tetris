package main.keyEvents;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.KeyStroke;

import org.ini4j.Ini;

import main.gameBoard.GameBoard;

public class Controlls implements KeyListener {
	
	private GameBoard game;
	private TimerTask moveRight;
	private TimerTask moveLeft;
	private Timer timer;
	private final static long DELAY = 250;
	private HashMap<Integer, Runnable> keyPressMap;
	private HashMap<Integer, Runnable> keyReleasedMap;
	private final int PAUSE;
	
	public Controlls(GameBoard game, Timer t, Ini.Section config) {
		this.game = game;
		moveRight = null;
		moveLeft = null;
		timer = t;
		PAUSE = KeyStroke.getKeyStroke((String) config.get("pause")).getKeyCode();
		createKeyPressMap(config);
		createKeyReleasedMap(config);
	}
	
	private void createKeyPressMap(Ini.Section config) {
		keyPressMap = new HashMap<Integer, Runnable>();
		int hold = KeyStroke.getKeyStroke(config.get("hold")).getKeyCode();
		int left = KeyStroke.getKeyStroke((String) config.get("left")).getKeyCode();
		int right = KeyStroke.getKeyStroke((String) config.get("right")).getKeyCode();
		int down = KeyStroke.getKeyStroke((String) config.get("speed_up")).getKeyCode();
		int turn_right = KeyStroke.getKeyStroke((String) config.get("turn_right")).getKeyCode();
		int turn_left = KeyStroke.getKeyStroke((String) config.get("turn_left")).getKeyCode();
		int place = KeyStroke.getKeyStroke((String) config.get("place")).getKeyCode();
		
		keyPressMap.put(left, () -> {
			game.moveLeft();
			if (moveLeft == null && moveRight == null) {
				moveLeft();
			} else if (moveRight != null) {
				moveRight.cancel();
				moveRight = null;
		}});
		
		keyPressMap.put(right, () -> {
			game.moveRight();
			if (moveRight == null && moveLeft == null) {
				moveRight();
			} else if (moveLeft != null) {
				moveLeft.cancel();
				moveLeft = null;
		}});
		
		keyPressMap.put(hold, () -> game.hold());
		keyPressMap.put(down, () -> game.setIncSpeed(true));
		keyPressMap.put(turn_right, () -> game.turnRight());
		keyPressMap.put(turn_left, () -> game.turnLeft());
		keyPressMap.put(PAUSE, () -> game.setPause(true));
		keyPressMap.put(place, () -> game.fastPlace());
	}
	
	private void createKeyReleasedMap(Ini.Section config) {
		keyReleasedMap = new HashMap<Integer, Runnable>();
		int left = KeyStroke.getKeyStroke((String) config.get("left")).getKeyCode();
		int right = KeyStroke.getKeyStroke((String) config.get("right")).getKeyCode();
		int down = KeyStroke.getKeyStroke((String) config.get("speed_up")).getKeyCode();
		
		keyReleasedMap.put(left, () -> {
			if (moveLeft == null) { //both keys where pressed down and left released first
				moveRight();
			} else {
				moveLeft.cancel();
				moveLeft = null;
			}});
		
		keyReleasedMap.put(right, () -> {
			if (moveRight == null) { //both keys where pressed down and right released first
				moveLeft();
			} else {
				moveRight.cancel();
				moveRight = null;
			}
		});
		
		keyReleasedMap.put(down, () -> game.setIncSpeed(false));
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (game.isRuning() && !game.isPaused()) {
			int keyCode = e.getKeyCode();
			Runnable action = keyPressMap.get(keyCode);
			if (action != null) {
				action.run();
			}
		} else {
			if (e.getKeyCode() == PAUSE) {
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
			Runnable action = keyReleasedMap.get(e.getKeyCode());
			if (action != null) {
				action.run();
			}
		}
	}

}
