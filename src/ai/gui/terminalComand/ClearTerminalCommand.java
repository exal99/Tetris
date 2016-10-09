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
		shortDesc = "Clears the screen";
		longDesc = shortDesc;
		requierdArgs = new String[0];
		optionalArgs = new String[0];
		argDescriptions = new String[0];
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

}
