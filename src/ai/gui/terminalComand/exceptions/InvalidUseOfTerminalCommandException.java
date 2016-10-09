package ai.gui.terminalComand.exceptions;

public class InvalidUseOfTerminalCommandException extends TerminalException{

	private static final long serialVersionUID = 8953032179365691626L;
	
	public InvalidUseOfTerminalCommandException() {super();}
	public InvalidUseOfTerminalCommandException(String userUse) {super(userUse);}
	public InvalidUseOfTerminalCommandException(String msg, String userUse) {super(msg, userUse);}
	public InvalidUseOfTerminalCommandException(String msg, Throwable cause, String userUse) {super(msg, cause, userUse);}
	public InvalidUseOfTerminalCommandException(Throwable cause, String userUse) {super(cause, userUse);}
	
}
