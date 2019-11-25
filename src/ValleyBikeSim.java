/*
 * DESCRIPTION OF THE VALLEYBIKESIM
 */

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;
import java.sql.Timestamp;

public class ValleyBikeSim {
	private Map<Integer, Station> stations = new HashMap<>();
	private Map<String, User> users = new HashMap<>();
	private Map<Integer, Bike> bikes = new HashMap<>();
	private Map<Integer, Ride> rides = new HashMap<>();
	private Map<Integer, MainReq> mainReqs = new HashMap<>();
	private List<Integer> currRides = new ArrayList<>();
	private Integer lastRideId = 0;
	private Integer lastBikeId = 0;
	private PaymentSys paymentSystem = new PaymentSys();
	private Integer lastMainReqId = 0;

	/**
	 * ValleyBikeSim class constructor
	 * Calls all methods to read csv data files
	 * Populates class HashMaps and currRides List
	 * 
	 */
	public ValleyBikeSim() {
		this.stations = readStationData();
		this.users = readUserData();
		this.bikes = readBikeData();
		this.rides = readRideData();
		this.mainReqs = readMainReqData();
	}

	/**
	 * Reads in station data from stored .csv file 
	 * Parse values into new Station objects 
	 * Add objects to a HashMap using stationId:Station key-value pair
	 * 
	 * @return stations HashMap for the whole ValleyBikeSim object to access
	 */
	public HashMap<Integer, Station> readStationData() {

		HashMap<Integer, Station> stations = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data-files/station-data.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				// Parse all values
				int id = Integer.parseInt(values[0]);
				String name = values[1];
				int avDocks = Integer.parseInt(values[2]);
				int cap = Integer.parseInt(values[3]);
				boolean kiosk = Boolean.parseBoolean(values[4]);
				String address = values[5];
				List<Integer> bikeIds = new ArrayList<>();
				// Loop to end of csv line for all bikeIds
				for (int i=6; i < values.length;i++) {
					// Add bike ids to ArrayList
					bikeIds.add(Integer.parseInt(values[i]));
				}
				// Create station object using parsed data
				Station station = new Station(id, avDocks, cap, kiosk, address, name, bikeIds);
				// Add station to stations
				stations.put(id, station);
			}
			br.close();
			return stations;
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read station data file.");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Reads in data from stored .csv file 
	 * Parse values into new objects 
	 * Add objects to a HashMap using key-value pair
	 * 
	 * @return users HashMap for the whole ValleyBikeSim object to access
	 */
	public HashMap<String, User> readUserData() {

		HashMap<String, User> users = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data-files/user-data.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				// Parse all values
				String username = values[0];
				List<Integer> rideHistory = new ArrayList<>();
				User user = new User(username,values[1],Integer.parseInt(values[2]),
									Long.parseLong(values[3]),Integer.parseInt(values[4]),
									values[5]);
				// Loop to end of line for all ride history
				for (int i=8; i < values.length;i++) {
					rideHistory.add(Integer.parseInt(values[i]));
				}
				// Add ride history to ride object
				user.setRides(rideHistory);
				// Add user to users hashmap
				users.put(username,user);
			}
			br.close();
			return users;
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read user data file.");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Reads in data from stored .csv file 
	 * Parse values into new bike objects 
	 * Add objects to a HashMap using key-value pair
	 * 
	 * @return bikes HashMap for the whole ValleyBikeSim object to access
	 */
	public HashMap<Integer, Bike> readBikeData() {

		HashMap<Integer, Bike> bikes = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data-files/bike-data.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				// Parse all values
				int id = Integer.parseInt(values[0]);
				// Create bike object
				Bike bike = new Bike(id, Integer.parseInt(values[1]),
									values[2],Boolean.parseBoolean(values[3]));
				// Add bike object to bikes hash
				bikes.put(id,bike);
				// Update this.lastBikeId var
				if (id > this.lastBikeId) {
					this.lastBikeId = id;
				}
			}
			br.close();
			return bikes;
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read bike data file.");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Reads in data from stored .csv file
	 * Parse values into new ride objects
	 * Add objects to Hashmap using key-value pair
	 * Adds current rides to currRides list
	 * 
	 * @return rides Hashmap
	 */
	public HashMap<Integer, Ride> readRideData() {

		HashMap<Integer, Ride> rides = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data-files/ride-data.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				// Parse all values
				int id = Integer.parseInt(values[0]);
				Ride ride = new Ride(id, values[1], Integer.parseInt(values[2]),Integer.parseInt(values[3]));
				// Set end station id
				ride.setEndStation(Integer.parseInt(values[4]));
				// Reset timestamps
				ride.setStartTime(values[5]);
				ride.setEndTime(values[6]);
				// Set currentRide boolean
				ride.setCurrentRide(Boolean.parseBoolean(values[4]));
				// If current ride == true, add ride id to currRides list
				if (Boolean.parseBoolean(values[4])) {
					currRides.add(id);
				}
				// Add ride to rides hash
				rides.put(id,ride);
				// Update this.lastRideId var
				if (id > this.lastRideId) {
					this.lastRideId = id;
				}
				
			}
			br.close();
			return rides;
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read ride data file.");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Reads in data from stored .csv file 
	 * Parse values into new mainReq objects 
	 * Add objects to a HashMap using key-value pair
	 * 
	 * @return mainReqs HashMap for the whole ValleyBikeSim object to access
	 */
	public HashMap<Integer, MainReq> readMainReqData() {

		HashMap<Integer, MainReq> mainReqs = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data-files/mainreq-data.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				// Parse all values
				int id = Integer.parseInt(values[0]);
				// Create new mainreq object
				MainReq req = new MainReq(id, values[1], Integer.parseInt(values[2]), values[3]);
				// Add req to hash
				mainReqs.put(id,req);
				// Update this.lastMainReqId var
				if (id > this.lastMainReqId) {
					this.lastMainReqId = id;
				}
			}
			br.close();
			return mainReqs;
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read maintenance requests data file.");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Overwrites current data files with the station updates in the program
	 * (rides, new stations, etc)
	 * 
	 * @throws IOException
	 * TODO: test more extensively
	 */
	public String saveData() throws IOException {
		// Save station data
		FileWriter stationWriter = new FileWriter("data-files/station-data.csv");
		stationWriter.write("ID,Name,Available Docks,Capacity,Kiosk,Address,Bike Ids\n");
			for (Station s : this.stations.values()) {
				stationWriter.write(s.toSaveString());
			}
			stationWriter.flush();
			stationWriter.close();
		// Save user data
		FileWriter userWriter = new FileWriter("data-files/user-data.csv");
		userWriter.write("Username,Password,Membership Type,Credit Card Num,CVV,Expiration Date,Current Ride,Ride History\n");
			for (User u : this.users.values()) {
				userWriter.write(u.toSaveString());
			}
			userWriter.flush();
			userWriter.close();
		// Save ride data
		FileWriter rideWriter = new FileWriter("data-files/ride-data.csv");
		rideWriter.write("ID,Username,Bike Id,Start Station Id,End Station Id,Start Time,End Time\n");
			for (Ride r : this.rides.values()) {
				rideWriter.write(r.toSaveString());
			}
			rideWriter.flush();
			rideWriter.close();
		// Save bike data
		FileWriter bikeWriter = new FileWriter("data-files/bike-data.csv");
		bikeWriter.write("ID,Last Station Id,User Id,Checked Out\n");
			for (Bike b  : this.bikes.values()) {
				bikeWriter.write(b.toSaveString());
			}
			bikeWriter.flush();
			bikeWriter.close();
		// Save maintenance requests data
		FileWriter reqWriter = new FileWriter("data-files/mainreq-data.csv");
		reqWriter.write("ID,User Id,Station Id,Message\n");
			for (MainReq req : this.mainReqs.values()) {
				reqWriter.write(req.toSaveString());
			}
			reqWriter.flush();
			reqWriter.close();
		return "System data successfully saved.";
	}

	
	/**
	 * Function to print out list of stations ordered by id and formatted to console.
	 * 
	 */
	public String viewStationList() {
		String stationList = "ID	Bikes	AvDocs	Capacity	Kiosk	Name - Address\n";
		Iterator<Integer> keyIterator = stations.keySet().iterator();
		while(keyIterator.hasNext()){
			Integer id = (Integer) keyIterator.next();
			stationList = stationList + this.stations.get(id).toViewString();
		}
		return stationList;
	}

