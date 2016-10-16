package ai.main;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ai.gui.Terminal;

public class Main implements Thread.UncaughtExceptionHandler{
	
	private Terminal term;
	
	public Main(Terminal t) {
		term = t;
	}

	
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		handle(e);
	}
	
	public void handle(Throwable e) {
		try {
			if (e instanceof RuntimeException) {
				StringWriter sw = new StringWriter();
				PrintWriter pw =  new PrintWriter(sw, true);
				e.printStackTrace(pw);
				term.printStackTrace(sw.getBuffer().toString());
			} else {
				e.printStackTrace();
				System.exit(-1);
			}
		} catch (Throwable err) {
			err.printStackTrace();
			System.exit(-1);
		}
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Terminal t = new Terminal();
		Thread.setDefaultUncaughtExceptionHandler(new Main(t));
		System.setProperty("sun.awt.exception.handler", Main.class.getName());
		JFrame root = new JFrame("AI Selection Control Terminal");
		root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		root.add(t);
		root.setSize(Terminal.STANDARD_WINDOW_WIDTH, Terminal.STANDARD_WINDOW_HEIGHT);
		root.setVisible(true);	
	}
}
