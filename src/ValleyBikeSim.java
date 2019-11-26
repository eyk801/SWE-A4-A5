import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author      Ali Eshghi, Charlotte Gephart, Emily Kim, Ester Zhao
 * @version     1.0
 */
public class ValleyBikeSim {
	/** Hashmap of all station objects stationid:Station)*/
	private Map<Integer, Station> stations = new HashMap<>();
	/** Hashmap of all user objects userId:User)*/
	private Map<String, User> users = new HashMap<>();
	/** Hashmap of all bikes objects bikeId:Bike)*/
	private Map<Integer, Bike> bikes = new HashMap<>();
	/** Hashmap of all rides objects rideId:Ride)*/
	private Map<Integer, Ride> rides = new HashMap<>();
	/** Hashmap of all maintenance request objects mainReqId:MainReq)*/
	private Map<Integer, MainReq> mainReqs = new HashMap<>();
	/** List of current ride ids)*/
	private List<Integer> currRides = new ArrayList<>();
	/** Value of last ride id */
	private Integer lastRideId = 0;
	/** Value of last bike id */
	private Integer lastBikeId = 0;
	/** Value of last station id */
	private Integer lastStationId = 0;
	/** Value of last maintenance request id */
	private Integer lastMainReqId = 0;
	/** Instance of the payment system */
	private PaymentSys paymentSystem = new PaymentSys();
	/** Instance of the ValleyBikeSim */
	private static ValleyBikeSim instance = null;
	
	/**
	 * ValleyBikeSim class constructor.
	 * </p>
	 * Calls all methods to read csv data files,
	 * populates class HashMaps and currRides List,
	 * and updates lastId variables.
	 * 
	 */
	private ValleyBikeSim() {
		this.stations = readStationData();
		this.users = readUserData();
		this.bikes = readBikeData();
		this.rides = readRideData();
		this.mainReqs = readMainReqData();
	}
	
	/**
	 * Public getter method for the ValleyBikeSim instance 
	 * </p>
	 * Implementation of the Singleton design pattern
	 * </p>
	 * @return ValleyBikeSim instance
	 */
	public static ValleyBikeSim getInstance() {
	    if (instance == null) {
            instance = new ValleyBikeSim();
        }
	    return instance;
	}

	/**
	 * Reads in station data from stored csv file to populate HashMap
	 * </p>
	 * @return stations HashMap global variable
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
				// Update this.lastStationId var
				if (id > this.lastStationId) {
					this.lastStationId = id;
				}
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
	 * Reads in user data from stored csv file to populate HashMap
	 * </p>
	 * @return users HashMap global variable
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
				// Check if user is currently on a ride and set currentRideId
				if (Integer.parseInt(values[6]) != 0) {
					user.setCurrentRideId(Integer.parseInt(values[6]));
				}
				// Loop to end of line for all ride history
				for (int i=7; i < values.length;i++) {
					// Check if valid ride id
					if (Integer.parseInt(values[i]) != 0) {
						rideHistory.add(Integer.parseInt(values[i]));
					}
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
	 * Reads in bike data from stored csv file to populate HashMap
	 * </p>
	 * @return bikes HashMap global variable
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
	 * Reads in ride data from stored csv file to populate HashMap
	 * </p>
	 * @return rides HashMap global variable
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
				// Set currentRideId
				ride.setCurrentRide(Boolean.parseBoolean(values[7]));
				// If current ride == true, add ride id to currRides list
				if (Boolean.parseBoolean(values[7])) {
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
	 * Reads in maintenance request data from stored csv file to populate HashMap
	 * </p>
	 * @return mainReqs HashMap global variable
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
	 * Saves current system data into csv files.
	 * </p>
	 * @throws IOException
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
		rideWriter.write("ID,Username,Bike Id,Start Station Id,End Station Id,Start Time,End Time,Current Ride\n");
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
	 * Generates a string of station info.
	 * </p>
	 * @return String of stations info
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
	 * Add a station to the system.
	 * </p>
	 * @return String to show function success.
	 */
	public String addStation(int capacity,boolean kiosk,String address,String name) {
		// Set this.lastStationId to current station id
		this.lastStationId = this.lastStationId + 1;
		// Get new ride id
		int id = this.lastStationId;
		// Initialize empty list of bikes ids
		List<Integer> bikeIds = new ArrayList<>();
		// Create new station
		Station s = new Station(id, capacity, capacity, kiosk, address, name, bikeIds);
		this.stations.put(id, s);
		return "Station successfully added to the system.";
	}

