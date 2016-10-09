package ai.gui.terminalComand;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.TerminalCommandNotFoundException;
import ai.gui.terminalComand.exceptions.TerminalException;

public final class HelpTerminalCommand extends AbstractTerminalCommand {
	
	private HashMap<String, TerminalCommand> commands;
	
	public HelpTerminalCommand(Terminal t, HashMap<String, TerminalCommand> coms) {
		super(t);
		commands = coms;
	}
	
	@Override
	public final String getRegex() {
		return "help(\\s[\\w]*)?";
	}
	
	@Override
	public void excecuteCommand(String args) throws TerminalException{
		super.excecuteCommand(args);
		Pattern p = Pattern.compile("help\\s[\\w]+");
		Matcher m = p.matcher(args);
		if (m.matches()) {
			TerminalCommand commandToGetHelp = commands.get(args.split(" ")[1]);
			if (commandToGetHelp != null) {
				term.append(getHelpOnCommand(commandToGetHelp));
			} else {
				throw new TerminalCommandNotFoundException("No Command Named: " + args.split(" ")[1], args);
			}
		} else {
			term.append(getHelp());
		}
	}
	
	private String getHelpOnCommand(TerminalCommand t) {
		String rString = ""
				+ t.getName() + ":<br>" + t.getLongDescription();
		return rString;
	}
	
	private String getHelp() {
		StringBuilder rString = new StringBuilder(""
				+ "<table style=\"width:100%\">"
				+ "<tr><th>COMMAND</th>"
				+ "<th>DESCRIPTION</th></tr>");
		for (String com : commands.keySet()) {
			rString.append("<tr><td>" + commands.get(com).getName() + ":</td><td>" + commands.get(com).getShortDescription() + "</td></tr>");
		}
		rString.append("</table><br><br>");
		return rString.toString();
	}

	@Override
	public String getShortDescription() {
		return "Provides a list of commands";
	}

	@Override
	public String getLongDescription() {
		String rString = "" 
				+ "Provides help information for a given command or a list of all commands<br><br>"
				+ "<h3>USAGE:</h3><br>"
				+ "help [command]<br><br>"
				+ Terminal.getTab() + "command - display help information on that command<br><br>";
		return rString;
	}
	
	@Override
	public String getName() {
		return "help";
	}

}
