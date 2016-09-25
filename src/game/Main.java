package game;

import java.io.File;
import java.io.IOException;
import java.util.Timer;

import javax.swing.JFrame;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import gameBoard.GameBoard;
import gui.StartMenu;
import highscore.HighScore;
import keyEvents.Controlls;
import soundPlayer.SoundPlayer;

public class Main {
	
	public static void main(String[] args) {
		Timer t = new Timer();
		GameBoard player1 = new GameBoard(t);
		GameBoard player2 = new GameBoard(t);
		JFrame root = new JFrame("Tetris");
		Wini ini = null;
		try {
			ini = new Wini(new File("config.ini"));
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		root.setFocusable(true);
		root.addKeyListener(new Controlls(player1, t, ini.get("pl1_controls")));
		root.addKeyListener(new Controlls(player2, t, ini.get("pl2_controls")));
		StartMenu menu = new StartMenu(player1, player2, root, HighScore.getScore(), args);
		root.add(menu);
		root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		root.setSize(800, 600);
		root.setVisible(true);
		SoundPlayer sound = new SoundPlayer("music.wav");
		sound.playSound();
		
	}
}
