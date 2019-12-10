import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author      Ali Eshghi, Charlotte Gephart, Emily Kim, Ester Zhao
 * @version     1.0
 */
public class Controller {
	/** Instance of the ValleyBikeSim */
	ValleyBikeSim valleyBike = ValleyBikeSim.getInstance();
	/** Global Scanner object */
	private Scanner sc = new Scanner(System.in);
	/** Hashmap of all user login info username:password)*/
	private Map<String, String> userAccounts = new HashMap<>();
	/** Hashmap of all employee login info username:password)*/
	private Map<String, String> employeeAccounts = new HashMap<>();
	
	/**
	 * Controller class constructor. 
	 * </p>
	 * Calls methods to read csv data files populates user/employee HashMaps.
	 */
	public Controller() {
		// Read in csv data
		this.userAccounts = readUserData();
		this.employeeAccounts = readEmployeeData();
	}
	
	/**
	 * Choose employee or user view.
	 * </p>
	 * @throws IOException
	 * @throws ParseException 
	 */
	private void chooseView() throws IOException, ParseException {
		System.out.println("Welcome to ValleyBike! "
				+ "Please enter 'user' for user and 'employee' for employee: ");
		String response = sc.next();
		if (response.equalsIgnoreCase("user")) {
			String username = accountLogin();
			executeUser(username);
		} else if (response.equalsIgnoreCase("employee")) {
			employeeLogin();
			executeEmployee();
		} else {
			System.out.println("Invalid input, try again.");
			chooseView();
		}
	}
	
