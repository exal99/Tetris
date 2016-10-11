package ai.gui.terminalComand;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import ai.game.MainAiGameThread;
import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.TerminalException;
import main.gui.GameGUI;

public class ShowGraphicsTerminalCommand extends AbstractTerminalCommand {
	
	private JFrame graphicsWindow;
	
	public ShowGraphicsTerminalCommand(Terminal term) {
		super(term);
		shortDesc = "Displays the current running ai in action";
		longDesc = "Creates a new window showing the current running ai";
		requierdArgs = new String[]{};
		optionalArgs = new String[]{};
		argDescriptions = new String[]{};
		graphicsWindow = null;
	}
	
	@Override
	public void excecuteCommand(String args) throws TerminalException {
		super.excecuteCommand(args);
		if (MainAiGameThread.getGraphics() == null) {
			graphicsWindow = new JFrame("Ai selection");
			graphicsWindow.addWindowListener(new ClosingWindowListener());
			GameGUI gui = new GameGUI(term.getGeneration().getGame(), term.getTimer(), 800, 600, false, false);
			graphicsWindow.add(gui);
			MainAiGameThread.setGraphics(gui);
			MainAiGameThread.setGraphicsUpdate(true);
			graphicsWindow.setSize(800, 600);
			graphicsWindow.setVisible(true);
		} else {
			term.append("Graphics already showing<br>");
		}
	}

	@Override
	public String getName() {
		return "show";
	}

	@Override
	public String getRegex() {
		return getName();
	}
	
	private class ClosingWindowListener implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {}

		@Override
		public void windowClosing(WindowEvent e) {
			MainAiGameThread.setGraphics(null);
			MainAiGameThread.setGraphicsUpdate(false);
			graphicsWindow.dispose();
			graphicsWindow = null;
			
		}

		@Override
		public void windowClosed(WindowEvent e) {}

		@Override
		public void windowIconified(WindowEvent e) {}

		@Override
		public void windowDeiconified(WindowEvent e) {}

		@Override
		public void windowActivated(WindowEvent e) {}

		@Override
		public void windowDeactivated(WindowEvent e) {}
		
	}

}
