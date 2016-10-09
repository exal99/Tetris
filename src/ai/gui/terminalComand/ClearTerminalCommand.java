package ai.gui.terminalComand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.TerminalException;

public class ClearTerminalCommand extends AbstractTerminalCommand {
	
	private String command;
	private final static String[] allowedCommands = new String[]{"cls", "clear"};
	
	public ClearTerminalCommand(Terminal t, String com) {
		super(t);
		boolean res = false;
		for (String allowed : allowedCommands) {
			Pattern p = Pattern.compile(allowed);
			Matcher m = p.matcher(com);
			res = res || m.matches();
		}
		assert res;
		command = com;
	}

	@Override
	public String getName() {
		return command;
	}

	@Override
	public String getRegex() {
		return command;
	}
	
	@Override
	public void excecuteCommand(String args) throws TerminalException {
		super.excecuteCommand(args);
		term.clear();
	}
	
	@Override
	public String getShortDescription() {
		return "Clears the screen";
	}
	
	@Override
	public String getLongDescription() {
		return "Clears the screen<br><br>"
				+ "<h3>USAGE:</h3><br>"
				+ command + "<br><br>";
	}

}
