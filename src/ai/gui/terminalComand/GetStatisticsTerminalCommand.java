package ai.gui.terminalComand;

import ai.generation.Generation;
import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.TerminalException;

public class GetStatisticsTerminalCommand extends AbstractTerminalCommand {
	
	public GetStatisticsTerminalCommand(Terminal term) {
		super(term);
		shortDesc = "Prints all statistics on the current generation";
		longDesc = shortDesc;
		requierdArgs = new String[]{};
		optionalArgs = new String[]{};
		argDescriptions = new String[]{};
	}
	
	@Override
	public void excecuteCommand(String args) throws TerminalException {
		super.excecuteCommand(args);
		Generation gen = term.getGeneration();
		StringBuilder sb = new StringBuilder("<br><table style=\"width:100%\"><th colspan=\"2\">Statistics</th>");
		sb.append("<tr><td>Generation number</td><td colspan=\"2\">" + gen.getGenNum() + "</td></tr>");
		sb.append("<tr><td>Current Generation's Best AI</td><td>" + ((gen.getBestAi() != null) ? gen.getBestAi().toString() : "null") + "</td><td>" + gen.getBestScore() + "</td></tr>");
		sb.append("<tr><td>All Time Best AI</td><td>" + ((gen.getAllTimeBestAi() != null) ? gen.getAllTimeBestAi().toString() : "null")+ "</td><td>" + gen.getAllTimeBestScore() +  "</td></tr></table><br><br>");
		term.append(sb.toString());
	}

	@Override
	public String getName() {
		return "statistics";
	}

	@Override
	public String getRegex() {
		return getName();
	}

}
