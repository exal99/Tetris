package tetrads;


public class Gamma extends Tetrad {
	
	public Gamma() {
		super(3, 0, createOrientation("GAMMA"));
	}

	@Override
	public Tetrads getType() {
		// TODO Auto-generated method stub
		return Tetrads.GAMMA;
	}

}
