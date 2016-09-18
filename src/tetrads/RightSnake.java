package tetrads;


public class RightSnake extends Tetrad{
	
	public RightSnake() {
		super(3, 0, createOrientation("RIGHT_SNAKE"));
	}

	@Override
	public Tetrads getType() {
		// TODO Auto-generated method stub
		return Tetrads.RIGHT_SNAKE;
	}
	
}
