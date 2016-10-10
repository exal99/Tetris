package ai.gui.terminalComand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.InvalidUseOfTerminalCommandException;
import ai.gui.terminalComand.exceptions.TerminalException;

public abstract class AbstractTerminalCommand implements TerminalCommand {
	
	protected Terminal term;
	protected String shortDesc;
	protected String longDesc;
	protected String[] requierdArgs;
	protected String[] optionalArgs;
	protected String[] argDescriptions;
	
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
		return shortDesc;
	}

	@Override
	public String getLongDescription() {
		return longDesc + "<br><br>" + getUsage(requierdArgs, optionalArgs, argDescriptions);
	}
	
	protected String getUsage(String[] requierd, String[] optional, String[] descriptions) {
		assert (requierd.length + optional.length == descriptions.length);
		StringBuilder sb = new StringBuilder("<h3>USAGE:</h3><br>" + getName() + " ");
		sb.append(String.join(", ", requierd));
		if (optional.length != 0)
			sb.append((requierd.length > 0) ? "[, " : "[" + String.join(", ", optional) + "]<br><br>");
		for (int i = 0; i < requierd.length; i++) {
			sb.append(Terminal.getTab() + requierd[i] + " - " + descriptions[i] + "<br><br>");
		}
		for (int i = 0; i < optional.length; i++) {
			sb.append(Terminal.getTab() + optional[i] + " - " + descriptions[i + requierd.length] + "<br><br>");
		}
		return sb.toString();
	}

}
