package ai.gui.terminalComand;

import ai.gui.terminalComand.exceptions.TerminalException;

public interface TerminalCommand {
	
	public void excecuteCommand(String args) throws TerminalException;
	public String getShortDescription();
	public String getLongDescription();
	public String getName();
}
