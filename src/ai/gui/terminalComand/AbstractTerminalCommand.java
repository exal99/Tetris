package ai.gui.terminalComand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.InvalidUseOfTerminalCommandException;
import ai.gui.terminalComand.exceptions.TerminalException;

public abstract class AbstractTerminalCommand implements TerminalCommand {
	
	protected Terminal term;
	
	protected AbstractTerminalCommand(Terminal term) {
		this.term = term;
	}
	
	public abstract String getRegex();

	@Override
	public void excecuteCommand(String args) throws TerminalException {
		Pattern p = Pattern.compile(getRegex());
		Matcher m = p.matcher(args);
		if (!m.matches()) {
			throw new InvalidUseOfTerminalCommandException("Invalid use", args);
		}
	}

	@Override
	public String getShortDescription() {
		return "";
	}

	@Override
	public String getLongDescription() {
		return "";
	}
	
	protected void append(String toAppend) {
		
	}

}
