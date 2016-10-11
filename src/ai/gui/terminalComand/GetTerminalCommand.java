package ai.gui.terminalComand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.TerminalException;

public class GetTerminalCommand extends AbstractTerminalCommand{
	
	public GetTerminalCommand(Terminal term) {
		super(term);
		shortDesc = "Gets a given property's value";
		longDesc = shortDesc;
		requierdArgs = new String[]{"property"};
		optionalArgs = new String[0];
		argDescriptions = new String[]{"The property to get the value for"};
	}
	
	@Override
	public void excecuteCommand(String args) throws TerminalException {
		super.excecuteCommand(args);
		Pattern p = Pattern.compile("%(\\w)+%");
		Matcher m = p.matcher(args);
		String key = "";
		if (m.matches()) {
			key = args.substring(1, args.lastIndexOf('%'));
		} else {
			String[] splitString = args.split(" ");
			key = splitString[1];
		}
		term.append(key + ": " + System.getProperty(key) + "<br>");
	}
	

	@Override
	public String getName() {
		return "get";
	}

	@Override
	public String getRegex() {
		return "(get\\s(\\w)+)|(%(\\w)+%)";
	}

}
