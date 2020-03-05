package de.natalie.validator.exceptions;

/**
 * Exception, die bei ungültiger Syntax geworfen wird
 * @author Natalie Bestler
 *
 */
public class FailedArithmeticRuleException extends Exception{

	private static final long serialVersionUID = 3475796503777059539L;
	
	public FailedArithmeticRuleException(String message) {
		super(message);
	}
	
	public FailedArithmeticRuleException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