	/**
	 * 
	 * Function that reads in a ride data file that contains all the rides for one
	 * day of service and outputs stats for the day
	 * 
	 * TODO: Could simulate bike movement by pushing events to stack to further validate
	 * and check stations for Available Docks, pedelecs, etc
	 * TODO: Implement as a viewStats() method in A5
	 * 
	 */
//	public void resolveRideData() {
//
//		boolean cont = true;
//		int totalTime = 0;
//		int totalRides = 0;
//
//		while (cont) {
//			// prompt for address
//			String path = "";
//			do {
//				System.out.println(
//						"What is the path of the ride data file (Example: data-files/sample-ride-data-0820.csv)? ");
//				while (!sc.hasNext()) {
//					System.out.println("Please try again, input must be a valid file path. ");
//					sc.nextLine();
//				}
//				path = sc.nextLine();
//			} while (path.length() < 4);
//			try {
//				BufferedReader br = new BufferedReader(new FileReader(path));
//				String line = br.readLine();
//				while ((line = br.readLine()) != null) {
//					String[] values = line.split(",");
//					String startTime = values[3];
//					String endTime = values[4];
//					Integer from = Integer.parseInt(values[1]);
//					Integer to = Integer.parseInt(values[2]);
//					if (Integer.parseInt(startTime.split(" ")[1].split(":")[0]) >= 24
//							|| Integer.parseInt(endTime.split(" ")[1].split(":")[0]) >= 24
//							|| stationData.containsKey(to) == false
//							|| stationData.containsKey(from) == false){ continue; }
//					Date tempStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
//					Date tempEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);
//
//					if (tempEnd.before(tempStart)){ continue; }
//					
//					totalTime += ((int) (((tempEnd.getTime() - tempStart.getTime())) / (60 * 1000)));
//					totalRides ++;
//						
//					
//				}
//				cont = false;
//				br.close();
//			} catch (Exception e) {
//				System.err.format("Exception occurred trying to read station data file.");
//				e.printStackTrace();
//			}
//		}
//		int avg = totalTime / totalRides;
//		System.out.println("This ride list contains: " + Integer.toString(totalRides)
//				+ " rides with average ride time of " + Integer.toString(avg) + " minutes.");
//	}

