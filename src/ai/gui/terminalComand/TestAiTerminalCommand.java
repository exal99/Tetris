package ai.gui.terminalComand;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Arrays;

import javax.swing.JFrame;

import ai.ai.Ai;
import ai.aiGameBoard.AiGameBoard;
import ai.game.MainAiGameThread;
import ai.generation.Generation;
import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.TerminalException;
import main.gui.GameGUI;

public class TestAiTerminalCommand extends AbstractTerminalCommand {
	
	private JFrame graphicsWindow;
	
	public TestAiTerminalCommand(Terminal term) {
		super(term);
		shortDesc = "Tests a given ai";
		longDesc = "Runs a full game with a AI with the given constants as its genotype.";
		requierdArgs = new String[]{"height-multiplyer", "roughness-multiplyer", "holes-multiplyer", "blocking-multiplyer", "score-gain-multiplyer"};
		optionalArgs = new String[]{};
		argDescriptions = new String[]{"The hight multiplyer for the AI", "The roughness multiplyer for the AI", "The holes multiplyer for the AI", "The blocking multiplyer for the AI", "The score gain multiplyer for the AI",};
	}
	
	@Override
	public void excecuteCommand(String args) throws TerminalException {
		super.excecuteCommand(args);
		if (MainAiGameThread.getGraphics() == null) {
			String[] splitArgs = args.replaceAll("\\[|\\]", "").replaceAll(",", "").split(" ");
			System.out.println(Arrays.asList(splitArgs).toString());
			double[] genome = new double[5];
			for (int i = 1; i < splitArgs.length; i++) {
				genome[i-1] = Double.parseDouble(splitArgs[i]);
			}
			Ai ai = new Ai(new AiGameBoard(term.getTimer()), genome);
			Thread thread = new Thread(() -> {Generation.play(ai); Generation.setRunning(false);});
			thread.start();
			ai.getGame().start();
			Generation.setRunning(true);
			graphicsWindow = new JFrame("Ai selection");
			graphicsWindow.addWindowListener(new ClosingWindowListener());
			GameGUI gui = new GameGUI(ai.getGame(), term.getTimer(), 800, 600, false, false);
			graphicsWindow.add(gui);
			MainAiGameThread.setGraphics(gui);
			MainAiGameThread.setGraphicsUpdate(true);
			graphicsWindow.setSize(800, 600);
			graphicsWindow.setVisible(true);
		}
		
	}

	@Override
	public String getName() {
		return "testai";
	}

	@Override
	public String getRegex() {
		StringBuilder sb = new StringBuilder("testai\\s(\\[)?");
		String decNumRegex = "(-)?\\d*(\\.\\d*)?";
		for (int i = 0; i < 5; i++) {
			sb.append(decNumRegex + ",\\s");
		}
		sb.replace(sb.length() - 3, sb.length(), "");
		sb.append("(\\s\\])?");
		System.out.println(sb);
		return sb.toString();
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
