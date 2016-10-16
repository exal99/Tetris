package ai.gui.terminalComand;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ai.ai.Ai;
import ai.generation.Generation;
import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.TerminalException;

public class SaveTerminalCommand extends AbstractTerminalCommand {
	
	public SaveTerminalCommand(Terminal term) {
		super(term);
		shortDesc = "Saves the current generation to a file";
		longDesc = "Saves the current generation to a given file. If no file is given a file explorer promt will be opened";
		requierdArgs = new String[]{};
		optionalArgs = new String[]{"file-path"};
		argDescriptions = new String[]{"path to save the file to"};
	}
	
	@Override
	public void excecuteCommand(String args) throws TerminalException {
		super.excecuteCommand(args);
		if (Pattern.matches("save\\s^(.*/)([^/]*)", args)) {
			save(args.split(" ")[1]);
		} else {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Generation files", "json");
			chooser.setFileFilter(filter);
			if (chooser.showSaveDialog(term) == JFileChooser.APPROVE_OPTION) {
				save(chooser.getSelectedFile());
				
			}
		}
		
	}
	
	private void save(String path) {
		save(new File(path));
	}
	
	private void save(File f) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Generation.class, new Generation.GenerationSerializer());
		builder.registerTypeAdapter(Ai.class, new Ai.AiSerializer());
		builder.setPrettyPrinting();
		Gson json = builder.create();
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream(f), true);
			writer.println(json.toJson(term.getGeneration()));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "save";
	}

	@Override
	public String getRegex() {
		return "save(\\s^(.*/)([^/]*))?";
	}

}
