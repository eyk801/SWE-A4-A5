import java.io.*;
import java.text.ParseException;
import java.util.*;

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
				+ "1. View station list.\n" + "2. Check out bike.\n" + "3. Check in bike.\n" + "4. View history.\n"
				+ "5. View account info.\n" + "6. Report issue.\n");

		Integer option = this.getIntResponse("Please enter your selection (0-6)", 0, 6);
		
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
			// Take in all ride data
			System.out.println(checkOutBike(username));
			break;
		case 3: 
			System.out.println(checkInBike(username));
			break;
		case 4:
			System.out.println(valleyBike.viewHistory(username));
			break;
		case 5:
			System.out.println(valleyBike.viewAccount(username));
			break;
		case 6:
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
				+ "1. View station list.\n" + "2. View current Rides\n" + "3. View Issues.\n" + "4. Resolve Issues.\n"
				+ "5. Add Station.\n" + "6. View System Overview.\n" + "7. Check Stats.\n" + "8. Add Bikes.\n" + "9. Equalize Stations.\n");

		Integer option = this.getIntResponse("Please enter your selection (0-9)", 0, 9);
		
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
			// Take in all ride data
			System.out.println(valleyBike.viewCurrentRides());
			break;
		case 3: 
			System.out.println(valleyBike.viewIssues());
			break;
		case 4:
			System.out.println(resolveIssues());
			break;
		case 5:
			System.out.println(addStation());
			break;
		case 6:
			System.out.println(valleyBike.viewSystemOverview());
			break;
		case 7:
			System.out.println(valleyBike.viewStats());
			break;
		case 8:
			System.out.println(addBikes());
			break;
		case 9:
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
			};
		} else if (choice.equalsIgnoreCase("n")) {
			System.out.println("Enter username: ");
			username = sc.next();
			System.out.println("Enter password: ");
			String password = sc.next();
			if (createAccount(username, password) == true) {
				return username;
			} else {
				System.out.println("Error creating account (username taken or payment invalid)");
				accountLogin();
			};
			
		} else {
			System.out.println("Input invalid try again.");
			accountLogin();
		}
		return username;
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
		if (userAccounts.containsKey(username) == true) {
			return false;
		} else {
			System.out.println("Please enter you preferred membership type.\n"
					+ "0 = Pay-per-ride, $2 per ride.\n"
					+ "1 = Pay-per-month, $12 per month \n"
					+ "2 = Pay-per-year, $100 per year.");
			Integer membership = getIntResponse("Please enter your preferred membership type (0,1,2)", 0, 2);
			//TODO: validate cardNum: 16 digits, etc.
			Long cardNum = getUnboundedLongResponse("Please enter you credit card number",(long)0);
			Integer CVV = getIntResponse("Please enter your CVV", 0, 999);
			System.out.println("Please enter expiration date(MM/YY): ");
			//TODO: validate this date
			String expDate = sc.next(); 
			userAccounts.put(username, password);
			if (valleyBike.createUser(username, password, membership, cardNum, CVV, expDate) == true) {
				// Add username and password to controller csvs
				this.userAccounts.put(username, password);
				System.out.println("Account successfully created!");
				return true;
			} else {
				return false;
			}
		}
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
	public Object validate_Line(String line, VariableType type) {
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
	
	/**
	 * 
	 * @param line	User input
	 * @param type	Desired output type
	 * @param min	The lower bound
	 * @param max	The upper bound
	 * @return	the user input in desired form
	 */
	public Object validate_Line(String line, VariableType type, long min, long max) {
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
		// Clear scanner
		sc.nextLine();
		System.out.println("Please enter your current station: ");
		String line = sc.nextLine();
		// Get user input
		Object obj = validate_Line(line, VariableType.INT);
		// If output is an int
		if (obj == null) {
			return "";
		} else if (obj.getClass() == Integer.class) {
			System.out.println("object is int");
			int id = (int)obj;
			if (valleyBike.stationExists(id)) {
				return valleyBike.checkOutBike(username, id);
			} else {
				return "The station you entered does not exist. Please enter an existing station id.";
			}
		} else if (obj.getClass() == String.class) { // if output is String
			String s = (String)obj;
			if (s.equals("Error")) {
				System.out.println("Please enter a valid station id.");
				checkOutBike(username);
			}
		} 
		// Catch case
		// If user enters "q", quit to main menu (return empty string)
		return "";
		
//		if (stationID == null) {
//			try {
//				executeUser(username);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else if (stationID.equals("Error")) {
//			System.out.println("Please enter a valid station id.");
//			checkOutBike(username);
//		}
//		
//		int id = (int)stationID;
//		if (valleyBike.stationExists(id)) {
//			return valleyBike.checkOutBike(username, id);
//		} else {
//			return "The station you entered does not exist. Please enter an existing station id.";
//		}
		
//		Integer stationId = getUnboundedIntResponse("Please enter your current station", 0);
//		if (valleyBike.stationExists(stationId)) {
//			return valleyBike.checkOutBike(username, stationId);
//		} else {
//			return "The station you entered does not exist. Please enter an existing station id.";
//		}
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
		Integer stationId = getUnboundedIntResponse("Please enter your current station", 0);
		if (valleyBike.stationExists(stationId)) {
			return valleyBike.checkInBike(username, stationId);
		} else {
			return "The station you entered does not exist. Please enter an existing station id.";
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
		Integer stationId = getUnboundedIntResponse("Please enter your current station", 0);
		sc.nextLine(); //throw away the \n not consumed by nextInt()
		// Check if valid station id
		if (valleyBike.stationExists(stationId)) {
			System.out.println("Please enter issue message: ");
			String issueMessage = sc.nextLine();
			return valleyBike.reportIssue(username, stationId, issueMessage);
		} else {
			return "The station you entered does not exist. Please enter an existing station id.";
		}
	}
		
	
	/**
	 * Allows employee to add a station to the system.
	 * </p>
	 * Prompts user for station info input.
	 * </p>
	 * @return report	String confirming success from valleyBike.addStation
	 */
	private String addStation() {
		Integer capacity = getIntResponse("Please enter station capacity", 0,40);
		boolean kiosk = getBoolResponse("Please enter whether station has a kiosk or not (true/false)");
		sc.nextLine();
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
		Integer stationId = getIntResponse("What station would you like to put the bikes at?", 0, 100);
		sc.nextLine();
		if (valleyBike.stationExists(stationId)) {
			Integer numBikes = getIntResponse("How many bikes would you like to add?", 
					0, 100);
			// If no bikes are added, no need to go into valleyBike.addBikes
			if (numBikes <= 0) {
				return "No bikes added.";
			} else {
				return valleyBike.addBikes(stationId, numBikes);
			}
		} else {
			return "The station you entered does not exist. Please enter an existing station id.";
		}
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
	 * @return report	String confirmingsuccess from valleyBike.resolveIssue
	 */
	private String resolveIssues() {
		ArrayList<Integer> issues = new ArrayList<Integer>();
		String response = "y";
		while (response.equalsIgnoreCase("y")) {
			Integer issue = getUnboundedIntResponse("Please enter issue number", 0);
			issues.add(issue);
			System.out.println("Would you like to add another issue to resolve? (y/n): ");
			response = sc.next();
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
