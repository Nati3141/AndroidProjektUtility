package de.natalie.validator.exceptions;


public class InvalidUserInputException extends Exception{

	private static final long serialVersionUID = 668623790786944127L;
	
	public InvalidUserInputException(String message) {
		super(message);
	}
	
	public InvalidUserInputException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
