package ai.gui;

import java.util.Collections;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import ai.gui.Terminal.Mode;
/**
 * Based on the AreaTextDemo from oracle.
 * Source: http://docs.oracle.com/javase/tutorial/uiswing/components/textarea.html
 * 
 * @author Oracle
 *
 */
public class AutoCompleat implements DocumentListener {
	
	private JTextField input;
	private Terminal term;
	private List<String> words;
	
	public AutoCompleat(JTextField in, Terminal t, List<String> w) {
		input = in;
		words = w;
		term = t;
	}

	@Override
	public void insertUpdate(DocumentEvent ev) {
		if (ev.getLength() != 1) {
            return;
        }
        
        int pos = ev.getOffset();
        String content = null;
        try {
            content = input.getText(0, pos + 1);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        
        // Find where the word starts
        int w;
        for (w = pos; w >= 0; w--) {
            if (! Character.isLetter(content.charAt(w))) {
                break;
            }
        }
        if (pos - w < 2) {
            // Too few chars
        	term.setMode(Mode.TYPING);
            return;
        }
        
        String prefix = content.substring(w + 1).toLowerCase();
        int n = Collections.binarySearch(words, prefix);
        if (n < 0 && -n <= words.size()) {
            String match = words.get(-n - 1);
            if (match.startsWith(prefix)) {
                // A completion is found
                String completion = match.substring(pos - w);
                // We cannot modify Document from within notification,
                // so we submit a task that does the change later
                SwingUtilities.invokeLater(term.getNewTask(completion, pos + 1));
            }
        } else {
        	term.setMode(Mode.TYPING);
        }
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

}
