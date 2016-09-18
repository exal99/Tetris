package gameBoard;

import java.util.TimerTask;

public class PlaceTimerTask extends TimerTask{
	
	private MethodRunner m;
	
	public PlaceTimerTask(MethodRunner m) {
		super();
		this.m = m;
	}

	@Override
	public void run() {
		m.run();
	}
	
	public interface MethodRunner {
		public void run();
	}

}
