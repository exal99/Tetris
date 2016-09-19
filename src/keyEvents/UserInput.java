package keyEvents;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.KeyStroke;
import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import gameBoard.GameBoard;

public class UserInput implements KeyListener {
	
//	private HashMap<Integer, String> keyBindings;
	private GameBoard game;
	
	public UserInput(GameBoard game) {
//		File keyBindings = new File("KeyBindings.xml");
//		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//		Document doc = null;
//		try {
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			doc = dBuilder.parse(keyBindings);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(-1);
//		}
//		
//		doc.getDocumentElement().normalize();
//		String[] keys = new String[]{"Hold", "MoveLeft", "MoveRight",
//									 "RotateLeft", "RotateRight", "Place"};
//		this.keyBindings = new HashMap<Integer, String>();
//		HashMap<String, Integer> specialKeys = new HashMap<String, Integer>();
//		specialKeys.put("Up", KeyEvent.VK_UP);
//		specialKeys.put("Down", KeyEvent.VK_DOWN);
//		specialKeys.put("Left", KeyEvent.VK_LEFT);
//		specialKeys.put("Right", KeyEvent.VK_RIGHT);
//		System.out.println(KeyStroke.getKeyStroke(doc.getElementsByTagName("RotateRight").item(0).getTextContent()));
//		for (String key : keys) {
//			System.out.println(key);
//			//System.out.println(doc.getElementsByTagName(key).item(0).getTextContent());
//			KeyStroke ks = KeyStroke.getKeyStroke(doc.getElementsByTagName(key).item(0).getTextContent().toCharArray()[0], 0);
//			if (ks != null) {
//				this.keyBindings.put(ks.getKeyCode(), key);
//			} else {
//				this.keyBindings.put(specialKeys.get(key), key);
//			}
//			//this.keyBindings.put(KeyStroke.getKeyStroke(doc.getElementsByTagName(key).item(0).getTextContent()).getKeyCode(), key);
//		}
//		System.out.println(this.keyBindings.get(KeyEvent.VK_X));
		this.game = game;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println("Key typed");

	}

	@Override
	public void keyPressed(KeyEvent e) {
		
//		String action = keyBindings.get(e.getKeyCode());
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
			game.place();
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
