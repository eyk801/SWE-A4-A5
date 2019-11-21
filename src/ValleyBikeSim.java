/*
 * hello
 */

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;
import java.sql.Timestamp;

public class ValleyBikeSim {
	private Map<Integer, Station> stationData = new HashMap<>();
	private Map<String, User> users = new HashMap<>();
	private Map<Integer, Bike> bikes = new HashMap<>();
	private Map<Integer, Ride> rides = new HashMap<>();
	private List<Integer> currRides = new ArrayList<>();
	private Integer rideID = 0;


	/**
	 * ValleyBikeSim class constructor
	 * 
	 * TODO: Remove and re-organize program so that this is no longer necessary
	 * or so that the object does more - Alicia says object could be potentially useful
	 * can also avoid the issue with static method stuff
	 */
	public ValleyBikeSim() {
		this.stationData = readStationData();

		// CREATE CSVS
		
		//this.users = readUserData();
		//this.bikes = readBikeData();
		// this.rides = readRideData();
	}

	/**
	 * Read in station data from file 
	 * Parse values into new station objects 
	 * and add objects to a Hashmap using id-station key-value pair
	 *
	 * @return stationData hashmap for the whole vallybikesim object to access
	 */
	public HashMap<Integer, Station> readStationData() {

		TreeMap<Integer, Station> stationData = new TreeMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data-files/station-data.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				int id = Integer.parseInt(values[0]);
				Station station = new Station(id, Integer.parseInt(values[2]), 
						Integer.parseInt(values[3]), Integer.parseInt(values[4]), Integer.parseInt(values[5]), 
						Integer.parseInt(values[6]), Integer.parseInt(values[7]), values[8], values[1]);
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
	 */
	public void viewStationList() {
		System.out.println("ID	Bikes	Pedelecs	AvDocs	MainReq	Cap	Kiosk	Name - Address");
		Iterator<Integer> keyIterator = stationData.keySet().iterator();
		while(keyIterator.hasNext()){
			Integer id = (Integer) keyIterator.next();
			System.out.println(this.stationData.get(id).toViewString());
		}
	}

	/**
	 * 
	 * Function that reads in a ride data file that contains all the rides for one
	 * day of service and outputs stats for the day
	 * 
	 * TODO: Ok for now but could simulate bike movement by pushing events to stack to further validate
	 * and check stations for Available Docks, pedelecs, etc
	 * 
	 * Employee can access, but not user
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
					Integer from = Integer.parseInt(values[1]);
					Integer to = Integer.parseInt(values[2]);
					if (Integer.parseInt(startTime.split(" ")[1].split(":")[0]) >= 24
							|| Integer.parseInt(endTime.split(" ")[1].split(":")[0]) >= 24
							|| stationData.containsKey(to) == false
							|| stationData.containsKey(from) == false){ continue; }
					Date tempStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
					Date tempEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);

					if (tempEnd.before(tempStart)){ continue; }
					
					totalTime += ((int) (((tempEnd.getTime() - tempStart.getTime())) / (60 * 1000)));
					totalRides ++;
						
					
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
	 * TODO: Save rides to ride list? Further ride validation? Waiting on Alicia and team consensus
	 * CHANGE - two methods, checkInBike() and checkOutBike()
	 * 
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
	 * Adds station to program's treemap of stations
	 * Station ID is automatically assigned
	 * 
	 * 
	 * Company method
	 */
	public void addStation(Integer id,Integer capacity,Integer kiosk,String address,String name) {

//		int id = this.getIntResponse("Please enter the id of the station you would like to edit/add", 0, 1000);
//		if (stationData.get(id) != null){
//			System.out.println("You are attempting to edit station "+stationData.get(id).getName() + 
//					" continue? y/n: ");
//			if (sc.next().toLowerCase().equals("n")){
//				return;
//			}
//		}
//		System.out.println("Please enter the name of this station: ");
//		sc.nextLine();
//		String name = sc.nextLine();
//
//		System.out.println("Please enter the address of this station: ");
//		String address = sc.nextLine();
//
//		int capacity = this.getIntResponse("Please enter the integer capacity of this station", pedelecs+bikes, 1000);
//		int kiosk = this.getIntResponse("Please enter the number of kiosks at this station", 0, 5);

		Station s = new Station(id, 0, 0, capacity, 0, capacity, kiosk, address, name);
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
				writer.write(s.toSaveString());
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
	 * by iterating through each station adding 1 if it is below percentage
	 * and continuing on while there are still extras to reassign
	 * 
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
	 * Check out bike method for user
	 * TODO check membership status and charge account accordingly
	 * @param username
	 * @param stationId 
	 * @param bikeID
	 * @return
	 */
	public String checkOutBike(String username, Integer stationId, Integer bikeID) {
		// assign ride ID
		rideID += 1;

		// check membership status and charge accordingly, add ride to user ride list
		Iterator userIterator = users.entrySet().iterator();
		while(userIterator.hasNext()){
			Map.Entry user = (Map.Entry)userIterator.next();
			if (user.getKey().equals(username)) {
				User currentUser = users.get(user);
				// membership type 0 = pay-per-ride
				if (currentUser.getType() == 0) {
					System.out.println("Pay-per-ride. Your card will be charged $2");
				}				
				currentUser.addUserRide(rideID);
			}
		}

		// create new Ride and add to current rides list
		Ride ride = new Ride(rideID, username, stationId, bikeID);
		currRides.add(rideID);

		// iterate through bikes hashmap to find bike and update values to show in use
		Iterator<Integer> bikeKeyIterator = bikes.keySet().iterator();
		while(bikeKeyIterator.hasNext()){
			Integer id = (Integer) bikeKeyIterator.next();
			if (id == bikeID) {
				Bike b = bikes.get(id);
				b.setCheckedOut(true);
				b.setLastStationId(stationId);
				b.setUserId(username);
			}
		}		
		// add ride to rides hashmap
		rides.put(rideID, ride);

		return "Bike " + bikeID + " successfully checked out by " + username + ", Ride ID " + rideID;
	};
	
	/**
	 * Check in bike method for user
	 * TODO: Implement method
	 * @param username
	 * @param stationId 
	 * @param ridID
	 * @return String "success"
	 */
	public String checkInBike(String username, Integer stationId, Integer rideID) {

		// iterate through rides hashmap to find ride and call end method
		Iterator<Integer> rideKeyIterator = rides.keySet().iterator();
		while(rideKeyIterator.hasNext()){
			Integer rID = (Integer) rideKeyIterator.next();
			if (rID == rideID) {
				Ride r = rides.get(rID);
				int bikeID = r.getBikeId();
				// iterate through rides hashmap to find bike and update values
				Iterator<Integer> bikeKeyIterator = bikes.keySet().iterator();
				while(bikeKeyIterator.hasNext()){
					Integer bID = (Integer) bikeKeyIterator.next();
					if (bID == bikeID) {
						Bike b = bikes.get(bID);
						b.setLastStationId(stationId);
						b.setUserId("");
						b.setCheckedOut(false);
					}
				}
				r.end(stationId);
			}
		}

		return "Successfully checked in.";
	}
	
	/**
	 * View user history
	 * TODO: Implement method
	 * @param username
	 * @return
	 */
	public String viewHistory(String username) {
		Iterator userIterator = users.entrySet().iterator();
		while(userIterator.hasNext()){
			Map.Entry user = (Map.Entry)userIterator.next();
			if (user.getKey().equals(username)) {
				User currentUser = users.get(user);
				System.out.println("Ride History: \n");
				for (int r : currentUser.getRides()) {
					System.out.println("Ride " + r);
				}
			}
		}
		return "";
	}
	
	/**
	 * view account information for user
	 * TODO: Implement method
	 * @param username
	 * @return
	 */
	public String viewAccount(String username) {
		Iterator userIterator = users.entrySet().iterator();
		while(userIterator.hasNext()){
			Map.Entry user = (Map.Entry)userIterator.next();
			if (user.getKey().equals(username)) {
				User currentUser = users.get(user);
				System.out.println("Account Information: \n" + currentUser.toViewString());
			}
		}
		return "";
	}
	
	//TODO: Implement method
	public String reportIssue(String username, String issueMessage) {
		return "Issue reported";
	}
	
	//TODO: Implement method
	public String checkStats() {
		String systemStats = "System Stats: " + "\n" + "\n" + "Stations:" + "\n";
		systemStats.concat("ID	Bikes	Pedelecs	AvDocs	MainReq	Cap	Kiosk	Name - Address");
		Iterator<Entry<Integer, Station>> stationsIterator = stationData.entrySet().iterator();
		while(stationsIterator.hasNext()){
			Map.Entry<Integer, Station> stationElement = (Map.Entry<Integer, Station>)stationsIterator.next();
			systemStats.concat("\n" + stationElement.toViewString());
			Map<Integer, Bike> stationBikes = stationElement.bikes;
			Iterator<Entry<Integer, Bike>> stationBikeIterator = stationBikes.entrySet().iterator();
			systemStats.concat("\n" + "Station Bikes: ");
			while (stationBikeIterator.hasNext()) {
				Map.Entry<Integer, Bike> stationBikeElement = (Map.Entry<Integer, Bike>)stationBikeIterator.next();
				systemStats.concat(Integer.toString(stationBikeElement.getKey()) + " ");
			}
			}
		systemStats.concat("Bikes currently checked out: \n");
		systemStats.concat("Bike \t User \n");

		Iterator<Entry<Integer, Bike>> bikeIterator = bikes.entrySet().iterator();
		while (bikeIterator.hasNext()) {
			Map.Entry<Integer, Bike> bikeElement = (Map.Entry<Integer, Bike>)bikeIterator.next();
			if (bikeElement.getValue().checkedOut == true) {
				systemStats.concat(Integer.toString(bikeElement.getValue().getId()) + "\t" + bikeElement.getValue().userId + "\n");
			}

		}
		return "system stats";
	}
	
	//TODO: Implement method
	public String viewIssues() {
		return "current issues";
	}
	
	/**
	 * Iterates through hashmap of rides
	 * picks out current rides and concatenates them to string
	 * 
	 * @return string of current rides to controller
	 */
	public String viewCurrentRides() {
		String currentRides = "Current Rides:";
		Iterator<Entry<Integer, Ride>> ridesIterator = rides.entrySet().iterator();
		while(ridesIterator.hasNext()){
			Map.Entry<Integer, Ride> mapElement = (Map.Entry<Integer, Ride>)ridesIterator.next();
			if (mapElement.getValue().isCurrentRide());{
				currentRides = "/n" + Integer.toString(mapElement.getValue().getId());
			}
		}
		return currentRides;
	}
	
	// TODO Implement method
	public String addBikes(Integer numBikes) {
		return numBikes.toString() + " bikes added";
	}
	
	// TODO Implement method
	public String moveBikes(Integer stationFrom, Integer stationTo, Integer numBikes) {
		return null;
	}
	
	// TODO Implement method
	public String resolveIssues(ArrayList<Integer> issues) {
		return issues.toString() + " resolved";
	}
	
	/**
	 * Method creates a new user object
	 * Checks if payment params are valid
	 * and adds it to user hashmap if so 
	 *
	 * @return boolean to controller for whether or not user was successfully added to system
	 */
	public boolean createUser(String username, String password, Integer membership, Integer cardNum, Integer CVV, String expDate) {

		User newUser = new User(username,password,membership,cardNum,CVV, expDate);
		if (newUser.validUser() == true) {
			users.put(username, newUser);
			return true;
		} else {
			return false;
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