package ai.gui.terminalComand;

import ai.generation.Generation;
import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.TerminalException;

public class PauseTerminalCommand extends AbstractTerminalCommand {
	
	public PauseTerminalCommand(Terminal term) {
		super(term);
		shortDesc = "Pauses the currently running generation simulation";
		longDesc = shortDesc;
		requierdArgs = new String[]{};
		optionalArgs = new String[]{};
		argDescriptions = new String[]{};
	}
	
	@Override
	public void excecuteCommand(String args) throws TerminalException {
		super.excecuteCommand(args);
		Generation.setRunning(false);
		term.append("Simulation paused<br>");
	}

	@Override
	public String getName() {
		return "pause";
	}

	@Override
	public String getRegex() {
		return "pause(\\s)*";
	}

}
