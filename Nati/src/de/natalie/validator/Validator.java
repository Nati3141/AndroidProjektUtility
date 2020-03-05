package de.natalie.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.natalie.validator.exceptions.FailedArithmeticRuleException;
import de.natalie.validator.exceptions.InvalidUserInputException;

public class Validator {
	
	private List<Operator> operators;
	
	/**
	 * 
	 * @param userInput Eine Formel, welche aus {@link Operator} besteht, die mit einem Leerzeichen vonneinander getrennt sind
	 * @throws InvalidUserInputException
	 */
	public Validator(String userInput){
		operators = convertStringInputToOperators(userInput);
	}
	
	public void checkUserInput() throws InvalidUserInputException{
		try {
			assertArithmeticRules();
		}
		catch (FailedArithmeticRuleException ex) {
			throw new InvalidUserInputException("Fehlerhafte Eingabe!", ex);
		}
	}
	
	private List<Operator> convertStringInputToOperators(String userInput){
		List<Operator> operatorList = new ArrayList<>();
		
		for(String input : splitUserInput(userInput)) {
			operatorList.add(Operator.getByIdentifier(input));
		}
		
		return operatorList;
	}
	
	private List<String> splitUserInput(String userInput){		
		return Arrays.asList(userInput.split("\\s+"));
	}
	
	private void assertArithmeticRules() throws FailedArithmeticRuleException{
		assertEqualOpenAndClosedBracketAmount();
		
		for(int index = 0; index < operators.size(); index++) {
			Operator operator = operators.get(index);
			assertOperatorRules(operator, index);
		}
	}
	
	private void assertEqualOpenAndClosedBracketAmount () throws FailedArithmeticRuleException {
		int differenceBrackets = 0;
		
		for(int index = 0; index < operators.size(); index++) {
			Operator operator = operators.get(index);
			
			if (operator == Operator.BRACKET_OPEN) {
				differenceBrackets++;
			}
			
			else if (operator == Operator.BRACKET_CLOSE) {
				differenceBrackets--;
				
				if (differenceBrackets < 0) {
					throw new FailedArithmeticRuleException("Mehr schließende Klammern als offene Klammern an Stelle " + (index + 1));
				}
			}
		}
	
		if (differenceBrackets > 0) {
			throw new FailedArithmeticRuleException("Der Aursdruck enthält " + differenceBrackets + " zu viele offene Klammern.");
		}
	}
	
	private void assertOperatorRules(Operator operator, int index) throws FailedArithmeticRuleException{
		switch(operator) {
			case BRACKET_OPEN:
				assertBracketOpenRules(index);
				return;
			case BRACKET_CLOSE:
				assertBracketCloseRules(index);
				return;
			case AND:
			case OR:
				assertAndOrOrRules(index, operator);
				return;
			case NOT:
				assertNotRules(index);
				return;
			case VARIABLE:
				assertVariableRules(index);
				return;
			case IMPLICIT:
			case EQUIVALENCE:
				//TODO Implicit und equivalence entwickeln
				throw new UnsupportedOperationException("Nati hatte noch keine Zeit das zu entwicklen :)");
		}
	}
	
	//TODO Fehlernachrichten anpassen
	//TODO Restliche Methoden kommentieren
	
	
	/**
	 * Prüfe Regeln, bis zum aktuellen Index, die eine offene Klammer erfüllen muss
	 * @param index Die Position der offenen Klammer
	 * @throws FailedArithmeticRuleException Wenn eine Variable direkt vor der Klammer steht
	 */
	private void assertBracketOpenRules(int index) throws FailedArithmeticRuleException{		
		//Wenn erstes Zeichen, dann alles okay
		if(index == 0) {
			return;
		}
		
		//Ansonsten prüfen, ob vorheriges Zeichen eine Variable ist -> Wenn ja, dann Exception
		Operator previousOperator = getPreviousOperator(index);
		
		if(previousOperator == Operator.VARIABLE) {
			throw new FailedArithmeticRuleException("Offene Klammer an Stelle " + (index + 1) + " gefunden, welche direkt nach einer Variablen steht!");
		}
	}
	
	

