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
	public void chooseView() throws IOException, ParseException {
		System.out.println("Welcome to ValleyBike! "
				+ "Please enter 'user' for user and 'employee' for employee: ");
		String response = sc.next();
		if (response.equalsIgnoreCase("user")) {
			String username = accountLogin();
			executeUser(username);
		} else if (response.equalsIgnoreCase("employee")) {
			//TODO: Add employee login in A5
			String employeeUsername = employeeLogin();
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
	public void executeUser(String username) throws IOException {
		System.out.println("Please choose from the following menu options:\n" + "0. Quit Program.\n"
				+ "1. View station list.\n" + "2. View map. \n" + "3. Check out bike.\n" + "4. Check in bike.\n" + "5. View history.\n"
				+ "6. View account info.\n" + "7. Report issue.\n");
		// Get user input
		Object obj = validateLine("Please enter your selection (0-7)", VariableType.INT, 0, 7);
		int option = (int)obj;
		
		switch (option) {
		/* 0. Quit program */
		case 0:
			// Close scanner
			sc.close();
			// Exit statement
			System.out.println("Thank you for using ValleyBike, have a great day!");
			System.exit(0);
			break;
		case 1:
			System.out.println(valleyBike.viewStationList());
			break;
		case 2:
			MapApp map = new MapApp(true);
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
	public void executeEmployee() throws IOException, ParseException {
		System.out.println("Please choose from the following menu options:\n" + "0. Quit Program.\n"
				+ "1. View station list.\n" + "2. View map.\n" + "2. View current Rides\n" + "3. View Issues.\n" + "4. Resolve Issues.\n"
				+ "5. Add Station.\n" + "6. View System Overview.\n" + "7. Check Stats.\n" + "8. Add Bikes.\n" + "9. Equalize Stations.\n");

		// Get user input
		Object obj = validateLine("Please enter your selection (0-10)", VariableType.INT, 0, 10);
		int option = (int)obj;
		
		switch (option) {
		/* 0. Quit program */
		case 0:
			// Close scanner
			sc.close();
			System.out.println("Thank you for using ValleyBike, have a great day!");
			System.exit(0);
			break;
		case 1:
			System.out.println(valleyBike.viewStationList());
			break;
		case 2:
			MapApp map = new MapApp(true);
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
	public HashMap<String, String> readUserData() {

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
	public HashMap<String, String> readEmployeeData() {

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
			return createUserCredentials();
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
		String response = "";
		System.out.println("Enter username (min 5 characters): ");
		String username = sc.next();
		if (username.length() < 5) {
			System.out.println("Username must be at least 5 characters long. Please enter a new username.");
			createUserCredentials();
		}
		System.out.println("Enter password (min 5 characters): ");
		String password = sc.next();
		if (password.length() < 5) {
			System.out.println("Password must be at least 5 characters long. Please enter a new username and password.");
			createUserCredentials();
		}
		// Clear out scanner
		sc.nextLine();	
		
		// Accepted username is greater than 5 characters
		if (username.length() >= 5 && password.length() >= 5 && createAccount(username, password) == true) {
			response = username;
		}
		else {
			System.out.println("Error creating account (username taken or payment invalid)");
			accountLogin();
		}
		return response;
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
	private boolean createAccount(String username, String password){
		int membership = 0;
		long cardNum = 0;
		int CVV = 0;
		String expDate = new String();
		
		if (userAccounts.containsKey(username) == true) {
			return false;
		} else {
			System.out.println("Types of ValleyBike Memberships:\n"
					+ "0 = Pay-per-ride, $2 per ride.\n"
					+ "1 = Pay-per-month, $12 per month \n"
					+ "2 = Pay-per-year, $100 per year.");
			// Get membership type
			Object obj = validateLine("Please enter your preferred membership type (0,1,2)", VariableType.INT, 0, 2);
			if (obj == null) {
				// Global quit functionality - return to menu
				//TODO: figure out how to do this
			} else {
				// Set membership #
				membership = (int)obj;
			}
			// Get credit card number
			Object obj1 = validateLine("Please enter your credit card number", VariableType.LONG);
			if (obj1 == null) {
				//TODO: figure out how to do this
			} else {
				// Set card num
				cardNum = (long)obj1;
			}
			//TODO: check if cvv has the right number of digits 
			// Get card cvv
			Object obj2 = validateLine("Please enter your CVV", VariableType.INT, 0, 999);
			System.out.println("hi");
			if (obj2 == null) {
				//TODO: figure out how to do this
			} else {
				// Set cvv
				CVV = (int)obj2;
			}
			// Get card expiration date
			Object obj3 = validateLine("Please enter expiration date(MM/YY)", VariableType.DATE);
			System.out.println(obj3);
			if (obj3 == null) {
				//TODO: figure out how to do this
			} else {
				// Set expiration date
				expDate = (String)obj3;
			}
			// Create user in valleybike
			if (valleyBike.createUser(username, password, membership, cardNum, CVV, expDate) == true) {
				// Add username and password to controller csvs
				this.userAccounts.put(username, password);
				System.out.println("Account successfully created!");
				return true;
			} else {
				return false;
			}
			
//			Integer membership = getIntResponse("Please enter your preferred membership type (0,1,2)", 0, 2);
//			//TODO: validate cardNum: 16 digits, etc. I THINK I DID THIS
//			Long cardNum = getUnboundedLongResponse("Please enter your credit card number",(long)0);
//			valCardNum(cardNum);
//			Integer CVV = getIntResponse("Please enter your CVV", 0, 999);
//			System.out.println("Please enter expiration date(MM/YY): ");
//			//TODO: validate this date
//			String expDate = sc.next();
//			userAccounts.put(username, password);
//			if (valleyBike.createUser(username, password, membership, cardNum, CVV, expDate) == true) {
//				// Add username and password to controller csvs
//				this.userAccounts.put(username, password);
//				System.out.println("Account successfully created!");
//				return true;
//			} else {
//				return false;
//			}
		}
	}
	
	
	/**
	 * 
	 * </p>
	 * @param 
	 * @param 
	 * @param 
	 * @return
	 */
	private Long valCardNum(Long cardNum) {
		String length = Long.toString(cardNum);
		if (length.length() != 16) {
			System.out.println("Please enter a sufficient credit card with 16 digits");
			getUnboundedLongResponse("Please enter a valid credit card number", (long)0);
		} else {
			System.out.println("It worked");
		}
		return cardNum;
	}
	
	private boolean getBoolResponse(String request) {
		boolean entry = true;
		System.out.println(request + ": ");
		while(sc.hasNext()) {
			if(!sc.hasNextBoolean()) {
				System.out.println("Entry must be a boolean");
				System.out.println(request + ": ");
				sc.next();
			}else{
				entry = sc.nextBoolean();
				break;
			}
		}
		return entry;
	}
	
	/**
	 * Gets an int response from the user within a specific range.
	 * </p>
	 * @param request
	 * @param min
	 * @param max
	 * @return the integer entry
	 */
	private Integer getIntResponse(String request, Integer min, Integer max){
		Integer entry = 0;
		System.out.println(request + ": ");
		while (sc.hasNext()){
			if(!sc.hasNextInt()){
				System.out.println("Entry must be an integer");
				System.out.println(request + ": ");
				sc.next();
			}else{
				entry = sc.nextInt();
				if(min <= entry && entry <= max){
					break;
				} else{
					System.out.println("Entry must be an integer in range "+min + "-" + max);
					System.out.println(request + ": ");
					sc.nextLine();
				}	
			}
		}
		return entry;
	}
	
	/**
	 * Gets a long response from the user within a specific range.
	 * </p>
	 * @param request
	 * @param i
	 * @return the long input
	 */
	private Long getUnboundedLongResponse(String request, Long i) {
		Long input = (long)0;
		System.out.println(request + ": ");
		String length = Long.toString(input);
		while (sc.hasNext()){
			if(!sc.hasNextLong()){
				System.out.println("Entry must be an integer");
				System.out.println(request + ": ");
				sc.next();
			}else{
				input = sc.nextLong();
				if(i <= input){
					break;
				} else{
					System.out.println("Entry must be an integer equal to or above "+i);
					System.out.println(request + ": ");
					sc.nextLine();
				}	
			}
		}
		return input;
	}

	
	/**
	 * Gets an unbounded int response from the user.
	 * </p>
	 * @param request
	 * @param min
	 * @return the integer entry
	 */
	private Integer getUnboundedIntResponse(String request, Integer min) {
		Integer entry = 0;
		System.out.println(request + ": ");
		while (sc.hasNext()){
			if(!sc.hasNextInt()){
				System.out.println("Entry must be an integer");
				System.out.println(request + ": ");
				sc.next();
			}else{
				entry = sc.nextInt();
				if(min <= entry){
					break;
				} else{
					System.out.println("Entry must be an integer equal to or above "+min);
					System.out.println(request + ": ");
					sc.nextLine();
				}	
			}
		}
		return entry;
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
	public Object validateLine(String prompt, VariableType type) {
		System.out.println("In validate");
		Object obj = null;
		System.out.println(prompt + ": ");
		scannerLoop:
		while (sc.hasNextLine()) {
			System.out.println("Top of while");
			// Global quit functionality
			if (sc.hasNext(Pattern.compile("q"))) {
				sc.nextLine();
				return null;
			} else {
				System.out.println("in the switch else");
				switch (type) {
				// Catch case
				default:
					System.out.println("In default");
					break;
				case INT:
					if (sc.hasNextInt()) {
						int i = Integer.parseInt(sc.nextLine());
						// Check if == 0
						obj = i;
						break;
					} else {
						// Else, call the prompt again
						sc.nextLine();
						validateLine(prompt, type);
						break;
					}
				case LONG:
					if (sc.hasNextLong()) {
						String line = sc.nextLine();
						if (line.length() != 16) {
							System.out.println("Please enter a valid credit card number. (16 digits)");
							validateLine(prompt, VariableType.LONG);
							break;
						} else {
							long l = Long.parseLong(line);
							obj = l;
							break;
						}
					} else {
						// Else, call the prompt again
						sc.nextLine();
						validateLine(prompt, type);
						break;
					}
				case BOOLEAN: 
					if (sc.hasNextBoolean()) {
						boolean b = Boolean.parseBoolean(sc.nextLine());
						obj = b;
						break;
					} else {
						// Else, call prompt again
						sc.nextLine();
						validateLine(prompt, type);
						break;
					}
				//TODO: The date thing breaks when the first attempt is not in correct format
				//TODO: If first date is invalid but in correct format it
				// also returns wrong (previously entered incorrect date) after new correct date is entered
				case DATE:
					String line = sc.nextLine();
					System.out.println(line);
					obj = date(line, prompt, type, obj);
					break;
					break scannerLoop;
				}
			}
			System.out.println("End of switch, inside while");
			System.out.println(obj);
			break scannerLoop;
		}
		System.out.println("Outside while");
		System.out.println(obj);
		return obj;
	}
	
	public Object date(String line, String prompt, VariableType type, Object obj) {
		System.out.println("In DATE switch");
		String pattern = "../..";
//		String line = sc.nextLine();
		if (line.length() != 5 || !Pattern.matches(pattern, line)) {
    		System.out.println("Incorrect date format. Please enter expiration date.");
    		validateLine(prompt, type);
//    		break;
		} else {
			System.out.println("In correct format else");
    		String[] arrDate = line.split("/");
		    String userMonth = arrDate[0];
		    String userYear = arrDate[1];
		    // Check if values are ints, if not prompt user to enter again
		    try {
		    	System.out.println("In the try");
			    int month = Integer.parseInt(userMonth);
			    int year = Integer.parseInt(userYear);
//			    // String is in correct format
//			    if (month < 13 && month > 0) {
//			    	obj = line;
//			    	System.out.println("in switch with: " + obj.toString());
//					break;
//			    } else {
//			    	System.out.println("Invalid month. Please enter expiration date.");
////			    	obj = line;
//			    	validateLine(prompt, type);
//			    	break;
//			    }
		    } catch (Exception FormatException) {
		    	System.out.println("In the catch");
		    	System.out.println("Incorrect date format. Please enter expiration date.");
		    	validateLine(prompt, type);
//		    	break;
		    }
//		    finally {
//		    	System.out.println("Finally");
////		    	obj = line;
//		    	break;
//		    }
		    System.out.println("Out of try/catch");
		    // String is in correct format
		    int month = Integer.parseInt(userMonth);
		    int year = Integer.parseInt(userYear);
		    if (month < 13 && month > 0) {
		    	obj = line;
		    	System.out.println(obj);
		    	System.out.println("in switch with: " + obj.toString());
//				break;
		    } else {
		    	System.out.println("Invalid month. Please enter expiration date.");
//		    	obj = line;
		    	validateLine(prompt, type);
//		    	break;
		    }
		    
		}
		return obj;
	}
	
	/**
	 * 
	 * @param line	User input
	 * @param type	Desired output type
	 * @param min	The lower bound
	 * @param max	The upper bound
	 * @return	the user input in desired form
	 */
	public Object validateLine(String prompt, VariableType type, long min, long max)  {
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
							break;
						} else {
							System.out.println("Please enter a number in the range "+min+"-"+max);
							validateLine(prompt, type, min, max);
							break;
						}
					} else {
						// Else, call the prompt again
						sc.nextLine();
						validateLine(prompt, type, min, max);
						break;
					}
				case LONG:
					if (sc.hasNextLong()) {
						long l = Long.parseLong(sc.nextLine());
						// Check if int is within bounds
						if (min <= l && l <= max) {
							obj = l;
							break;
						} else {
							System.out.println("Please enter a number in the range "+min+"-"+max);
							validateLine(prompt, type, min, max);
							break;
						}
					} else {
						// Else, call the prompt again
						sc.nextLine();
						validateLine(prompt, type, min, max);
						break;
					}
				case STRING:
					String line = sc.nextLine();
					if ((int)min <= line.length() && line.length() <= (int)max) {
						obj = line;
						break;
					} else {
						System.out.println("Please ensure your input is in the range of "+min+"-"+max+" characters.");
						validateLine(prompt, type, min, max);
						break;
					}
				default: // catch case
					break;
				}
			}
			break;
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
		Object capObj = validateLine("Please enter station capacity", VariableType.INT, 0, 40);
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
	 * TODO: Consider implementing for A5
	 */
	private String moveBikes() {
		Integer stationFrom = getUnboundedIntResponse("Please enter station ID to move from", 0);
		sc.nextLine();
		Integer stationTo = getUnboundedIntResponse("Please enter station ID to move to", 0);
		sc.nextLine();
		Integer numBikes = getUnboundedIntResponse("Please enter number of bikes to move", 0);
		sc.nextLine();
		return valleyBike.moveBikes(stationFrom, stationTo, numBikes);
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