	/**
	 * Balances bikes across the system's stations.
	 * </p>
	 * Moves bikes between stations so that each station is
	 * equalized based on a system-wide ideal percentage of bikes to station capacity.
	 * Removes bikes from stations then reassigns them by iterating through each station
	 * and adding bikes if the station bike-capacity percentage is low.
	 * </p>
	 * @return String to confirm equalization
	 * TODO: change implementation
	 */
	public String equalizeStations() {
		// find the total number of bikes and total capacity
		//The reason we use total bikes at stations instead of just bikes.size()
		//Is because we can't move bikes that are currently checked out by users
		int totalBikes = 0;
		int totalCap = 0;
		for (Station s : this.stations.values()) {
			totalBikes += s.getNumBikes();
			totalCap += s.getCapacity();
		}
		// find the average percentage of bikes to capacity
		int percentBikes = (int)(((float) totalBikes / totalCap) *100);
		ArrayList<Integer> spareBikes = new ArrayList<Integer>();
		
		//remove all the extra bikes from stations that are greater than 5% away from average percentage
		for (Station s : this.stations.values()) {
			int stationPercentage = (int)(((float) s.getNumBikes() / s.getCapacity() * 100));
			while (stationPercentage > percentBikes + 5) {
				Integer bikeToMove = s.getBikeIds().get(0);
				s.removeBike(bikeToMove);
				spareBikes.add(bikeToMove);
				stationPercentage = (int)(((float) s.getNumBikes() / s.getCapacity() * 100));
			}
		}
		//while there are still bikes left to add, add them to stations that are under
		//average percentage
		for (Station s : this.stations.values()) {
			int stationPercentage = (int)(((float) s.getNumBikes() / s.getCapacity() * 100));
			while(stationPercentage < percentBikes) {
				if(spareBikes.size() > 0) {
				Integer bikeToAdd = spareBikes.remove(0);
				s.addBike(bikeToAdd);					
				bikes.get(bikeToAdd).setLastStationId(s.getId());
				stationPercentage = (int)(((float) s.getNumBikes() / s.getCapacity() * 100));
				} else {
					break;
				}
			}
		
		}
		return "The number of bikes at all stations have been equalized.";
	}
	
	
	/**
	 * Assigns a bike to the user and creates a new ride.
	 * </p>
	 * Takes in input from the controller to checkout a bike from specified station,
	 * then creates a ride object and updates bike, station, and user data accordingly.
	 * </p>
	 * @param username 		String user id 
	 * @param stationId 	int of which station to remove bike from
	 * @return String 		verifying checkout and payment
	 */
	public String checkOutBike(String username, int stationId) {
		User currentUser = users.get(username);
		if (currentUser.onRide() == true) {
			return "User already on ride. Cannot check out more than one bike at a time";
		}
		
		if (stations.get(stationId).getNumBikes() == 0) {
			return "No bikes at this station. Try another!";
		}
		
		// Set this.lastRideId to current ride id
		this.lastRideId = this.lastRideId + 1;
		// Get new ride id
		int rideId = this.lastRideId;
		// Check membership status and charge accordingly
		// Right now, we have 3 types of membership
		//With tiers (0,1,2) that each pay (2,1,0)
		//respectively
		int cost = 0;
		if (currentUser.getType() == 0) {
			cost = 2;
			currentUser.addToBill(cost);
		}
		if (currentUser.getType() == 1) {
			cost = 1;
			currentUser.addToBill(cost);
		}
		if (currentUser.getType() == 2) {
			cost = 0;
			currentUser.addToBill(cost);
		}
		// Set as current ride for user
		currentUser.setCurrentRideId(rideId);
		// Get a bike from the station
		Station s = stations.get(stationId);
		Integer bikeId = s.getBikeIds().get(0);
		// Create new Ride and add to current rides list
		Ride ride = new Ride(rideId, username, bikeId, stationId);
		currRides.add(rideId);
		// Add ride to rides hashmap
		rides.put(rideId, ride);
		// Update the bike info
		Bike b = bikes.get(bikeId);
		b.setCheckedOut(true);
		b.setUserId(username);
		// Update the station info
		s.removeBike(bikeId);
		
		return "Bike " + bikeId + " successfully checked out. " + "Ride ID: " + rideId +". Your account has been charged $" + cost;
	};
	
