package ai.gui.terminalComand.exceptions;

/**
 * An Exception signaling that the user inputed a invalid command or usage of one.
 * @author Alexander
 *
 */
public class TerminalException extends Exception {

	private static final long serialVersionUID = -2495589130706779483L;
	private String userInput;
	
	public TerminalException() {super(); userInput="";}
	public TerminalException(String userUse) {super();userInput=userUse;}
	public TerminalException(String msg, String userUse) {super(msg); userInput=userUse;}
	public TerminalException(String msg, Throwable cause, String userUse) {super(msg, cause);userInput=userUse;}
	public TerminalException(Throwable cause, String userUse) {super(cause);userInput=userUse;}
	
	public String getUserUse() {
		return userInput;
	}

}