	/**
	 * Adds station to program's Hashmap stations
	 * Station ID is automatically assigned
	 * 
	 */
	public String addStation(Integer id,Integer capacity,boolean kiosk,String address,String name) {
		List<Integer> bikeIds = new ArrayList<>();
		Station s = new Station(id, capacity, capacity, kiosk, address, name, bikeIds);
		this.stations.put(id, s);
		return "Station successfully added to the system.";
	}

	/**
	 * Function to move bike/pedelecs between stations, so that each station is
	 * equalized (based on the percentages of bike/pedelecs per dock capacity). 
	 * 
	 * Removes bikes from stations based on an average percentage
	 * Then gradually reassigns by iterating through each station adding 1 if it is below the percentage
	 * and continuing on while there are still extras to reassign
	 * 
	 * @return String to confirm equalization
	 */
	public String equalizeStations() {

		// find the total number of bikes, pedelecs, and total capacity
		int totalBikes = 0;
		int totalCap = 0;

		for (Station s : this.stations.values()) {
			totalBikes += s.getNumBikes();
			totalCap += s.getCapacity();
		}
		// find the average percentage of bikes to capacity
		double percentBikes = (double) totalBikes / (double) totalCap;
		ArrayList<Integer> spareBikes = new ArrayList<Integer>();
		
		//remove all the extra bikes from stations that are greater than 15% away from average percentage
		for (Station s : this.stations.values()) {
			if((double)s.getNumBikes()/(double)s.getCapacity() > percentBikes + .15){
				spareBikes = removeExtraBikes(s, percentBikes, spareBikes);
			} 
		}
		//while there are still bikes left to add, add them to stations that are under
		//average percentage by more than 15%
		while(spareBikes.size() > 0) {
			for (Station s : this.stations.values()) {
				if((double)s.getNumBikes()/(double)s.getCapacity() < percentBikes - .15) {
					Integer bikeToAdd = spareBikes.remove(0);
					s.addBike(bikeToAdd);
					bikes.get(bikeToAdd).setLastStationId(s.getId());
				}
			}
		}
			
		return "The number of bikes and pedelecs at all stations have been equalized.";
	}
	
	/**
	 * Helper method for equalizeStations()
	 * Removes bikes from the inputted station and adds to a spareBikes list
	 * @param Station s
	 * @param double percentBikes
	 * @param ArrayList<Integer> spareBikes
	 * @returns ArrayList<Integer> spareBikes
	 */
	public ArrayList<Integer> removeExtraBikes(Station s, double percentBikes, ArrayList<Integer> spareBikes){
		while ((double)s.getNumBikes()/(double)s.getCapacity() > percentBikes + .15) {
			Integer bikeToMove = s.getBikeIds().get(0);
			s.removeBike(bikeToMove);
			spareBikes.add(bikeToMove);
		}
		return spareBikes;
	}
	
