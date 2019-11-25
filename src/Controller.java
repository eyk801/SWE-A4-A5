import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Controller {
	ValleyBikeSim valleyBike = new ValleyBikeSim();
	private Scanner sc = new Scanner(System.in);
	private Map<String, String> userAccounts = new HashMap<>();
	private Map<String, String> employeeAccounts = new HashMap<>();
	
	public Controller() {
		// Read in csv data
		this.userAccounts = readUserData();
		this.employeeAccounts = readEmployeeData();
	}
	
	
	public void chooseView() throws IOException {
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
	 * Runs until user selects 0 to quit program.
	 * 
	 * TODO: Validate input for methods. Ex. lets you check out more than one bike at a time
	 * 
	 * @throws IOException
	 */
	public void executeUser(String username) throws IOException {
		System.out.println("Please choose from the following menu options:\n" + "0. Quit Program.\n"
				+ "1. View station list.\n" + "2. Check out bike.\n" + "3. Check in bike.\n" + "4. View history.\n"
				+ "5. View account info.\n" + "6. Report issue.\n");

		Integer option = this.getIntResponse("Please enter your selection (0-6)", 0, 6);
		
		
		// NOTE: WITHIN SWITCH, VALIDATE INPUT TYPES
		switch (option) {
		/* 0. Quit program */
		case 0:
			// Save all system data to csvs
			// Check if you want to save
			System.out.println("Do you want to save the data? y/n");
			String response = sc.next();
			if (response.equalsIgnoreCase("y")) {
				System.out.println(valleyBike.saveData());
			}
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
		// execute call again after each switch thingy
		executeUser(username);
	}
	
	/**
	 * Menu selector for employee options. 
	 * Runs until employee selects 0 to quit program.
	 * 
	 * TODO: Validate input for methods. Ex. Lets you resolve an issue that does not exist
	 * 
	 * @throws IOException
	 */
	public void executeEmployee() throws IOException {
		System.out.println("Please choose from the following menu options:\n" + "0. Quit Program.\n"
				+ "1. View station list.\n" + "2. View current Rides\n" + "3. View Issues.\n" + "4. Resolve Issues.\n"
				+ "5. Add Station.\n" + "6. View System Overview.\n" + "7. Check Stats.\n" + "8. Add Bikes.\n" + "9. Equalize Stations.\n");

		Integer option = this.getIntResponse("Please enter your selection (0-9)", 0, 9);
		
		
		// NOTE: WITHIN SWITCH, VALIDATE INPUT TYPES
		switch (option) {
		/* 0. Quit program */
		case 0:
			// Save all system data to csvs
			// Check if you want to save
			System.out.println("Do you want to save the data? y/n");
			String response = sc.next();
			if (response.equalsIgnoreCase("y")) {
				System.out.println(valleyBike.saveData());
			}
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
		// execute call again after each switch thingy
		executeEmployee();
		
		
	}
	
	/**
	 * Reads in data from stored .csv file 
	 * Parse values into new objects 
	 * Add objects to a HashMap using key-value pair
	 * 
	 * @return users-passwords hashmap
	 */
	public HashMap<String, String> readUserData() {

		HashMap<String, String> userAccounts = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data-files/controller-users.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				// Parse all values
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
	 * Reads in data from stored .csv file 
	 * Parse values into new objects 
	 * Add objects to a HashMap using key-value pair
	 * 
	 * @return employee-passwords hashmap
	 */
	public HashMap<String, String> readEmployeeData() {

		HashMap<String, String> employeeAccounts = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data-files/controller-employees.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				// Parse all values
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
			} else{
				System.out.println("Invalid credentials");
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
	
	private boolean login(String username, String password) {
		if (userAccounts.containsKey(username) && userAccounts.get(username).equals(password)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean createAccount(String username, String password){
		if (userAccounts.containsKey(username) == true) {
			return false;
		} else {
			Integer membership = getIntResponse("Please enter your preferred membership? (0,1,2)", 0, 2);
			Long cardNum = getUnboundedLongResponse("Please enter you credit card number",(long)0);
			Integer CVV = getIntResponse("Please enter your CVV", 0, 999);
			System.out.println("Please enter expiration date(MM/YY): ");
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
	
	private String checkOutBike(String username) {
		Integer stationId = getUnboundedIntResponse("Please enter your current station", 0);
		return valleyBike.checkOutBike(username, stationId);
		}
	
	private String checkInBike(String username){
		Integer stationId = getUnboundedIntResponse("Please enter your current station", 0);
		return valleyBike.checkInBike(username, stationId);
	}
	
	private String reportIssue(String username){
		Integer stationId = getUnboundedIntResponse("Please enter your current station", 0);
		sc.nextLine(); //throw away the \n not consumed by nextInt()
		System.out.println("Please enter issue message: ");
		String issueMessage = sc.nextLine();
		return valleyBike.reportIssue(username, stationId, issueMessage);
	}
	
	/**
	 * TODO: check to make sure station id is not already taken and within correct range
	 * Maybe set station id from within valleybike? in same way as new ride and new bike?
	 * @return String "success"
	 */
	private String addStation() {
		Integer capacity = getUnboundedIntResponse("Please enter station capacity", 0);
		System.out.println("Please enter whether station has a kiosk or not: (true/false)");
		boolean kiosk = sc.nextBoolean();
		System.out.println("Please enter station address: ");
		String address = sc.nextLine();
		sc.nextLine();
		System.out.println("Please enter station name: ");
		String name = sc.nextLine();
		return valleyBike.addStation(capacity, kiosk, address, name);
	}
	
	/**
	 * TODO: validate station id
	 * @return
	 */
	private String addBikes() {
		Integer numBikes = getIntResponse("How many bikes would you like to add?", 
				0, 100);
		sc.nextLine();
		Integer stationId = getIntResponse("What station would you like to put the bikes at?", 0, 100);
		return valleyBike.addBikes(stationId, numBikes);
	}
	
	private String moveBikes() {
		Integer stationFrom = getUnboundedIntResponse("Please enter station ID to move from", 0);
		sc.nextLine();
		Integer stationTo = getUnboundedIntResponse("Please enter station ID to move to", 0);
		sc.nextLine();
		Integer numBikes = getUnboundedIntResponse("Please enter number of bikes to move", 0);
		sc.nextLine();
		return valleyBike.moveBikes(stationFrom, stationTo, numBikes);
	}
	
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
	public static void main(String[] args) {
		Controller controller = new Controller();
		try {
			controller.chooseView();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