	/**
	 * Places a bike back into designated station and ends user's ride.
	 * </p>
	 * Takes in input from the controller to checkin a bike to specified station,
	 * Then updates bike, station, ride, and user information accordingly.
	 * </p>
	 * @param username 		String user id 
	 * @param stationId 	int of which station to place bike in
	 * @return String 		verifying checkin and completion of ride
	 */
	public String checkInBike(String username, Integer stationId) {
		// Get user object
		User currentUser = users.get(username);
		if (currentUser.onRide() == false) {
			return "User does not currently have a bike to check in";
		}
		
		if(stations.get(stationId).getAvDocks() == 0) {
			return "No available docks for this station.\n "
					+ "Please call to set up a virtual station"
					+"or go to another station";
		}
		// Get ride object
		Ride r = rides.get(currentUser.getCurrentRideId());
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
		// Update user info (end ride), add ride to ride history
		currentUser.endRide();

		return "Successfully checked in. Ride completed.";
	}
	
	/**
	 * View user ride history.
	 * </p>
	 * @param username		user to view history
	 * @return rideHistory	String of ride ids
	 * TODO: Print ride stats for a more comprehensive user history (A5)
	 */
	public String viewHistory(String username) {
		User currentUser = users.get(username);
		String rideList = "";
		// Loop through all rides in User
		for (int r : currentUser.getRides()) {
			rideList = rideList + ("Ride " + r + "\n");
		}
		return "Ride History: \n" + rideList;
	}
	
	/**
	 * View user account information.
	 * @param username		user to view account info
	 * @return userInfo		String of user account info
	 */
	public String viewAccount(String username) {
		User currentUser = users.get(username);
		
		return ("Account Information: \n "
				+ "Username	Password	Membership	Current Ride	Credit Card\t Total Bill	Rides\n" 
				+ currentUser.toViewString());
	}
	
	/**
	 * Create a new maintenance request.
	 * </p>
	 * @param username		user creating the mainReq
	 * @param stationId		station at which the mainReq is located
	 * @param issueMessage	message detailing request
	 * @return report		String confirming issue has been created
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
	 * Print a system overview of all vehicles and stations for the company view.
	 * </p>
	 * This employee only access method first iterates through each station, 
	 * parsing station info. Then it goes deeper to show which bikes are at each station.
	 * Additionally, it will show the employee which bikes are currently checked out and by which user.
	 * </p>
	 * @return systemStats	String of full system overview
	 */
	public String viewSystemOverview() {
		// Begin return string
		String systemStats = "System Stats: " + "\n" + "\n" + "Stations:" + "\n";
		systemStats += ("ID	Bikes	AvDocs	Capacity	Kiosk	Name - Address \n");
		Iterator<Entry<Integer, Station>> stationsIterator = stations.entrySet().iterator();
		// For each station in the system
		while(stationsIterator.hasNext()){
			Map.Entry<Integer, Station> stationElement = (Map.Entry<Integer, Station>)stationsIterator.next();
			Station station = stationElement.getValue();
			// Add station info to the return string
			systemStats += ("\n" + station.toViewString());
			// For each bike at the station
			List<Integer> stationBikes = station.getBikeIds();
			systemStats += ("\n" + "Station " + station.getId() + " Bike IDs: ");
			// Add bike id to the return string
			for (Integer id : stationBikes) {
				systemStats += (Integer.toString(id) + " ");
			}
			systemStats += "\n \n";
		}
		// Add checked out bike info to the return string
		systemStats += ("Bikes currently checked out: \n");
		systemStats += ("Bike \t User \n");
		// For each bike in the list of bikes
		Iterator<Entry<Integer, Bike>> bikeIterator = bikes.entrySet().iterator();
		while (bikeIterator.hasNext()) {
			Map.Entry<Integer, Bike> bikeElement = (Map.Entry<Integer, Bike>)bikeIterator.next();
			if (bikeElement.getValue().checkedOut == true) {
				systemStats += (Integer.toString(bikeElement.getValue().getId()) + "\t" + bikeElement.getValue().userId + "\n");
			}

		}
		return systemStats;
	}
	
