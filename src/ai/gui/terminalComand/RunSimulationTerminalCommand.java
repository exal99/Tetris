package ai.gui.terminalComand;

import ai.generation.Generation;
import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.TerminalException;

public class RunSimulationTerminalCommand extends AbstractTerminalCommand {
	
	public RunSimulationTerminalCommand(Terminal term) {
		super(term);
		shortDesc = "Runs the simulation a given amout of generations";
		longDesc = shortDesc;
		requierdArgs = new String[]{"timesToRun"};
		optionalArgs = new String[]{};
		argDescriptions = new String[]{"the amount of generations to run the simulation for"};
	}
	
	@Override
	public void excecuteCommand(String args) throws TerminalException {
		super.excecuteCommand(args);
		Generation gen = term.getGeneration();
		int numTimes = Integer.parseInt(args.split(" ")[1]);
		Thread aiThread = new Thread(() -> {gen.runSimulation(numTimes); term.append("Done!<br>");});
		aiThread.start();
	}

	@Override
	public String getName() {
		return "run";
	}

	@Override
	public String getRegex() {
		return "run\\s(\\d)+";
	}

}
