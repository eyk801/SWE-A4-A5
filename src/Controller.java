
import java.io.*;
import java.util.*;

public class Controller {

	private Scanner sc = new Scanner(System.in);
	private Map<String, String> userAccounts = new HashMap<>();
	//private Map<String, String> employeeAccounts = new HashMap<>();
	//TODO: Read in CSV file of current accounts
	
	public void chooseView() throws IOException {
		System.out.println("Welcome to ValleyBike! "
				+ "Please enter 'user' for user and 'employee' for employee: ");
		String response = sc.next();
		ValleyBikeSim valleyBike = new ValleyBikeSim();
		if (response == "user") {
			String username = accountLogin();
			executeUser(valleyBike, username);
		} else if (response == "employee") {
			//TODO: Add employee login in A5
			executeEmployee(valleyBike);
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
	public void executeUser(ValleyBikeSim valleyBike, String username) throws IOException {
		System.out.println("Please choose from the following menu options:\n" + "0. Quit Program.\n"
				+ "1. View station list.\n" + "2. Add station.\n" + "3. Save station list.\n" + "4. Record ride.\n"
				+ "5. Resolve ride data.\n" + "6. Equalize stations.\n");

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
			//user.checkOutBike();
			break;
		case 3: 
			//user.checkInBike();
			break;
		case 4:
			//user.viewHistory();
			break;
		case 5:
			//user.viewInfo();
			break;
		case 6:
			//user.reportIssue();
			break;
		default:
			System.out.println("Input must be an integer from 0-6.");
			executeUser(valleyBike, username);
		}
		// execute call again after each switch thingy
		executeUser(valleyBike, username);
	}
	
	
	public void executeEmployee(ValleyBikeSim valleyBike) {
		// ALL SWITCH STATEMENTS FOR COMPANY METHODS
		
		// viewStationList()
		// viewCurrentRides()
		// equalizeStations()
		// moveBike() ??????
		// viewIssues()
		// resolveIssue()
		// addBike() - autogenerate ids, give an int of how many bikes to add, dock at a specific station
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
			//valleyBike.viewCurrentRides();
			break;
		case 3: 
			//valleyBike.viewIssues();
			break;
		case 4:
			//valleyBike.resolveIssues();
			break;
		case 5:
			//valleyBike.addStation();
			break;
		case 6:
			//valleyBike.equalizeStation();
			break;
		case 7:
			//valleyBike.checkStats();
			break;
		case 8:
			//valleyBike.addBikes();
			break;
		case 9:
			//valleyBike.moveBikes();
			break;
		default:
			System.out.println("Input must be an integer from 0-8.");
			executeEmployee(valleyBike);
		}
		// execute call again after each switch thingy
		executeEmployee(valleyBike);
		
		
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
				System.out.println("Error creating account (username taken)");
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
			//choose membership type
			userAccounts.put(username, password);
			//call create user on ValleyBikeSim
			return true;
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
	
}
