
/*
 * Assignment 1: ValleyBike Simulator
 * Authors: Mai Ngo, Jemimah Charles
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class ValleyBikeSim {
	private HashMap<Integer, Station> stationData;
	private Scanner sc = new Scanner(System.in);

	/**
	 * ValleyBikeSim object constructor
	 * 
	 * TODO: Remove and re-organize program so that this is no longer necessary
	 */
	public ValleyBikeSim() {
		this.stationData = readStationData();
	}

	/**
	 * Read in station data from file 
	 * Parse values into new station objects 
	 * and add objects to a Hashmap using id-station key-value pair
	 *
	 * @return stationData hashmap for the whole vallybikesim object to access
	 */
	public HashMap<Integer, Station> readStationData() {

		HashMap<Integer, Station> stationData = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data-files/station-data.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				int id = Integer.parseInt(values[0]);
				Station station = new Station(id, Integer.parseInt(values[2]), 
						Integer.parseInt(values[3]), Integer.parseInt(values[4]), Integer.parseInt(values[5]), 
						Integer.parseInt(values[6]), (values[7].equals("1")), values[8], values[1]);
				stationData.put(id, station);
			}
			br.close();
			return stationData;

		} catch (Exception e) {
			System.err.format("Exception occurred trying to read station data file.");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Function to print out list of stations ordered by id and formatted to console.
	 * 
	 * TODO: Add error handling, potentially use key iterator instead of extra data structure
	 */

	public void viewStationList() {

		ArrayList<Integer> idList = new ArrayList<Integer>(this.stationData.keySet());
		Collections.sort(idList);

		System.out.println("ID	Bikes	Pedelecs	AvDocs	MainReq	Cap	Kiosk	Name - Address");
		for (int id : idList) {
			System.out.println(this.stationData.get(id).toString());
		}
	}

	/**
	 * 
	 * Function that reads in a ride data file that contains all the rides for one
	 * day of service and outputs stats for the day
	 * 
	 * TODO: Ride validation
	 */

	public void resolveRideData() {

		boolean cont = true;
		int totalTime = 0;
		int totalRides = 0;

		while (cont) {
			// prompt for address
			String path = "";
			do {
				System.out.println(
						"What is the path of the ride data file (Example: data-files/sample-ride-data-0820.csv)? ");
				while (!sc.hasNext()) {
					System.out.println("Please try again, input must be a valid file path. ");
					sc.nextLine();
				}
				path = sc.nextLine();
			} while (path.length() < 4);
			try {
				BufferedReader br = new BufferedReader(new FileReader(path));
				String line = br.readLine();
				while ((line = br.readLine()) != null) {
					String[] values = line.split(",");
					String startTime = values[3];
					String endTime = values[4];
					Date tempStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
					if (endTime.split(" ")[1].split(":")[0].equals("24")) {
						int tempEndMinute = Integer.parseInt(endTime.split(" ")[1].split(":")[1]);
						Date midnight = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.parse(startTime.split(" ")[0] + " 23:59:00");
						totalTime += (int) ((midnight.getTime() - tempStart.getTime()) / (60 * 1000));
						totalTime += tempEndMinute + 1;
					} else {
						Date tempEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);
						totalTime += ((int) (((tempEnd.getTime() - tempStart.getTime())) / (60 * 1000)));
					}
					totalRides++;
				}
				cont = false;
				br.close();
			} catch (Exception e) {
				System.err.format("Exception occurred trying to read station data file.");
				e.printStackTrace();
			}
		}
		int avg = totalTime / totalRides;
		System.out.println("This ride list contains: " + Integer.toString(totalRides)
				+ " rides with average ride time of " + Integer.toString(avg) + " minutes.");
	}

	/**
	 * Function to record a user's ride. It will update the station's data after all
	 * information has been verified.
	 * 
	 * ASSUMPTION: There is no possibility to rent a bike. Only pedelecs are
	 * available.
	 * 
	 * TODO: Potentially save rides to ride list? Further ride validation?
	 */
	public void recordRide() {
		
		// list of existing stations
		ArrayList<Integer> idList = new ArrayList<Integer>(this.stationData.keySet());
		int userStartStation;
		int userEndStation;

		do {
			System.out.println(
					"Please enter Starting Station ID: ");
			while (!sc.hasNextInt()) {
				System.out.println(
						"The station ID must be an integer");
				sc.next();
			}
			userStartStation = sc.nextInt();
		} while (!(idList.contains(userStartStation))
				|| (idList.contains(userStartStation) && ((stationData.get(userStartStation).getPedelecs() < 1))));

		// prompt user for the station where the ride ended
		
		do {
			System.out.println("Please enter Docking Station ID: ");
			while (!sc.hasNextInt()) {
				System.out.println("The station ID must be an integer");
				sc.next();
			}
			userEndStation = sc.nextInt();
		} while (!idList.contains(userEndStation)
				|| (stationData.get(userEndStation).getAvDocks() - 1 < 0));

			Station endStation = stationData.get(userEndStation);
			Station startStation = stationData.get(userStartStation);
			int endPed = stationData.get(userEndStation).getPedelecs();
			int startPed = stationData.get(userStartStation).getPedelecs();

			// updating the station data
			endStation.setAvDocks(stationData.get(userEndStation).getAvDocks() - 1);
			startStation.setAvDocks(stationData.get(userStartStation).getAvDocks() + 1);

			endStation.setPedelecs(endPed + 1);
			startStation.setPedelecs(startPed - 1);
			
			System.out.println("Ride sucessfully added");
			
	}

	/**
	 * Adds station to program's hashmap of stations
	 * Station ID is automatically assigned
	 * TODO: Add station editing
	 */
	public void addStation() {

		System.out.println("Please enter the name of this station: ");
		sc.nextLine();
		String name = sc.nextLine();

		System.out.println("Please enter the address of this station: ");
		String address = sc.nextLine();


		int bikes = this.getIntResponse("Please enter the number of bikes for this station",0,1000);
		int pedelecs = this.getIntResponse("Please enter the number of pedelecs for this station",0, 1000);
		int capacity = this.getIntResponse("Please enter the integer capacity of this station", pedelecs+bikes, 1000);
		
		System.out.println("Does this station have a kiosk? y/n:");
		while (!sc.hasNext()) {
			System.out.println("Input must be y or n.");
			System.out.println("Does this station have a kiosk? Y/N:");
			sc.next();
		}
		boolean kiosk = (sc.next().toLowerCase().equals("y"));
		
		// initializing new station
		int id = Collections.max(this.stationData.keySet()) + 1;

		Station s = new Station(id, bikes, pedelecs, capacity - bikes - pedelecs, 0, capacity, kiosk, address, name);
		this.stationData.put(id, s);
		System.out.println("Station successfully added to the system.");
	}

	/**
	 * Overwrites current station data file with the station updates in the program
	 * (rides, new stations, etc)
	 * 
	 * @throws IOException
	 * TODO: Handle exceptions/errors
	 */

	public void saveStationList() throws IOException {
		FileWriter writer = new FileWriter("data-files/station-data.csv");
		writer.write("ID,Name,Bikes,Pedelecs,Available Docks,Maintainence Request,Capacity,Kiosk,Address");
		
			for (Station s : this.stationData.values()) {
				writer.write("\n");
				writer.write(s.toString());
			}
			writer.flush();
			writer.close();
		System.out.println("Station list successfully saved.");
	}

	/**
	 * Function to move bike/pedelecs between stations, so that each station is
	 * equalized (based on the percentages of bike/pedelecs per dock capacity). 
	 * 
	 * Does not prioritize larger capacity stations. 
	 * Essentially empties all stations and then gradually reassigns
	 * 
	 * TODO: Correct method
	 */
	public void equalizeStations() {

		// find the total number of bikes, pedelecs, and total capacity
		int totalBikes = 0;
		int totalPedelecs = 0;
		int totalCap = 0;

		for (Station s : this.stationData.values()) {
			totalBikes += s.getBikes();
			totalPedelecs += s.getPedelecs();
			totalCap += s.getCapacity();
			s.setBikes(0);
			s.setPedelecs(0);
			s.setAvDocks(s.getCapacity());
		}
		// find the average percentage of bikes/pedelecs to capacity
		double percentBikes = (double) totalBikes / (double) totalCap;
		double percentPeds = (double) totalPedelecs / (double) totalCap;
		
		while(totalBikes + totalPedelecs > 0){
			for (Station s : this.stationData.values()){
				double sPercentBikes = (double)s.getBikes()/s.getCapacity();
				double sPercentPeds = (double)s.getPedelecs()/s.getCapacity();
				
				if(totalBikes >0){
					if(sPercentBikes < percentBikes){
						s.setBikes(s.getBikes() + 1);
						s.setAvDocks(s.getAvDocks() - 1);
						totalBikes = totalBikes - 1;
					}
				}
				if(totalPedelecs>0){
					if(sPercentPeds < percentPeds){
						s.setPedelecs(s.getPedelecs() + 1);
						s.setAvDocks(s.getAvDocks() - 1);
						totalPedelecs = totalPedelecs - 1;
					}
				}
			}
		}
		System.out.println("The number of bikes and pedelecs at all stations have been equalized.");
	}

	/**
	 * Menu selector for user options. 
	 * Runs until user selects 0 to quit program.
	 * 
	 * @throws IOException
	 */
	public void execute() throws IOException {

		System.out.println("Please choose from the following menu options:\n" + "0. Quit Program.\n"
				+ "1. View station list.\n" + "2. Add station.\n" + "3. Save station list.\n" + "4. Record ride.\n"
				+ "5. Resolve ride data.\n" + "6. Equalize stations.\n");

		Integer option = this.getIntResponse("Please enter your selection (0-6)", 0, 6);
	
		switch (option) {
		/* 0. Quit program */
		case 0:
			sc.close();
			System.out.println("Thank you for using ValleyBike, have a great day!");
			System.exit(0);
			break;
		case 1:
			this.viewStationList();
			break;
		case 2:
			this.addStation();
			break;
		case 3:
			saveStationList();
			break;
		case 4:
			recordRide();
			break;
		case 5:
			resolveRideData();
			break;
		case 6:
			equalizeStations();
			break;
		default:
			System.out.println("Input must be an integer from 0-6.");
			this.execute();
		}
	}
	
	/**
	 * Main function. Creates new ValleyBikeSim Object, reads station data for that object
	 * And then calls the execute function which runs simulator until program is quit
	 * 
	 * TODO: Remove ValleyBikeSim Object and find more streamlined way to run program
	 */
	public static void main(String[] args) {
		ValleyBikeSim sim = new ValleyBikeSim();
		sim.readStationData();
		System.out.println("Welcome to the ValleyBike Simulator.\n");
		// calls option menu
		try {
			sim.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Function to get integer input from user with ability to dictate minimum entries
	 * 
	 * @param request - quality that is being asked for
	 * @param min - minimum integer value to be accepted
	 * @return validated user input
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
}