	/**
	 * Menu selector for user options. 
	 * </p>
	 * Runs until user selects 0 to quit program.
	 * </p>
	 * @throws IOException
	 */
	private void executeUser(String username) throws IOException {
		System.out.println("Please choose from the following menu options:\n" + "0. Quit Program.\n"
				+ "1. View station list.\n" + "2. View map. \n" + "3. Check out bike.\n" + "4. Check in bike.\n" + "5. View history.\n"
				+ "6. View account info.\n" + "7. Report issue.\n");
		// Get user input
		Object obj = validateLine("Please enter your selection (0-7)", VariableType.INT, 0, 7);
		// If user enters "q", exit to chooseView
		if (obj == null) {
			try {
				chooseView();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		int option = (int)obj;
		
		switch (option) {
		/* 0. Quit program */
		case 0:
			// Close scanner
			sc.close();
			// Save all system data
			valleyBike.saveData();
			// Exit statement
			System.out.println("Thank you for using ValleyBike, have a great day!");
			System.exit(0);
			break;
		case 1:
			System.out.println(valleyBike.viewStationList());
			break;
		case 2:
			new MapApp(true);
			break;
		case 3:
			System.out.println(checkOutBike(username));
			break;
		case 4: 
			System.out.println(checkInBike(username));
			break;
		case 5:
			System.out.println(valleyBike.viewHistory(username));
			break;
		case 6:
			System.out.println(valleyBike.viewAccount(username));
			break;
		case 7:
			System.out.println(reportIssue(username));
			break;
		default:
			System.out.println("Input must be an integer from 0-6.");
			executeUser(username);
		}
		// execute call again after each switch case
		executeUser(username);
	}
	
	/**
	 * Menu selector for employee options. 
	 * </p>
	 * Runs until employee selects 0 to quit program.
	 * </p>
	 * @throws IOException
	 * @throws ParseException 
	 */
	private void executeEmployee() throws IOException, ParseException {
		System.out.println("Please choose from the following menu options:\n" + "0. Quit Program.\n"
				+ "1. View station list.\n" + "2. View map.\n" + "3. View current Rides\n" + "4. View Issues.\n" + "5. Resolve Issues.\n"
				+ "6. Add Station.\n" + "7. View System Overview.\n" + "8. Check Stats.\n" + "9. Add Bikes.\n" + "10. Equalize Stations.\n" 
				+ "11. Delete Station.\n");

		// Get user input
		Object obj = validateLine("Please enter your selection (0-11)", VariableType.INT, 0, 11);
		// If user enters "q", exit to chooseView
		if (obj == null) {
			try {
				chooseView();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		int option = (int)obj;
		
		switch (option) {
		/* 0. Quit program */
		case 0:
			// Close scanner
			sc.close();
			// Save all system data
			valleyBike.saveData();
			System.out.println("Thank you for using ValleyBike, have a great day!");
			System.exit(0);
			break;
		case 1:
			System.out.println(valleyBike.viewStationList());
			break;
		case 2:
			new MapApp(true);
			break;
		case 3:
			// Take in all ride data
			System.out.println(valleyBike.viewCurrentRides());
			break;
		case 4: 
			System.out.println(valleyBike.viewIssues());
			break;
		case 5:
			System.out.println(resolveIssues());
			break;
		case 6:
			System.out.println(addStation());
			break;
		case 7:
			System.out.println(valleyBike.viewSystemOverview());
			break;
		case 8:
			System.out.println(valleyBike.viewStats());
			break;
		case 9:
			System.out.println(addBikes());
			break;
		case 10:
			System.out.println(valleyBike.equalizeStations());
			break;
		case 11: 
			System.out.println(removeStation());
			break;
		default:
			System.out.println("Input must be an integer from 0-9.");
			executeEmployee();
		}
		// execute call again after each switch case
		executeEmployee();
	}
	
	/**
	 * Populate user login data Hashmaps.
	 * </p>
	 * Reads in data from stored csv file 
	 * Parse values into new objects 
	 * Add objects to a HashMap using key-value pair
	 * </p>
	 * @return users:passwords global Hashmap
	 */
	private HashMap<String, String> readUserData() {

		HashMap<String, String> userAccounts = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data-files/controller-users.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				String username = values[0];
				String password = values[1];
				userAccounts.put(username,password);
			}
			br.close();
			return userAccounts;
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read controller-user data file.");
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Populate employee login data Hashmaps.
	 * </p>
	 * Reads in data from stored csv file 
	 * Parse values into new objects 
	 * Add objects to a HashMap using key-value pair
	 * </p>
	 * @return employee:password global Hashmap
	 */
	private HashMap<String, String> readEmployeeData() {

		HashMap<String, String> employeeAccounts = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data-files/controller-employees.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				String username = values[0];
				String password = values[1];
				employeeAccounts.put(username,password);
			}
			br.close();
			return employeeAccounts;
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read controller-employees data file.");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Allows a user to login or create a new account.
	 * </p>
	 * User chooses to either log in or create a new account 
	 * Log in prompts user to enter username and password and verifies
	 * New account prompts user to enter new account information and saves new user
	 * </p>
	 * @return username		current user's username
	 */
	private String accountLogin() {
		String username = "";
		System.out.println("Would you like to login or create a new account? "
				+"Type 'l' for login and 'n' for new account: " );
		String choice = sc.next().toLowerCase();
		if (choice.equalsIgnoreCase("l")) {
			System.out.println("Enter username: ");
			username = sc.next();
			System.out.println("Enter password: ");
			String password = sc.next();
			// Clear out scanner
			sc.nextLine();
			if (login(username, password) == true) {
				return username;
			} else {
				// If username exists, but password was incorrect
				if (userAccounts.containsKey(username)) {
					System.out.println("Incorrect password. Please re-enter your credentials.");
				} else { // if username does not exist
					System.out.println("Invalid credentials. Please create an account, or re-enter your information.");
				}
				accountLogin();
			}
		} else if (choice.equalsIgnoreCase("n")) {
			sc.nextLine();
			String response = createUserCredentials();
			if (response.equalsIgnoreCase("quit")) {
				accountLogin();
			} else {
				return response;
			}
		} else {
			System.out.println("Input invalid. Please enter 'l' or 'n'.");
			accountLogin();
		}
		return username;
	}
	
	/**
	 * Takes in user input to create a new user account
	 * @return the username of the new account
	 */
	private String createUserCredentials() {
		
		String username = "";
		String password = "";
		Object usernameObj = validateLine("Enter username (min 5 characters)", VariableType.STRING, 5, 25);
		if (usernameObj == null) {
			return "quit";
		} else {
			username = usernameObj.toString();
		}
		
		Object passwordObj = validateLine("Enter password (min 5 characters)", VariableType.STRING, 5, 25);
		if (passwordObj == null) {
			return "quit";
		} else {
			password = passwordObj.toString();
		}
		// Accepted username is greater than 5 characters
		String createAccountResponse = createAccount(username, password);
		if (username.length() >= 5 && password.length() >= 5 && createAccountResponse.equalsIgnoreCase("true")) {
			return username;
		}
		else if (createAccountResponse.equalsIgnoreCase("false")) {
			System.out.println("Error creating account (username taken or payment invalid)");
			return accountLogin();
		} else {
			return "quit";
		}
		//return response;
	}
	
	private String employeeLogin() {
		String employeeUsername = ""; 
		System.out.println("Enter username: ");
		employeeUsername = sc.next();
		System.out.println("Enter password: ");
		String password = sc.next();

		// Clear out scanner
		sc.nextLine();
		if (login(employeeUsername, password) == true) {
			return employeeUsername; 
		}
		else {
			if (employeeAccounts.containsKey(employeeUsername)){
				// If employeeUsername exists but password was incorrect
				System.out.println("Incorrect password. Please re-enter your credentials.");
			} else { // if employeeUsername does not exist
				System.out.println("Invalid username. Please re-enter employee credentials.");
			}
			employeeLogin();
		}
		return employeeUsername;
		
	}
	
	/**
	 * Verifies login information.
	 * </p>
	 * Checks if the entered username and password match an entry in the userAccounts hashmap
	 * </p>
	 * @param username		current user
	 * @param password		current user's oassword
	 * @return boolean 		If username and password match entry in userAccounts hashmap
	 */
	private boolean login(String username, String password) {
		if (userAccounts.containsKey(username) && userAccounts.get(username).equals(password)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Creates new user account.
	 * </p>
	 * User provides payment information for validation
	 * If valid, the valleyBike object creates the user and the new user info is added to the userAccounts hashmap
	 * </p>
	 * @param username
	 * @param password
	 * @return boolean		True if account can be successfully created and validated.
	 */
	private String createAccount(String username, String password){
		int membership = 0;
		long cardNum = 0;
		int CVV = 0;
		String expDate = new String();
		
		if (userAccounts.containsKey(username) == true) {
			return "false";
		} else {
			System.out.println("createAccount called");
			System.out.println("Types of ValleyBike Memberships:\n"
					+ "0 = Pay-per-ride, $2 per ride.\n"
					+ "1 = Pay-per-month, $20 per month \n"
					+ "2 = Pay-per-year, $80 per year.");
			// Get membership type
			Object obj = validateLine("Please enter your preferred membership type (0,1,2)", VariableType.INT, 0, 2);
			if (obj == null) {
				System.out.println("Object was null, returning to login");
				return "quit";
			} else {
				// Set membership #
				membership = (int)obj;
			}
			// Get credit card number
			Object obj1 = validateLine("Please enter your credit card number", VariableType.LONG);
			if (obj1 == null) {
				return "quit";
			} else {
				// Set card num
				cardNum = (long)obj1;
			}
			// Get card cvv
			Object obj2 = validateLine("Please enter your CVV", VariableType.CVV);
			if (obj2 == null) {
				return "quit";
			} else {
				// Set cvv
				CVV = (int)obj2;
			}
			// Get card expiration date
			Object obj3 = validateLine("Please enter expiration date(MM/YY)", VariableType.DATE);
			if (obj3 == null) {
				return "quit";
			} else {
				// Set expiration date
				expDate = (String)obj3;
			}
			// Create user in valleybike
			if (valleyBike.createUser(username, password, membership, cardNum, CVV, expDate) == true) {
				// Add username and password to controller csvs
				this.userAccounts.put(username, password);
				System.out.println("Account successfully created!");
				return "true";
			} else {
				return "false";
			}
		}
	}
	
	/**
	 * Validate user input based on type
	 * </p>
	 * Unbounded user input 
	 * </p>
	 * @param line	user input
	 * @param type	desired type
	 * @return the user input in desired form (Object type)
	 */
	private Object validateLine(String prompt, VariableType type) {
		Object obj = null;
		System.out.println(prompt + ": ");
		while (sc.hasNextLine()) {
			// Global quit functionality
			if (sc.hasNext(Pattern.compile("q"))) {
				sc.nextLine();
				return null;
			} else {
				obj = typeSwitch(prompt, type, obj);
			}
			break;
		}
		return obj;
	}

	/**
	 * Runs switch on different types of variable input
	 * </p>
	 * @param prompt	validateLine prompt
	 * @param type		expected type of variable
	 * @param obj		the return object
	 * @return obj		return the obj
	 */
	private Object typeSwitch(String prompt, VariableType type, Object obj) {
		switch (type) {
		// Catch case
		default:
			System.out.println("In default");
			return validateLine(prompt, type);
		case INT:
			if (sc.hasNextInt()) {
				int i = Integer.parseInt(sc.nextLine());
				// Check if == 0
				obj = i;
				return obj;
			} else {
				// Else, call the prompt again
				sc.nextLine();
				return validateLine(prompt, type);
			}
		case LONG:
			if (sc.hasNextLong()) {
				String line = sc.nextLine();
				if (line.length() != 16) {
					System.out.println("Please enter a valid credit card number. (16 digits)");
					return validateLine(prompt, VariableType.LONG);
				} else {
					long l = Long.parseLong(line);
					obj = l;
					return obj;
				}
			} else {
				// Else, call the prompt again
				sc.nextLine();
				return validateLine(prompt, type);
			}
		case BOOLEAN: 
			if (sc.hasNextBoolean()) {
				boolean b = Boolean.parseBoolean(sc.nextLine());
				obj = b;
				return obj;
			} else {
				System.out.println("Input must be a boolean (true/false)");
				// Else, call prompt again
				sc.nextLine();
				return validateLine(prompt, type);
			}
		case CVV:
			if (sc.hasNextInt()) {
				String line = sc.nextLine();
				// Check if 3 chars
				if (line.length() != 3) {
					System.out.println("CVV must be 3 characters long.");
					return validateLine(prompt,type);
				} else {
					int i = Integer.parseInt(line);
					obj = i;
					return obj;
				}
			} else {
				// Else, call the prompt again
				System.out.println("CVV must be a series of 3 numbers.");
				sc.nextLine();
				return validateLine(prompt, type);
			}
		case DATE:
			String line = sc.nextLine();
			String pattern = "../..";
			// Check if input matches pattern
			if (line.length() != 5 || !Pattern.matches(pattern, line)) {
	    		System.out.println("Incorrect date format. Please enter expiration date.");
	    		return validateLine(prompt, type);
			} else {
				// Split input string on /
	    		String[] arrDate = line.split("/");
			    String userMonth = arrDate[0];
			    String userYear = arrDate[1];
			    // Check if month and year vars are both integers
			    if (isInteger(userYear) && isInteger(userMonth)) {
			    	// Check if month is a valid month (1-12)
			    	 int month = Integer.parseInt(userMonth);
					    if (month < 13 && month > 0) {
					    	obj = line;
					    	return obj;
					    } else {
					    	System.out.println("Invalid month. Please enter expiration date.");
					    	return validateLine(prompt, type);
					    }
			    } else {
			    	System.out.println("Incorrect date format. Please enter expiration date.");
			    	return validateLine(prompt, type);
			    }  
			}
		}
	}
	
	/**
	 * Checks whether an inputed string can be parsed into an integer
	 * </p>
	 * @param value		String to parse to int
	 * @return boolean	True if string can be parsed to int
	 */
	private boolean isInteger(String value) {
		try {
		    Integer.parseInt(value);
	    } catch (Exception FormatException) {
	    	return false;
	    }
		return true;
	}
	
	/**
	 * Validate user input based on type
	 * </p>
	 * Bounded user input 
	 * </p>
	 * @param line	User input
	 * @param type	Desired output type
	 * @param min	The lower bound
	 * @param max	The upper bound
	 * @return	the user input in desired form
	 */
	private Object validateLine(String prompt, VariableType type, long min, long max)  {
		Object obj = null;
		System.out.println(prompt + ": ");
		while (sc.hasNext()) {
			// Global quit functionality
			if (sc.hasNext(Pattern.compile("q"))) {
				sc.nextLine();
				return null;
			} else {
				switch (type) {
				case INT:
					if (sc.hasNextInt()) {
						int i = Integer.parseInt(sc.nextLine());
						// Check if int is within bounds
						if ((int)min <= i && i <= (int)max) {
							obj = i;
							return obj;
						} else {
							System.out.println("Please enter a number in the range "+min+"-"+max);
							return validateLine(prompt, type, min, max);
						}
					} else {
						
						// Else, call the prompt again
						sc.nextLine();
						return validateLine(prompt, type, min, max);
					}
				case LONG:
					if (sc.hasNextLong()) {
						long l = Long.parseLong(sc.nextLine());
						// Check if int is within bounds
						if (min <= l && l <= max) {
							obj = l;
							return obj;
						} else {
							System.out.println("Please enter a number in the range "+min+"-"+max);
							return validateLine(prompt, type, min, max);
						}
					} else {
						// Else, call the prompt again
						sc.nextLine();
						return validateLine(prompt, type, min, max);
					}
				case STRING:
					String line = sc.nextLine();
					
					if(line.contains(" ")) {
						System.out.println("No spaces are allowed, try again");
						return validateLine(prompt, type, min, max);
					} else if ((int)min <= line.length() && line.length() <= (int)max) {
						obj = line;
						return obj;
					} else {
						System.out.println("Please ensure your input is in the range of "+min+"-"+max+" characters.");
						return validateLine(prompt, type, min, max);
					}
					
				default: // catch case
					return validateLine(prompt, type, min, max);
				}
			}
		}
		return obj;
	}
	
	/**
	 * Allows the user to check out a bike.
	 * </p>
	 * User enters ID of station they're checking a bike out from
	 * Calls valleyBike.checkOut()
	 * </p>
	 * @param username
	 * @return report	String confirming success from valleyBike.checkOut
	 */
	private String checkOutBike(String username) {
		// Initialize response string
		String response = new String();
		Object obj = validateLine("Please enter your current station", VariableType.INT);
		if (obj == null) {
			// Global quit functionality - return to menu
			response = "";
		} else {
			int id = (int)obj;
			if (valleyBike.stationExists(id)) {
				response = valleyBike.checkOutBike(username, id);
			} else {
				System.out.println("The station you entered does not exist. Please enter an existing station id.");
				// Call func again
				checkOutBike(username);
			}
		}
		return response;
	}
	
	/**
	 * Allows the user to check in a bike.
	 * </p>
	 * User enters ID of station they're checking a bike into
	 * Calls valleyBike.checkIn()
	 * </p>
	 * @param username
	 * @return report	String confirming success from valleyBike.checkIn
	 */
	private String checkInBike(String username){
		Object obj = validateLine("Please enter your current station", VariableType.INT);
		if (obj == null) {
			// Global quit functionality - return to menu
			return "";
		} else {
			int id = (int)obj;
			if (valleyBike.stationExists(id)) {
				return valleyBike.checkInBike(username, id);
			} else {
				System.out.println("The station you entered does not exist. Please enter an existing station id.");
				// Call func again
				return checkInBike(username);
			}
		}
	}
	
	/**
	 * Allows the user to report an issue/maintenance request.
	 * </p>
	 * User enters ID of the current station and the issue message
	 * Calls valleyBike object's reportIssue method
	 * </p>
	 * @param username
	 * @return report	String confirming success from valleyBike.reportIssue
	 */
	private String reportIssue(String username){
		String response = new String();
		Object obj = validateLine("Please enter your current station", VariableType.INT);
		if (obj == null) {
			// Global quit functionality - return to menu
			response = "";
		} else {
			int stationId = (int)obj;
			if (valleyBike.stationExists(stationId)) {
				// Get issue message
				System.out.println("Please enter issue message: ");
				String issueMessage = sc.nextLine();
				response = valleyBike.reportIssue(username, stationId, issueMessage);
			} else {
				System.out.println("The station you entered does not exist. Please enter an existing station id.");
				// Call func again
				reportIssue(username);
			}
		}
		return response;
	}
		
	
	/**
	 * Allows employee to add a station to the system.
	 * </p>
	 * Prompts user for station info input.
	 * </p>
	 * @return report	String confirming success from valleyBike.addStation
	 */
	private String addStation() {
		int capacity = 0;
		boolean kiosk = false;
		// Get station capacity
		Object capObj = validateLine("Please enter station capacity", VariableType.INT, 5, 27);
		if (capObj == null) {
			// Global quit functionality - return to menu
			return "";
		} else {
			capacity = (int)capObj;
		}
		// Get station kiosk
		Object kioskObj = validateLine("Please enter whether station has a kiosk or not (true/false)", VariableType.BOOLEAN);
		if (kioskObj == null) {
			return "";
		} else {
			kiosk = (boolean)kioskObj;
		}
		// Get station address and name
		System.out.println("Please enter station address: ");
		String address = sc.nextLine();
		System.out.println("Please enter station name: ");
		String name = sc.nextLine();
		return valleyBike.addStation(capacity, kiosk, address, name);
	}
	
	
	/**
	 * Allows employee to remove stations.
	 * </p>
	 * Prompts user for bike info input
	 * </p>
	 * @return confirmation String
	 */
	private String removeStation() {
		Object stationObj = validateLine("Please enter the ID of the station to remove", VariableType.INT);
		if (stationObj == null) {
			return "";
		} else {
			int id = (int)stationObj;
			if(valleyBike.stationExists(id)) {
				return valleyBike.removeStation(id);
			} else {
				System.out.println("That station is not in our system");
				return removeStation();
			}
		}
	}
	
	/**
	 * Allows employee to add bikes to a specific station.
	 * </p>
	 * Prompts user for bike info input
	 * </p>
	 * @return report	String confirming success from valleyBike.addBikes
	 */
	private String addBikes() {
		String response = new String();
		Object stationObj = validateLine("Please enter the station you would like to add bikes to", VariableType.INT);
		if (stationObj == null) {
			// Global quit functionality - return to menu
			response = "";
		} else {
			int stationId = (int)stationObj;
			if (valleyBike.stationExists(stationId)) {
				Object bikesObj = validateLine("Please enter the number of bikes you would like to add", VariableType.INT, 0, 40);
				if (bikesObj == null) {
					response = "";
				} else {
					int numBikes = (int)bikesObj;
					if (numBikes <= 0) {
						response = "No bikes added.";
					} else {
						response = valleyBike.addBikes(stationId, numBikes);
					}
				}
			} else {
				System.out.println("The station you entered does not exist. Please enter an existing station id.");
				// Call func again
				addBikes();
			}
		}
		return response;
	}
	
	/**
	 * Allows employee to resolve issues.
	 * </p>
	 * Prompts user for the issue IDs and creates a list
	 * Calls valleyBike resolveIssues method to remove the issues from the mainReqs hashmap
	 * </p>
	 * @return report	String confirming success from valleyBike.resolveIssue
	 */
	private String resolveIssues() {
		// Print out all issues
		System.out.println(valleyBike.viewIssues());
		
		ArrayList<Integer> issues = new ArrayList<Integer>();
		String response = "y";
		while (response.equalsIgnoreCase("y")) {
			Object obj = validateLine("Please enter issue number", VariableType.INT);
			if (obj == null) {
				// Global quit functionality - return to menu
				return "";
			} else {
				int issue = (int)obj;
				issues.add(issue);
				System.out.println("Would you like to add another issue to resolve? (y/n): ");
				response = sc.next();
			}	
		}
		sc.nextLine();
		return valleyBike.resolveIssues(issues);
	}
	
	/**
	 * Controller main method.
	 * @throws ParseException 
	 */
	public static void main(String[] args){
		Controller controller = new Controller();
		try {
			controller.chooseView();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
