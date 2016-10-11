package ai.gui.terminalComand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.TerminalException;

public class SetTerminalCommand extends AbstractTerminalCommand {

	public SetTerminalCommand(Terminal term) {
		super(term);
		shortDesc = "Sets a given property to a new value";
		longDesc = shortDesc;
		requierdArgs = new String[]{"property", "value"};
		optionalArgs = new String[0];
		argDescriptions = new String[]{"The property to set the value for", "The new value of the property"};
	}
	
	@Override
	public void excecuteCommand(String args) throws TerminalException {
		super.excecuteCommand(args);
		Pattern p = Pattern.compile("(%(\\w)+%(\\s)*=(\\s)*(\\w)+)");
		Matcher m = p.matcher(args);
		String key = "";
		String value = "";
		if (m.matches()) {
			String[] splitString = args.split("=");
			String before = splitString[0];
			String after = splitString[1];
			key = before.substring(1, before.lastIndexOf('%'));
			value = after;
		} else {
			String[] splitString = args.split(" ");
			key = splitString[1];
			value = splitString[2];
		}
		System.setProperty(key.replaceAll("\\s", ""), value.replaceAll("\\s", ""));
	}
	

	@Override
	public String getName() {
		return "set";
	}

	@Override
	public String getRegex() {
		return "(set\\s(\\w)+\\s(\\w|-)+)|(%(\\w)+%(\\s)*=(\\s)*(\\w|-)+)";
	}

}
