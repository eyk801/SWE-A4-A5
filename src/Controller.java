
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Controller {
	ValleyBikeSim valleyBike = new ValleyBikeSim();
	private Scanner sc = new Scanner(System.in);
	private Map<String, String> userAccounts = new HashMap<>();
	//private Map<String, String> employeeAccounts = new HashMap<>();
	//TODO: Read in CSV file of current accounts
	
	public void chooseView() throws IOException {
		System.out.println("Welcome to ValleyBike! "
				+ "Please enter 'user' for user and 'employee' for employee: ");
		String response = sc.next();
		if (response == "user") {
			String username = accountLogin();
			executeUser(username);
		} else if (response == "employee") {
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
			sc.close();
			System.out.println("Thank you for using ValleyBike, have a great day!");
			System.exit(0);
			break;
		case 1:
			valleyBike.viewStationList();
			break;
		case 2:
			// Take in all ride data
			checkOutBike(username);
			break;
		case 3: 
			checkInBike(username);
			break;
		case 4:
			valleyBike.viewHistory(username);
			break;
		case 5:
			valleyBike.viewAccount(username);
			break;
		case 6:
			reportIssue(username);
			break;
		default:
			System.out.println("Input must be an integer from 0-6.");
			executeUser(username);
		}
		// execute call again after each switch thingy
		executeUser(username);
	}
	
	public void executeEmployee() {
		// ALL SWITCH STATEMENTS FOR COMPANY METHODS
		
		// viewStationList()
		// viewCurrentRides()
		// equalizeStations()
		// moveBike() ??????
		// viewIssues()
		// resolveIssue()
		// addBike() - 
		// addStation()
		// checkStats() - resolveRideData() sorta
		
		System.out.println("Please choose from the following menu options:\n" + "0. Quit Program.\n"
				+ "1. View station list.\n" + "2. View current Rides\n" + "3. View Issues.\n" + "4. Resolve Issues.\n"
				+ "5. Add Station.\n" + "6. Equalize stations.\n" + "7. Check Stats.\n" + "8. Add Bikes.\n" + "9. Move Bikes.\n");

		Integer option = this.getIntResponse("Please enter your selection (0-9)", 0, 9);
		
		
		// NOTE: WITHIN SWITCH, VALIDATE INPUT TYPES
		switch (option) {
		/* 0. Quit program */
		case 0:
			sc.close();
			System.out.println("Thank you for using ValleyBike, have a great day!");
			System.exit(0);
			break;
		case 1:
			valleyBike.viewStationList();
			break;
		case 2:
			// Take in all ride data
			valleyBike.viewCurrentRides();
			break;
		case 3: 
			valleyBike.viewIssues();
			break;
		case 4:
			resolveIssues();
			break;
		case 5:
			addStation();
			break;
		case 6:
			valleyBike.equalizeStations();
			break;
		case 7:
			valleyBike.checkStats();
			break;
		case 8:
			//autogenerate ids, give an int of how many bikes to add, dock at a specific station
			addBikes();
			break;
		case 9:
			moveBikes();
			break;
		default:
			System.out.println("Input must be an integer from 0-8.");
			executeEmployee();
		}
		// execute call again after each switch thingy
		executeEmployee();
		
		
	}
	
	private String accountLogin() {
		String username = "";
		System.out.println("Would you like to login or create a new account? "
				+"Type 'l' for login and 'n' for new account: " );
		String choice = sc.next().toLowerCase();
		if (choice == "l") {
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
		} else if (choice == "n") {
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
		if (userAccounts.containsKey(username) && userAccounts.get(username)== password) {
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
			Integer cardNum = getUnboundedIntResponse("Please enter you credit card number",0);
			Integer CVV = getIntResponse("Please enter your CVV", 0, 999);
			System.out.println("Please enter experation date(MM/YY): ");
			String expDate = sc.next(); 
			userAccounts.put(username, password);
			if (valleyBike.createUser(username, password, membership, cardNum, CVV, expDate) == true) {
				userAccounts.put(username, password);				
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
		System.out.println("Please enter issue message: ");
		String issueMessage = sc.nextLine();
		return valleyBike.reportIssue(username, issueMessage);
	}
	
	private void addStation() {
		Integer id = getUnboundedIntResponse("Please enter station ID", 0);
		Integer capacity = getUnboundedIntResponse("Please enter station capacity", 0);
		Integer kiosk = getUnboundedIntResponse("Please enter number of kiosks", 0);
		System.out.println("Please enter station address: ");
		String address = sc.nextLine();
		System.out.println("Please enter station name: ");
		String name = sc.nextLine();
		valleyBike.addStation(id, capacity, kiosk, address, name);
	}
	
	private String addBikes() {
		Integer numBikes = getIntResponse("How many bikes would you like to add?", 
				0, 100);
		return valleyBike.addBikes(numBikes);
	}
	
	private String moveBikes() {
		Integer stationFrom = getUnboundedIntResponse("Please enter station ID to move from", 0);
		Integer stationTo = getUnboundedIntResponse("Please enter station ID to move to", 0);
		Integer numBikes = getUnboundedIntResponse("Please enter number of bikes to move", 0);
		return valleyBike.moveBikes(stationFrom, stationTo, numBikes);
	}
	
	private String resolveIssues() {
		ArrayList<Integer> issues = new ArrayList<Integer>();
		String response = "y";
		while (response == "y") {
			Integer issue = getUnboundedIntResponse("Please enter issue number", 0);
			issues.add(issue);
			System.out.println("Would you like to add another issue to resolve? (y/n): ");
			response = sc.next().toLowerCase();
		}
		return valleyBike.resolveIssues(issues);
	}
}
