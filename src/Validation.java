
public class Validation {
	
	// Standard constructor
	private Validation(String line, VariableType type) {
		
	}
	
	// Bounded constructor
	private Validation(String line, VariableType type, int lowerBound, int upperBound) {
		
	}
	
	
	private Object validate_Line(String line) {
		if (line.equals("q")) {
			return null;
		} else {
			// switch statement
			return null;
		}
	}
}