	/**
	 * Prüfe Regeln, bis zum aktuellen Index, die eine schließende Klammer erfüllen muss
	 * @param index Die Position der schließenden Klammer
	 * @throws FailedArithmeticRuleException Wenn schließende Klammer nicht direkt nach einer Variablen oder offenen Klammer steht
	 */
	private void assertBracketCloseRules(int index) throws FailedArithmeticRuleException{		
		//schließende Klammer darf nicht an erster Stelle stehen (sollte bereits vorher geprüft worden sein)
		if(index == 0) {
			throw new FailedArithmeticRuleException("Schließende Klammer darf nicht an erster Stelle stehen!");
		}
		
		//Ansonsten prüfen, ob vorheriges Zeichen eine Variable ist -> Wenn nein, dann Exception
		Operator previousOperator = getPreviousOperator(index);
		
		if(previousOperator != Operator.VARIABLE && previousOperator != Operator.BRACKET_OPEN) {
			throw new FailedArithmeticRuleException("Schließende Klammer an Stelle " + (index + 1) + " gefunden, welche nicht direkt nach einer Variablen oder offenen Klammer steht!");
		}
	}
	
	
	
	
	/**
	 * Prüfe Regeln, bis zum aktuellen Index, die ein AND oder OR erfüllen muss
	 * @param index Die Position des AND oder OR
	 * @param operator Der Operator an der Position (AND oder OR)
	 * @throws FailedArithmeticRuleException Wenn AND oder OR nicht nach einer Variablen oder schließenden Klammer steht
	 */
	private void assertAndOrOrRules(int index, Operator operator) throws FailedArithmeticRuleException{
		//Wenn erstes Zeichen, dann Exception
		if(index == 0) {
			throw new FailedArithmeticRuleException(operator + " darf nicht an erster Stelle stehen!");
		}
		
		//Ansonsten prüfen, ob vorheriges Zeichen eine Variable oder eine Klammer zu ist -> Wenn ja alles okay, ansonsten Exception
		Operator previousOperator = getPreviousOperator(index);
		
		if(previousOperator == Operator.VARIABLE || previousOperator == Operator.BRACKET_CLOSE) {
			return;
		}
		else {
			throw new FailedArithmeticRuleException(operator + " an Stelle " + (index + 1) + " gefunden, welches nicht nach einer Variablen oder einer Klammer zu steht!");
		}
	}
	
	/**
	  Prüfe Regeln, bis zum aktuellen Index, die ein NOT erfüllen muss
	 * @param index Die Position des NOT
	 * @throws FailedArithmeticRuleException Wenn NOT nach einer Variablen oder schließenden Klammer steht
	 */
	// TODO Prüfen, ob NOT nach NOT stehen darf
	private void assertNotRules( int index) throws FailedArithmeticRuleException {
		//Wenn erstes Zeichen, dann alles okay
		if(index == 0) {
			return;
		}
		
		//Ansonsten prüfen, ob vorheriges Zeichen eine Variable oder eine Klammer zu ist -> Wenn ja, Exception
		Operator previousOperator = getPreviousOperator(index);
				
		
		if(previousOperator == Operator.VARIABLE || previousOperator == Operator.BRACKET_CLOSE) {
			throw  new FailedArithmeticRuleException ("NOT an Stelle " + (index + 1) + " gefunden, welches nicht nach einer Variablen oder einer Klammer zu steht!");
		}
	}
	

	/**
	 * Prüfe Regeln, bis zum aktuellen Index, die eine Variable erfüllen muss
	 * @param index Die Position der Variablen
	 * @throws FailedArithmeticRuleException Wenn die Variable direkt nach einer anderen Variablen oder schließenden Klammer steht
	 */
	private void assertVariableRules(int index) throws FailedArithmeticRuleException{		
		//Wenn an erster Stelle, dann alles ok
		if(index == 0) {
			return;
		}
		
		//Ansonsten prüfen, ob vorheriges Zeichen eine Variable oder schließende Klammer ist -> Wenn ja, dann Exception
		Operator previousOperator = getPreviousOperator(index);
		
		if(previousOperator == Operator.VARIABLE || previousOperator == Operator.BRACKET_CLOSE) {
			throw new FailedArithmeticRuleException("Variable an Stelle " + (index + 1) + " gefunden, welche direkt nach einer anderen Variablen oder schließenden Klammer steht!");
		}
	}
	
	
	private Operator getPreviousOperator(int index) {
		return operators.get(index - 1);
	}
	
	
}
