package ai.gui.terminalComand.exceptions;

public class TerminalCommandNotFoundException extends TerminalException {

	private static final long serialVersionUID = -5455453077568863899L;
	
	public TerminalCommandNotFoundException() {super();}
	public TerminalCommandNotFoundException(String userUse) {super(userUse);}
	public TerminalCommandNotFoundException(String msg, String userUse) {super(msg, userUse);}
	public TerminalCommandNotFoundException(String msg, Throwable cause, String userUse) {super(msg, cause, userUse);}
	public TerminalCommandNotFoundException(Throwable cause, String userUse) {super(cause, userUse);}
}
