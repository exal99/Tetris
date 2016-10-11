package ai.gui.terminalComand;

import ai.generation.Generation;
import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.TerminalException;

public class CreateNewGenerationTerminalCommand extends AbstractTerminalCommand {

	public CreateNewGenerationTerminalCommand(Terminal term) {
		super(term);
		shortDesc = "Creates a new generation of ai:s";
		longDesc = "Creates a new generation of ai:s based on the given genration size and the interval for witch the ai:s can be generated from";
		requierdArgs = new String[] {};
		optionalArgs = new String[] {};
		argDescriptions = new String[] {};
	}
	
	@Override
	public void excecuteCommand(String args) throws TerminalException {
		super.excecuteCommand(args);
		term.setGeneration(new Generation(term.getTimer()));
		term.append("Succefully created new generation<br>");
	}
	
	@Override
	public String getName() {
		return "new";
	}

	@Override
	public String getRegex() {
		return getName();
	}

}
