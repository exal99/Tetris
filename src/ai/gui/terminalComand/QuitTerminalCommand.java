package ai.gui.terminalComand;

import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.TerminalException;

public class QuitTerminalCommand extends AbstractTerminalCommand {
	

	public QuitTerminalCommand(Terminal term) {
		super(term);
		shortDesc = "Quits the Ai Terminal";
		longDesc = shortDesc;
		requierdArgs = new String[0];
		optionalArgs = new String[0];
		argDescriptions = new String[0];
	}

	@Override
	public String getName() {
		return "quit";
	}

	@Override
	public String getRegex() {
		return "quit";
	}
	
	@Override
	public void excecuteCommand(String args) throws TerminalException {
		super.excecuteCommand(args);
		term.quit();
	}
	

}
