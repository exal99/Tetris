package ai.gui.terminalComand;

import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.TerminalException;

public class QuitTerminalCommand extends AbstractTerminalCommand {

	public QuitTerminalCommand(Terminal term) {
		super(term);
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
	public String getShortDescription() {
		return "Quits the Ai Terminal";
	}
	
	@Override
	public String getLongDescription() {
		return getShortDescription() + "<br><br>"
				+ "<h3>USAGE:</h3><br><br>"
				+ "quit<br><br>";
	}
	
	@Override
	public void excecuteCommand(String args) throws TerminalException {
		super.excecuteCommand(args);
		term.quit();
	}
	

}
