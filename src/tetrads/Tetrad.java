package tetrads;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class Tetrad {
//	STRAIGHT (3, 0, createOrientation("STRAIGHT")),
//	SQUARE (3, 0, createOrientation("SQUARE")),
//	T_TURN (3, 0, createOrientation("T_TURN")),
//	RIGHT_SNAKE (3, 0, createOrientation("RIGHT_SNAKE")),
//	LEFT_SNAKE (3, 0, createOrientation("LEFT_SNAKE")),
//	GAMMA (3, 0, createOrientation("GAMMA")),
//	ALPHA (3, 0, createOrientation("ALPHA"));
	
	private int xPos;
	private int yPos;
	private boolean[][] orientation;
	
	protected Tetrad(int xPos, int yPos, boolean[][] orien) {
		this.xPos = xPos;
		this.yPos = yPos;
		orientation = orien;
	}
	
	public void copyAll(Tetrad t) {
		xPos = t.xPos;
		yPos = t.yPos;
		orientation = t.orientation;
	}
	
	protected static boolean[][]  createOrientation(String type) {
		File xmlTetrads = new File("Tetrads.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xmlTetrads);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		doc.getDocumentElement().normalize();
		
		String[] labelNames = new String[] {"a", "b", "c", "d"};
		Node node = null;
		
		boolean[][] orien = null;
		
		switch (type) {
		case "STRAIGHT":
			orien = new boolean[4][4];
			node = doc.getElementsByTagName("Straight").item(0);
			break;
		case "SQUARE":
			orien = new boolean[3][3];
			node = doc.getElementsByTagName("Square").item(0);
			break;
		case "T_TURN":
			orien = new boolean[3][3];
			node = doc.getElementsByTagName("T_turn").item(0);
			break;
		case "RIGHT_SNAKE":
			orien = new boolean[3][3];
			node = doc.getElementsByTagName("Right_snake").item(0);
			break;
		case "LEFT_SNAKE":
			orien = new boolean[3][3];
			node = doc.getElementsByTagName("Left_snake").item(0);
			break;
		case "GAMMA":
			orien = new boolean[3][3];
			node = doc.getElementsByTagName("Gamma").item(0);
			break;
		case "ALPHA":
			orien = new boolean[3][3];
			node = doc.getElementsByTagName("Alpha").item(0);
			break;
		default:
			throw new IllegalArgumentException("'" + type + "' is not a valid type");
		}
		for (String label : labelNames) {
			setOrientation(((Element) node).getElementsByTagName(label).item(0).getTextContent(), orien);
		}
		return orien;
	}
	
	private static void setOrientation(String pos, boolean[][] orientation) {
		String[] a = pos.split(", ");
		orientation[Integer.parseInt(a[0])][Integer.parseInt(a[1])] = true;
	}
	
	public void rotateLeft() {
		int maxRow = orientation.length;
		int maxCol = orientation[0].length;
		boolean[][] rotated = new boolean[maxRow][maxCol];
		for (int row = 0; row < orientation.length; row++) {
			for (int col = 0; col < orientation[0].length; col++) {
				rotated[col][maxCol-row-1] = orientation[row][col];
			}
		}
		
		orientation = rotated;
	}
	
	public void rotateRight() {
		int maxRow = orientation.length;
		int maxCol = orientation[0].length;
		boolean[][] rotated = new boolean[maxRow][maxCol];
		for (int row = 0; row < orientation.length; row++) {
			for (int col = 0; col < orientation[0].length; col++) {
				rotated[maxRow-col-1][row] = orientation[row][col];
			}
		}
		
		orientation = rotated;
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public boolean colide(int x, int y) {
		//return y < orientation.length + yPos && x < orientation[0].length + xPos &&
		return xPos <= x && x < xPos + orientation[0].length &&
			   yPos <= y && y < yPos + orientation.length &&
			   orientation[y - yPos][x - xPos];
	}
	
	public void fall() {
		yPos++;
	}
	
	public void moveLeft() {
		xPos--;
	}
	
	public void moveRight() {
		xPos++;
	}
	
	public void moveUp() {
		yPos--;
	}
	
	
	public boolean[][] getOrientation() {
		return orientation;
	}
	
	public abstract Tetrads getType();
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int row = 0; row < orientation.length; row++) {
			for (int col = 0; col < orientation[0].length; col++) {
				if (orientation[row][col]) {
					sb.append("o");
				} else {
					sb.append(" ");
				}
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		Tetrad t = new Square();
		Tetrad s = new Straight();
		System.out.println(t);
		System.out.println(s);
		s.rotateLeft();
		System.out.println(s);
//		Tetrad t = Tetrad.SQUARE;
//		Tetrad Straight = Tetrad.STRAIGHT;
//		System.out.println(t);
//		t.rotateLeft();
//		System.out.println(t);
//		System.out.println(Straight);
//		Straight.rotateLeft();
//		System.out.println(Straight);
//		System.out.println(t.colide(3, 0));
//		System.out.println(t.colide(4, 0));
//		System.out.println(t.colide(1000, 1000));
	}
}
