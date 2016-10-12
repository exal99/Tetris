package ai.gui.terminalComand;

import ai.generation.Generation;
import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.TerminalException;

public class ResumeTerminalCommand extends AbstractTerminalCommand {
	
	public ResumeTerminalCommand(Terminal term) {
		super(term);
		shortDesc = "Resumes the current running generation simulation";
		longDesc = shortDesc;
		requierdArgs = new String[]{};
		optionalArgs = new String[]{};
		argDescriptions = new String[]{};
	}
	
	@Override
	public void excecuteCommand(String args) throws TerminalException {
		super.excecuteCommand(args);
		Generation.setRunning(true);
		term.append("Resumed simulation<br>");
	}

	@Override
	public String getName() {
		return "resume";
	}

	@Override
	public String getRegex() {
		return "resume(\\s)*";
	}

}
