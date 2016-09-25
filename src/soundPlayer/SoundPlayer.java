package soundPlayer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {
	private String filePath;
	private Clip clip;
	
	public SoundPlayer(String path) {
		filePath = path;
	}
	
	public void playSound() {
		try {
//			InputStream audioIn = getClass().getResourceAsStream("/" + filePath);
			InputStream audioIn = new FileInputStream(filePath);
			InputStream buff = new BufferedInputStream(audioIn);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(buff);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}
