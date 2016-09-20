package keyEvents;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gameBoard.GameBoard;

public class UserInput implements KeyListener {
	
	private GameBoard game;
	
	public UserInput(GameBoard game) {
		this.game = game;
	}

	@Override
	public void keyTyped(KeyEvent e) {
//		System.out.println("Key typed");

	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			game.hold();
			break;
		case KeyEvent.VK_LEFT:
			game.moveLeft();
			break;
		case KeyEvent.VK_RIGHT:
			game.moveRight();
			break;
		case KeyEvent.VK_Z:
			game.turnLeft();
			break;
		case KeyEvent.VK_X:
			game.turnRight();
			break;
		case KeyEvent.VK_DOWN:
			game.fastPlace();
			break;
		default:
			System.out.println("Unrecognized key: " + e.getKeyChar());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