	/**
	 * Returns system stats.
	 * <p>
	 * @return stats	String of desired system statistics
	 * TODO: Ask our stakeholder what kind of stats wanted here (A5)
	 */
	public String viewStats() {
		int numUsers = users.size();
		int numRides = rides.size();
		
		String stats = "Total number of users for the system is "+numUsers + " with a "
				+ "total number of " + numRides + " rides";
				
		return stats;
	}
	
	/**
	 * Returns the list of current maintenance requests.
	 * </p>
	 * @return issues	String of all current maintenance requests
	 */
	public String viewIssues() {
		String currIssues = "";
		for (MainReq issue : mainReqs.values()) {
			currIssues = currIssues + issue.toViewString() + "\n";
		}
		return "Current Issues: \n" + currIssues;
	}
	
	/**
	 * Returns the list of current rides.
	 * </p>
	 * This function iterates through the list of current rides,
	 * and concatenates each ride and associated data into the return string.
	 * </p>
	 * @return currentRides	String of current ride data
	 * TODO: Ask our stakeholder if more info on current rides is wanted. (A5)
	 */
	public String viewCurrentRides() {
		String currentRides = "Current Rides:\n";
		// Loop through rides in currRides list
		for (int ride : this.currRides) {
			currentRides = currentRides + "User: " + rides.get(ride).getUserId() + ". Ride: " + ride + "\n";
		}
		return currentRides;
	}
	
	/**
	 * Add a specified number of bikes to a station.
	 * </p>
	 * Checks whether the number of bikes can be added to the station,
	 * then creates new Bike objects and adds them to specified station.
	 * </p>
	 * @return report	String confirming success.
	 */
	public String addBikes(int stationId, int numBikes) {
		int avDocks = stations.get(stationId).getAvDocks();
		// check if this station can take this many bikes
		// return error string to controller
		if(avDocks < numBikes) {
			return "Station " + stationId + " only has available docks for " + avDocks + " new bikes\n"
					+ "Please try again.";
		}
		
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
	 * TODO: Consider implementing for A5
	 */
	public String moveBikes(Integer stationFrom, Integer stationTo, Integer numBikes) {
		return null;
	}
	
	/**
	 * Resolve maintenance request issues.
	 * </p>
	 * This employee-only function iterates through the issues requested to resolve,
	 * and checks whether they exist. If they do, the mainReqs are removed from the global HashMap.
	 * </p>
	 * @param issues	ArrayList<Integer> list of mainReqs to resolve
	 * @return report	String confirming success.
	 */
	public String resolveIssues(ArrayList<Integer> issues) {
		String resolved = "Issues ";
		String invalid = "";
		for (int i : issues) {
			if (mainReqs.remove(i) == null) {
				invalid += i + " ";
			}else{
				resolved += i + " ";
			};
		}
		if (resolved.equalsIgnoreCase("Issues ")) {
			resolved = "";
		}if (invalid.equalsIgnoreCase("") == false) {
			resolved += "have been resolved\nIssues " + invalid + " were not found in the system";
		}
		return resolved;
	}
	
	/**
	 * Creates a new user.
	 * </p>
	 * This function validates inputted payment info, and if valid, 
	 * creates a new User object and adds it to the global HashMap.
	 * @return report	boolean declaring whether user was successfully made or not.
	 */
	public boolean createUser(String username, String password, Integer membership, long cardNum, Integer CVV, String expDate) {
		if (paymentSystem.validate(cardNum, CVV, expDate)) {
			User newUser = new User(username,password,membership,cardNum,CVV, expDate);
			int membershipCharge = newUser.getType();
			newUser.addToBill(membershipCharge);
			users.put(username, newUser);
			return true;
		} else {
			return false;
		}
	}
}