	/**
	 * Check out bike method for user
	 * TODO implement payment system
	 * @param username
	 * @param stationId 
	 * @return String verifying checkout and payment
	 */
	public String checkOutBike(String username, Integer stationId) {
		// Set this.lastRideId to current ride id
		this.lastRideId = this.lastRideId + 1;
		// Get new ride id
		int rideId = this.lastRideId;
		// Check membership status and charge accordingly
		User currentUser = users.get(username);
		// Right now, we only one type of membership (0)
		int cost = 0;
		if (currentUser.getType() == 0) {
			// TODO: @ali implement payment system here
			// Add cost to user bill?
			// Validate
			// Let's say a ride costs 2 dollars.
			cost = 2;
		}
		// Add ride to user history and set as current ride
		currentUser.addUserRide(rideId);
		currentUser.setCurrentRide(rideId);
		// Get a bike from the station
		Station s = stations.get(stationId);
		Integer bikeId = s.getBikeIds().get(0);
		// Create new Ride and add to current rides list
		Ride ride = new Ride(rideId, username, stationId, bikeId);
		currRides.add(rideId);
		// Add ride to rides hashmap
		rides.put(rideId, ride);
		// Update the bike info
		Bike b = bikes.get(bikeId);
		b.setCheckedOut(true);
		b.setUserId(username);
		
		return "Bike " + bikeId + " successfully checked out. " + "Ride ID: " + rideId +". Your account has been charged $" + cost;
	};
	
	/**
	 * Check in bike method for user
	 * @param username
	 * @param stationId 
	 * @return String "success"
	 */
	public String checkInBike(String username, Integer stationId) {
		// Get user object
		User currentUser = users.get(username);
		// Get ride object
		Ride r = rides.get(currentUser.getCurrentRide());
		// Get bike object
		Bike b = bikes.get(r.getBikeId());
		// Update bike info
		b.setLastStationId(stationId);
		b.setCheckedOut(false);
		b.setUserId("");
		// Add bike to new station
		Station s = stations.get(stationId);
		s.addBike(r.getBikeId());
		// End the ride (update ride info)
		r.end(stationId);

		return "Successfully checked in. Ride completed.";
	}
	
	/**
	 * View user history
	 * TODO: Print ride stats for a more comprehensive user history
	 * @param username
	 * @return String rideHistory
	 */
	public String viewHistory(String username) {
		User currentUser = users.get(username);
		String rideList = "";
		
		for (int r : currentUser.getRides()) {
			rideList = rideList + ("Ride " + r + "\n");
		}
		return "Ride History: \n" + rideList;
	}
	
	/**
	 * view account information for user
	 * @param username
	 * @return String userInfo
	 */
	public String viewAccount(String username) {
		User currentUser = users.get(username);
		
		return ("Account Information: \n "
				+ "Username	Password	Membership	Current Ride	Credit Card	Total Bill	Rides\n" 
				+ currentUser.toViewString());
	}
	
	/**
	 * Create a new maintenance request 
	 * @param username
	 * @param stationId
	 * @param issueMessage
	 * @return String "success"
	 */
	public String reportIssue(String username, Integer stationId, String issueMessage) {
		// Increment lastMainReqId var
		this.lastMainReqId = this.lastMainReqId + 1;
		// Get new id
		int id = this.lastMainReqId;
		// Create new MainReq object
		MainReq req = new MainReq(id, username, stationId, issueMessage);
		mainReqs.put(id, req);
		return "Issue reported.";
	}
	
