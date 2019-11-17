
import java.io.*;
import java.util.*;

public class Controller {

	private Scanner sc = new Scanner(System.in);
	
	/**
	 * Menu selector for user options. 
	 * Runs until user selects 0 to quit program.
	 * 
	 * @throws IOException
	 */
	public void execute() throws IOException {
		
		
		// Log in stuff (user vs company)
		
		
		// IF USER:
	
		ValleyBikeSim user = new ValleyBikeSim();
		
		
		// input username and password
		// user.checkCredentials()
		
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
			user.viewStationList();
			break;
		case 2:
			// Take in all ride data
			user.checkOutBike();
			break;
		case 3: 
			user.checkInBike();
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
			execute();
		}
		
		
		// if company
		ValleyBikeSim company = new ValleyBikeSim();
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
		

	
		// execute call again after each switch thingy
		execute();
	}
}
