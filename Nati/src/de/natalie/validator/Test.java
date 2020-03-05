package de.natalie.validator;

import de.natalie.validator.exceptions.InvalidUserInputException;

public class Test {
	
	public static void main(String[] args) {
		test("( A * B ) + ( C * ¬ D )", true);
		test("( A + D + B * C ) + ¬ B * D", true);
		test("( A ) + ( B ) + ( C + D )", true);
		test("A + C * ¬ ( B + A )", true);
		test("( A ) + ( B * C ) + ( C + D )", true);
		// Klammer auf Test
		test("¬ ( A * B + C ( ) ) + ¬ ( C * ¬ D )", false);
		// Klammer zu Test
		test("¬ ( ¬ ) A * B + C + ¬ ( C * ¬ D )", false);
		test("¬ ( A * B + C ) ( A + ) ¬ ( C * ¬ D )", false);
		// Anzahl Klammern Test
		test(") ¬ ( A * B + C  ) ) + ) ¬ ( C * ¬ D )", false);
		test("¬ ( A * B ) + C ) ) + ) ¬ ( C * ¬ D )", false);
		test("¬ ( A * B + C ) + ( ( ¬ ( C * ¬ D )", false);
		// Variablen Test
		test("A B + C * ¬ ( B + A )", false);
		test("A + C * ¬ ( B + A ) A", false);
		// NOT Test
		test("A ¬ + C * ¬ ( B + A )", false);
		test("A + C * ¬ ( B + A ) ¬", false);
		// AND und OR Test
		test("A + * ¬ ( B + A )", false);
		test("* A + C * ¬ ( B + A )", false);
		test("A + C * ¬ ( * B + A )", false);		
	}
	
	private static int testID = 1;
	
	private static void test(String formel, boolean expect) {
		try {
			new Validator(formel).checkUserInput();
			
			if(expect) {
				System.out.println("Test " + testID + ": Erfolg!");
			}
			else {
				System.err.println("Formel: " + formel);
				System.err.println("Test " + testID + ": Fehlerhafte Formel ist erfolgreich gewesen...");
			}
		}
		catch (InvalidUserInputException ex) {
			if(expect) {
				System.err.println("Formel: " + formel);
				System.err.println("Test " + testID + ": Hätte erfolgreich sein müssen!");
				System.err.println(ex.getMessage());
				
				if(ex.getCause() != null) {
					System.err.println(ex.getCause().getMessage());
				}
			}
			else {
				System.out.println("Test " + testID + ": Fehler erfolgreich gefunden!");
			}
		}
		
		testID++;
	}
	
}