	/**
	 * Print a system overview of all vehicles and stations
	 * Company view
	 * 
	 * TODO: Finish commenting this method for clarity @ali
	 * @return String systemStats
	 */
	public String viewSystemOverview() {
		// Begin return string
		String systemStats = "System Stats: " + "\n" + "\n" + "Stations:" + "\n";
		systemStats.concat("ID	Bikes	AvDocs	MainReq	Cap	Kiosk	Name - Address");
		Iterator<Entry<Integer, Station>> stationsIterator = stations.entrySet().iterator();
		// For each station in the system
		while(stationsIterator.hasNext()){
			Map.Entry<Integer, Station> stationElement = (Map.Entry<Integer, Station>)stationsIterator.next();
			Station station = stationElement.getValue();
			// Add station info to the return string
			systemStats.concat("\n" + station.toViewString());
			// For each bike at the station
			List<Integer> stationBikes = station.getBikeIds();
			systemStats.concat("\n" + "Station Bikes: ");
			// Add bike id to the return string
			for (Integer id : stationBikes) {
				systemStats.concat(Integer.toString(id) + " ");
			}
		}
		// Add checked out bike info to the return string
		systemStats.concat("Bikes currently checked out: \n");
		systemStats.concat("Bike \t User \n");
		// For each bike in the list of bikes
		Iterator<Entry<Integer, Bike>> bikeIterator = bikes.entrySet().iterator();
		while (bikeIterator.hasNext()) {
			Map.Entry<Integer, Bike> bikeElement = (Map.Entry<Integer, Bike>)bikeIterator.next();
			if (bikeElement.getValue().checkedOut == true) {
				systemStats.concat(Integer.toString(bikeElement.getValue().getId()) + "\t" + bikeElement.getValue().userId + "\n");
			}

		}
		return systemStats;
	}
	
	/**
	 * TODO: What should this do? Ask our stakeholder
	 * @return String stats
	 */
	public String viewStats() {
		Integer numUsers = 0;
		Integer numRides = 0;
		String stats = "";
		Iterator<Entry<String, User>> userIterator = users.entrySet().iterator();
		while(userIterator.hasNext()) {
			numUsers += 1;
			Map.Entry<String, User> userElement = (Map.Entry<String, User>)userIterator.next();
			numRides += userElement.getValue().getRides().size();
		}
		stats = "Total number of users for the system is "+numUsers + " with a "
				+ "total number of " + numRides + " rides";
				
		return stats;
	}
	
	/**
	 * Returns the list of current maintenance requests to the controller
	 * 
	 * @return String issues
	 */
	public String viewIssues() {
		String currIssues = "";
		for (MainReq issue : mainReqs.values()) {
			currIssues = currIssues + issue.toViewString() + "\n";
		}
		return "Current Issues: \n" + currIssues;
	}
	
	/**
	 * Iterates through hashmap of rides
	 * picks out current rides and concatenates them to string
	 * TODO: do we want more info on the rides? where they started, who has them?
	 * @return string of current rides to controller
	 */
	public String viewCurrentRides() {
		String currentRides = "Current Rides:";
		// For all rides in the Hashmap
		Iterator<Entry<Integer, Ride>> ridesIterator = rides.entrySet().iterator();
		while(ridesIterator.hasNext()){
			Map.Entry<Integer, Ride> mapElement = (Map.Entry<Integer, Ride>)ridesIterator.next();
			if (mapElement.getValue().isCurrentRide());{
				currentRides = "/n" + Integer.toString(mapElement.getValue().getId());
			}
		}
		return currentRides;
	}
	
	/**
	 * Add a number of bikes to a station
	 * 
	 * TODO: Add in validate for number of bikes being added to station
	 * @return String "success"
	 */
	public String addBikes(int stationId, int numBikes) {
		
		// check if this station can take this many bikes
		// return string to controller "you can't do that"
		
		Station s = stations.get(stationId);
		
		for (int i = 0; i < numBikes; i++) {
			// Increment last bike id
			this.lastBikeId = this.lastBikeId + 1; 
			// Set new bike id
			int id = this.lastBikeId;
			// Create new bike
			Bike bike = new Bike(id, stationId, "", false);
			// Add bike to the bikes Hashmap
			bikes.put(id, bike);
			// add bike id to station info
			s.addBike(id);
		}
		
		return "Bikes added.";
	}
	
	/**
	 * TODO: Do we want this method?
	 * @return ??? null for now
	 */
	public String moveBikes(Integer stationFrom, Integer stationTo, Integer numBikes) {
		return null;
	}
	
	/**
	 * TODO: @ali check if this works how you want it
	 */
	public String resolveIssues(ArrayList<Integer> issues) {
		for (int i : issues) {
			mainReqs.remove(i);
		}
		return "Issues resolved";
	}
	
	/**
	 * Method checks if payment params are valid
	 * and creates a new user object and adds it to user hashmap if so 
	 * TODO: do we want this as a bool? or do we want a string response if it doesnt work to explain why
	 * @return boolean to controller for whether or not user was successfully added to system
	 */
	public boolean createUser(String username, String password, Integer membership, long cardNum, Integer CVV, String expDate) {
		if (paymentSystem.validate(cardNum, CVV, expDate)) {
			User newUser = new User(username,password,membership,cardNum,CVV, expDate);
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
//		ValleyBikeSim sim = new ValleyBikeSim();
	}
}