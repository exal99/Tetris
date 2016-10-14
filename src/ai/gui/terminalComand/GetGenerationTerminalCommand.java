package ai.gui.terminalComand;

import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.TerminalException;

public class GetGenerationTerminalCommand extends AbstractTerminalCommand {
	
	public GetGenerationTerminalCommand(Terminal term) {
		super(term);
		shortDesc = "Gets the current generation number";
		longDesc = shortDesc;
		requierdArgs = new String[]{};
		optionalArgs = new String[]{};
		argDescriptions = new String[]{};
	}
	
	@Override
	public void excecuteCommand(String args) throws TerminalException {
		super.excecuteCommand(args);
		term.append("Current gen: " + term.getGeneration().getGenNum() + "<br>");
	}

	@Override
	public String getName() {
		return "generation";
	}

	@Override
	public String getRegex() {
		return getName();
	}

}
