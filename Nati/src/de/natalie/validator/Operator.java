package de.natalie.validator;

/**
 * Ein Operator einer Formel
 * @author Natalie Bestler
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
	
	/**
	 * Gibt entsprechenden Operator zurück
	 * @param identifier Umzuwandelndes Zeichen
	 * @return Operator des entsprechenden Zeichens ({@link Operator}.VARIABLE bei allem, das kein bekanntes Zeichen ist)
	 */
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
