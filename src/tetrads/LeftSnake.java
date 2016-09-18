package tetrads;


public class LeftSnake extends Tetrad {
	
	public LeftSnake() {
		super(3, 0, createOrientation("LEFT_SNAKE"));
	}

	@Override
	public Tetrads getType() {
		// TODO Auto-generated method stub
		return Tetrads.LEFT_SNAKE;
	}

}
