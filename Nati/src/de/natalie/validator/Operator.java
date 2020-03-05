package de.natalie.validator;

/**
 * Ein Operator einer Formel
 * @author Nati
 *
 */
public enum Operator {
	
	BRACKET_OPEN,
	BRACKET_CLOSE,
	AND,
	OR,
	NOT,
	
	IMPLICIT,
	EQUIVALENCE,
	
	VARIABLE;
	
	public static Operator getByIdentifier(String identifier) {
		switch(identifier.toUpperCase()) {
			case "(":
				return Operator.BRACKET_OPEN;
			case ")":
				return Operator.BRACKET_CLOSE;
			case "*":
				return Operator.AND;
			case "+":
				return Operator.OR;
			case "¬":
				return Operator.NOT;
			case "→":
				return Operator.IMPLICIT;
			case "↔":
				return Operator.EQUIVALENCE;
			default:
				return Operator.VARIABLE;
		}
	}
	
}
