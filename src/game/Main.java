package game;

import java.util.Timer;

import javax.swing.JFrame;

import gameBoard.GameBoard;
import gui.StartMenu;
import highscore.HighScore;
import keyEvents.PlayerOneKeyListener;
import keyEvents.PlayerTwoKeyListener;
import soundPlayer.SoundPlayer;

public class Main {
	
	public static void main(String[] args) {
		Timer t = new Timer();
		GameBoard player1 = new GameBoard(t);
		GameBoard player2 = new GameBoard(t);
		JFrame root = new JFrame("Tetris");
		root.setFocusable(true);
		root.addKeyListener(new PlayerOneKeyListener(player1, player1.getTimer()));
		root.addKeyListener(new PlayerTwoKeyListener(player2, player2.getTimer()));
		StartMenu menu = new StartMenu(player1, player2, root, HighScore.getScore(), args);
		root.add(menu);
		root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		root.setSize(800, 600);
		root.setVisible(true);
		SoundPlayer sound = new SoundPlayer("music.wav");
		sound.playSound();
		
	}
}
