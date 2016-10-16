package ai.gui.terminalComand;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import ai.ai.Ai;
import ai.generation.Generation;
import ai.gui.Terminal;
import ai.gui.terminalComand.exceptions.TerminalException;

public class LoadTerminalCommand extends AbstractTerminalCommand {
	
	public LoadTerminalCommand(Terminal term) {
		super(term);
		shortDesc = "Loads a specific generation";
		longDesc = "Loads a given generation file. If no file is given a file explorer will open";
		requierdArgs = new String[]{};
		optionalArgs = new String[]{"file-path"};
		argDescriptions = new String[]{"path to the generation file"};
	}
	
	@Override
	public void excecuteCommand(String args) throws TerminalException {
		super.excecuteCommand(args);
		if (Pattern.matches("load\\s^(.*/)([^/]*)", args)) {
			load(args.split(" ")[1]);
		} else {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Generation files", "gen");
			chooser.setFileFilter(filter);
			if (chooser.showOpenDialog(term) == JFileChooser.APPROVE_OPTION) {
				load(chooser.getSelectedFile());
			}
		}
	}
	
	private void load(String path) {
		load(new File(path));
	}
	
	private void load(File f) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Generation.class, new Generation.GenerationDeserializer());
		builder.registerTypeAdapter(Ai.class, new Ai.AiDeserializer());
		Gson json = builder.create();
		try {
			InputStreamReader jsonFile = new InputStreamReader(new FileInputStream(f));
			JsonReader reader = new JsonReader(jsonFile);
			reader.setLenient(true);
			JsonObject jObject = new JsonParser().parse(reader).getAsJsonObject();
			Generation gen = json.fromJson(jObject, Generation.class);
			term.setGeneration(gen);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

	@Override
	public String getName() {
		return "load";
	}

	@Override
	public String getRegex() {
		return "load(\\s^(.*/)([^/]*))?";
	}

}
