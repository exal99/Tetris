package ai.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

public class Terminal extends JPanel {

	private static final long serialVersionUID = -2607615191596287587L;
	private static final Dimension STANDARD_TEXT_PANE_DIM = new Dimension(480, 336);
	private static final Dimension MAX_INPUT_DIM = new Dimension(Integer.MAX_VALUE, 21);
	
	public static final int STANDARD_WINDOW_WIDTH = 700;
	public static final int STANDARD_WINDOW_HEIGHT = 400;
	
	
	private JTextPane text;
//	private JTextArea text;
	private JTextField input;
	private ArrayList<String> lastInput;
	private int prevInput;
	
	public Terminal() {
		Font font = null;
//		text = new JTextArea(24, 80);
		text = new JTextPane();
		input = new JTextField();
		lastInput = new ArrayList<String>();
		setPreferredSize(new Dimension(STANDARD_WINDOW_WIDTH, STANDARD_WINDOW_HEIGHT));
		text.setPreferredSize(STANDARD_TEXT_PANE_DIM);
		input.setMaximumSize(MAX_INPUT_DIM);
		input.setMinimumSize(MAX_INPUT_DIM);
		input.setPreferredSize(MAX_INPUT_DIM);
		
		text.setEditable(false);
		text.setContentType("text/html");
		
		input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lastInput.add(input.getText());
//				text.append(input.getText() + "\n");
				append(input.getText() + "\n");
				prevInput = lastInput.size() - 1;
				input.setText("");
			}
		});
		
		input.getInputMap().put(KeyStroke.getKeyStroke("UP"), "recall");
		input.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "derecall");
		input.getActionMap().put("recall", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (prevInput < lastInput.size() && prevInput >= 0) {
					input.setText(lastInput.get(prevInput));
					prevInput--;
				}
			}
		});
		input.getActionMap().put("derecall", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(prevInput);
				if (prevInput + 2 < lastInput.size()) {
					input.setText(lastInput.get(prevInput + 2));
					prevInput++;
				} else if (prevInput + 1 < lastInput.size()) {
					input.setText("");
					prevInput = lastInput.size() - 1;
				}
			}
		});
		
		try {
			InputStream in = Terminal.class.getResourceAsStream("/fonts/terminal.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, in);
			font = font.deriveFont(10f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
		}
		prevInput = 0;
		
		if (font != null) {
			text.setFont(font);
			input.setFont(font);
		}
		JScrollPane scrollPane = new JScrollPane(text);
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		input.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(scrollPane);
		add(input);
		
		String fontFamily = font.getFamily();
		text.setText(String.format("<html><style>"+
		"body {font-family: \"%s\"; font-size: \"%2$d\"}" +
		 "</style></html>", fontFamily, font.getSize()));
		
	}
	
	public Dimension getTextDim() {
		return text.getSize();
	}
	
	public Dimension getInputDim() {
		return input.getSize();
	}
	
	public void append(String toAppend) {
		HTMLDocument doc = (HTMLDocument) text.getStyledDocument();
		try {
			doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()), toAppend.replaceAll("\n", "<br>").replaceAll("\t", getTab()));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printStackTrace(String trace) {
//		text.append(trace);
		append("<font color=\"red\">" + trace + "</font>");
	}
	
	public String getTab() {
		return "&nbsp;&nbsp;&nbsp;&nbsp;";
	}
}
