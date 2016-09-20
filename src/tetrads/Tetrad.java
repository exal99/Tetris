package tetrads;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class Tetrad {
	
	private int xPos;
	private int yPos;
	protected boolean[][] orientation;
	
	protected Tetrad(int xPos, int yPos, String type) {
		this.xPos = xPos;
		this.yPos = yPos;
		orientation = createOrientation(type, getClass().getResourceAsStream("/Tetrads.xml"));
	}
	
	public void copyAll(Tetrad t) {
		xPos = t.xPos;
		yPos = t.yPos;
		orientation = t.orientation;
	}
	
	protected static boolean[][]  createOrientation(String type, InputStream in) {
//		File xmlTetrads = new File("Tetrads.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		Document doc = null;
//		InputStream in = type.getClass().getResourceAsStream("Tetrads.xml");
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(in);
//		} catch (FileNotFoundException e) { 
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			doc = dBuilder.parse(new File());
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
}
