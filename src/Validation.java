
public class Validation {
	
	// Standard constructor
	public Validation() {
		
	}
	
	// Unbounded input
	private Object validate_Line(String line, VariableType type) {
		if (line.equals("q")) {
			return null;
		} else {
			// Switch statement for variable types
			switch (type) {
			case STRING:
				return line;
			case INT:
				try {
					return Integer.parseInt(line);
				} catch (Exception NumberFormatException) {
					return "Error";
				}
			case LONG:
				try {
					return Long.parseLong(line);
				} catch (Exception FormatException) {
					return "Error";
				}
			case BOOLEAN:
				try {
					return Boolean.parseBoolean(line);
					} catch (Exception FormatException) {
					return "Error";
				}
			case DATE:
				// add in date verification
				return null;
			default: // catch for other var types
				return "Error";
			}
		}
	}
	
	private Object validate_Line(String line, VariableType type, long min, long max) {
		if (line.equals("q")) {
			return null;
		} else {
			switch (type) {
			case INT:
				int i = 0;
				// Check if valid int
				try {
					i = Integer.parseInt(line);
				} catch (Exception NumberFormatException) {
					return "Error";
				}
				// Check if int is within bounds
				if ((int)min <= i && i <= (int)max) {
					return i;
				} else {
					return "Error";
				}
			case LONG:
				long l = 0;
				// Check if valid long
				try {
					l = Long.parseLong(line);
				} catch (Exception NumberFormatException) {
					return "Error";
				}
				// Check if long is within bounds
				if (min <= l && l <= max) {
					return l;
				} else {
					return "Error";
				}
			default: // catch for other var types
				return "Error";	
			}
		}
	}
}
