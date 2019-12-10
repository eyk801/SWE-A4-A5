import java.awt.Point;
import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author      Ali Eshghi, Charlotte Gephart, Emily Kim, Ester Zhao
 * @version     1.0
 */
public class ValleyBikeSim {
	/**
	 * String that denotes the path for the data files
	 * ValleyBikeSim will be accessing.
	 * Make sure to set proper path before running!!
	 * For dev: "data-files/"
	 * For testing: "test-data-files/"
	 */
	private static String path = "data-files/" /*"test-data-files/"*/;
	private static String savePath = "data-files/" /*"test-data-files-junk/"*/;
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
	private int lastRideId = 0;
	/** Value of last bike id */
	private int lastBikeId = 0;
	/** Value of last station id */
	private int lastStationId = 0;
	/** Value of last maintenance request id */
	private int lastMainReqId = 0;
	/** Instance of the payment system */
	private PaymentSys paymentSystem = new PaymentSys();
	/** Boolean var that states whether a window is open */
	private boolean windowOpen = false;
	/** Object LOCK */
	private Object LOCK = new Object();
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
	 * Reads and parses station data from stored csv file to populate HashMap
	 * </p>
	 * @return stations HashMap global variable
	 */
	public HashMap<Integer, Station> readStationData() {

		HashMap<Integer, Station> stations = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + "station-data.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				int id = Integer.parseInt(values[0]);
				String name = values[1];
				int avDocks = Integer.parseInt(values[2]);
				int cap = Integer.parseInt(values[3]);
				boolean kiosk = Boolean.parseBoolean(values[4]);
				String address = values[5];
				int coordX = Integer.parseInt(values[6]);
				int coordY = Integer.parseInt(values[7]);
				List<Integer> bikeIds = new ArrayList<>();
				// Loop to end of csv line for all bikeIds
				for (int i=8; i < values.length;i++) {
					// Add bike ids to ArrayList
					bikeIds.add(Integer.parseInt(values[i]));
				}
				// Create station object using parsed data
				Station station = new Station(id, avDocks, cap, kiosk, address, name, bikeIds);
				// Set map coordinates
				Point p = new Point(coordX, coordY);
				station.setPoint(p);
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
	 * Reads and parses user data from stored csv file to populate HashMap
	 * </p>
	 * @return users HashMap global variable
	 */
	public HashMap<String, User> readUserData() {

		HashMap<String, User> users = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + "user-data.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				String username = values[0];
				List<Integer> rideHistory = new ArrayList<>();
				User user = new User(username,values[1],Integer.parseInt(values[2]),
									Long.parseLong(values[3]),Integer.parseInt(values[4]),
									values[5]);
				// Check if user is currently on a ride to set currentRideId
				if (Integer.parseInt(values[6]) != 0) {
					user.setCurrentRideId(Integer.parseInt(values[6]));
				}
				
				//TODO: check the numRides values[7], loop for that amount 
				int numRides = Integer.parseInt(values[7]);
				for (int i=8; i < 8+numRides;i++) {
					rideHistory.add(Integer.parseInt(values[i]));
				}
				// Loop to end of line for all ride history
//				for (int i=7; i < values.length;i++) {
//					// Check if valid ride id
//					if (Integer.parseInt(values[i]) != 0) {
//						rideHistory.add(Integer.parseInt(values[i]));
//					}
//				}
				user.setRides(rideHistory);
				//TODO: read in user bill history
				List<String> billHistory = new ArrayList<>();
				for (int i = numRides+8; i < values.length; i++) {
					// If billHist == 0, don't do anything
					if (values[i].equalsIgnoreCase("0")) {
						break;
					}
					billHistory.add(values[i]);
				}
				user.setBillHistory(billHistory);
				
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
	 * Reads and parses bike data from stored csv file to populate HashMap
	 * </p>
	 * @return bikes HashMap global variable
	 */
	public HashMap<Integer, Bike> readBikeData() {

		HashMap<Integer, Bike> bikes = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + "bike-data.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				int id = Integer.parseInt(values[0]);
				Bike bike = new Bike(id, Integer.parseInt(values[1]),
									values[2],Boolean.parseBoolean(values[3]));
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
	 * Reads and parses ride data from stored csv file to populate HashMap
	 * </p>
	 * @return rides HashMap global variable
	 */
	public HashMap<Integer, Ride> readRideData() {

		HashMap<Integer, Ride> rides = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + "ride-data.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
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
	 * Reads and parses maintenance request data from stored csv file to populate HashMap
	 * </p>
	 * @return mainReqs HashMap global variable
	 */
	public HashMap<Integer, MainReq> readMainReqData() {

		HashMap<Integer, MainReq> mainReqs = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + "mainreq-data.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				int id = Integer.parseInt(values[0]);
				MainReq req = new MainReq(id, values[1], Integer.parseInt(values[2]), values[3]);
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
	 * Saves current station data into csv file.
	 * </p>
	 * @throws IOException
	 */
	private void saveStationData() throws IOException {
		FileWriter stationWriter = new FileWriter(savePath + "station-data.csv");
		stationWriter.write("ID,Name,Available Docks,Capacity,Kiosk,Address,Bike Ids\n");
			for (Station s : this.stations.values()) {
				stationWriter.write(s.toSaveString());
			}
			stationWriter.flush();
			stationWriter.close();
	}
	
	/**
	 * Saves current user data into csv file.
	 * </p>
	 * @throws IOException
	 */
	private void saveUserData() throws IOException {
		FileWriter userWriter = new FileWriter(savePath + "user-data.csv");
		userWriter.write("Username,Password,Membership Type,Credit Card Num,CVV,Expiration Date,Current Ride,Number of Rides,Ride History,Bill History\n");
			for (User u : this.users.values()) {
				userWriter.write(u.toSaveString());
			}
			userWriter.flush();
			userWriter.close();
	}
	
	/**
	 * Saves current ride data into csv file.
	 * </p>
	 * @throws IOException
	 */
	private void saveRideData() throws IOException {
		FileWriter rideWriter = new FileWriter(savePath + "ride-data.csv");
		rideWriter.write("ID,Username,Bike Id,Start Station Id,End Station Id,Start Time,End Time,Current Ride\n");
			for (Ride r : this.rides.values()) {
				rideWriter.write(r.toSaveString());
			}
			rideWriter.flush();
			rideWriter.close();
	}
	
	/**
	 * Saves current bike data into csv file.
	 * </p>
	 * @throws IOException
	 */
	private void saveBikeData() throws IOException {
		FileWriter bikeWriter = new FileWriter(savePath + "bike-data.csv");
		bikeWriter.write("ID,Last Station Id,User Id,Checked Out\n");
			for (Bike b  : this.bikes.values()) {
				bikeWriter.write(b.toSaveString());
			}
			bikeWriter.flush();
			bikeWriter.close();
	}
	
	/**
	 * Saves current maintenance request data into csv file.
	 * </p>
	 * @throws IOException
	 */
	private void saveMainReqData() throws IOException {
		FileWriter reqWriter = new FileWriter(savePath + "mainreq-data.csv");
		reqWriter.write("ID,User Id,Station Id,Message\n");
			for (MainReq req : this.mainReqs.values()) {
				reqWriter.write(req.toSaveString());
			}
			reqWriter.flush();
			reqWriter.close();
	}
	
	/**
	 * Saves all current system data into csv files.
	 * </p>
	 * @throws IOException
	 */
	public String saveData() throws IOException {
		// Save station data
		saveStationData();
		// Save user data
		saveUserData();
		// Save ride data
		saveRideData();
		// Save bike data
		saveBikeData();
		// Save maintenance requests data
		saveMainReqData();
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
			int id = (int) keyIterator.next();
			stationList = stationList + this.stations.get(id).toViewString();
		}
		return stationList;
	}

	/**
	 * Add a station to the system.
	 * </p>
	 * Locks until map app is closed
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
		// Add the station to the map
		MapApp map = new MapApp(false);
		this.windowOpen = true;
		// Lock program until user clicks to place station on map
		// Wait on windowOpen boolean
		synchronized (LOCK) {
		    while (this.windowOpen) {
		        try { LOCK.wait(); }
		        catch (InterruptedException e) {
		            // treat interrupt as exit request
		            break;
		        }
		    }
		}
		// Set the station coordinates according to new point
		s.setPoint(map.getPoint());
		this.stations.put(id, s);
		// Save station data
		try {
			saveStationData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Station successfully added to the system.";
	}
	
	/**
	 * Remove a station from the system.
	 * </p>
	 * @return String to show function success.
	 */
	public String removeStation(int id) {
		this.stations.remove(id);
		// Save station data
		try {
			saveStationData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Station " + id + " successfully removed from the system.";
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
	 */
	public String equalizeStations() {
		//Find the total number of bikes and total capacity
		// Use total bikes at each station instead of bikes.size() to account for bikes taken out by users
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
			while (stationPercentage > percentBikes) {
				int bikeToMove = s.getBikeIds().get(0);
				s.removeBike(bikeToMove);
				spareBikes.add(bikeToMove);
				stationPercentage = (int)(((float) s.getNumBikes() / s.getCapacity() * 100));
			}
		}
		//while there are still bikes left to add, add them to stations that are under
		//average percentage
		while(spareBikes.size() > 0) {
			for(Station s : this.stations.values()) {
				int stationPercentage = (int)(((float) s.getNumBikes() / s.getCapacity() * 100));
				if(spareBikes.size() > 0 && stationPercentage < percentBikes) {
					Integer bikeToAdd = spareBikes.remove(0);
					s.addBike(bikeToAdd);					
					bikes.get(bikeToAdd).setLastStationId(s.getId());
					}
			}
		}
		// Save station and bike data
		try {
			saveStationData();
			saveBikeData();
		} catch (IOException e) {
			e.printStackTrace();
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
			return "No available bikes at this station. Please visit another station.";
		}
		
		// Set this.lastRideId to current ride id
		this.lastRideId = this.lastRideId + 1;
		// Get new ride id
		int rideId = this.lastRideId;
		// Check membership status and charge accordingly
		// TODO: Right now, we have 3 types of membership
		//With tiers (0,1,2) that each pay (2 per ride,15 per month,80 per year) respectively
		
		// Charge user account for ride
		String charge = "";
		int membership = currentUser.getType();
		// If user was charged
		if (currentUser.chargeAccount()) {
			switch (membership) {
			case 0: 
				charge = "Your account has been charged $2.";
				break;
			case 1:
				charge = "Your account has been charged $15 for a 30-day pass.";
				break;
			case 2:
				charge = "Your account has been charged $80 for a 365-day pass.";
				break;
			default:
				break;
			}
		} else { // If user was not charged
			switch (membership) {
			case 0:
				break;
			case 1:
				charge = "You are using a 30-day pass.";
				break;
			case 2:
				charge = "You are using a 365-day pass.";
				break;
			default:
				break;
			}
		}
		// Set as current ride for user
		currentUser.setCurrentRideId(rideId);
		// Get a bike from the station
		Station s = stations.get(stationId);
		int bikeId = s.getBikeIds().get(0);
		// Create new Ride and add to current rides list
		Ride ride = new Ride(rideId, username, bikeId, stationId);
		currRides.add(rideId);
		rides.put(rideId, ride);
		// Update the bike info
		Bike b = bikes.get(bikeId);
		b.setCheckedOut(true);
		b.setUserId(username);
		// Update the station info
		s.removeBike(bikeId);
		// Save all data
		try {
			saveStationData();
			saveBikeData();
			saveUserData();
			saveRideData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Bike " + bikeId + " successfully checked out. " + "Ride ID: " + rideId +".\n" + charge;
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
	public String checkInBike(String username, int stationId) {
		User currentUser = users.get(username);
		if (currentUser.onRide() == false) {
			return "User does not currently have a bike to check in";
		}
		
		if(stations.get(stationId).getAvDocks() == 0) {
			return "No available docks for this station.\n "
					+ "Please visit another station.";
		}
		Ride r = rides.get(currentUser.getCurrentRideId());
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
		// Remove ride from current rides
		currRides.remove(r);
		// Save data
		try {
			saveStationData();
			saveUserData();
			saveBikeData();
			saveRideData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Successfully checked in. Ride completed.";
	}
	
	/**
	 * View user ride history.
	 * </p>
	 * @param username		user to view history
	 * @return rideHistory	String of ride ids
	 */
	public String viewHistory(String username) {
		User currentUser = users.get(username);
		int membership = currentUser.getType();
		String rideList = "Ride Id \t Start Time \t\t End Time \n";
		String billList = "Account Charged \t Amount Billed\n";
		int charge = 0;
		int totalCharge = 0;
		// Loop through all rides in User
		for (int r : currentUser.getRides()) {
			Ride ride = rides.get(r);
			rideList = rideList + ("Ride " + r + "\t\t" + ride.getStartTime() + "\t" + ride.getEndTime() + "\n");
		}
		// Set charge for different types of memberships
		switch (membership) {
		case 0:
			charge = 2;
			break;
		case 1:
			charge = 15;
			break;
		case 2:
			charge = 80;
			break;
		}
		// Loop through all times user was charged
		for (String date : currentUser.getBills()) {
			billList = billList + date + "\t\t $" + charge +"\n";
			totalCharge = totalCharge + charge;
		}
		return "User History: \n\n" + rideList + "\n" + billList + "\n\nTotal Costs Incurred: $" + totalCharge +"\n";
	}
	
	/**
	 * View user account information.
	 * @param username		user to view account info
	 * @return userInfo		String of user account info
	 */
	public String viewAccount(String username) {
		User currentUser = users.get(username);
		
		return ("Account Information: \n "
				+ "Username	Password	Membership	Current Ride	Credit Card\t	Rides\n" 
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
	public String reportIssue(String username, int stationId, String issueMessage) {
		// Increment lastMainReqId var
		this.lastMainReqId = this.lastMainReqId + 1;
		// Get new id
		int id = this.lastMainReqId;
		// Create new MainReq object
		MainReq req = new MainReq(id, username, stationId, issueMessage);
		mainReqs.put(id, req);
		// Save mainreq data
		try {
			saveMainReqData();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			for (int id : stationBikes) {
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
	 * Returns system stats of weekly and all time total rides/users.
	 * <p>
	 * @return stats	String of desired system statistics
	 * @throws ParseException 
	 */
	public String viewStats() throws ParseException {
		int numUsers = users.size();
		int numRides = rides.size();
		int numWeeklyRides = 0;
		HashMap<String, Integer> userRideStats = new HashMap<>();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		Date date = new Date();
		//sets date to a week ago
		date.setTime(currentDate.getTime()-604800000);
		
		for(Ride ride : rides.values()) {
			String rideDateString = ride.getStartTime();
			Date rideDate = formatter.parse(rideDateString);
			if(!rideDate.before(date)) {
				numWeeklyRides = numWeeklyRides + 1;
				String userId = ride.getUserId();
				if (!userRideStats.containsKey(userId)) {
					userRideStats.put(userId, 1);
				} else {
					int rides = userRideStats.get(userId);
					userRideStats.replace(userId, rides+1);
				}
			};
		}
		
		String bestUser = "";
		int maxRides = 0;
		String maxUser = "";
		
		Iterator<Entry<String, Integer>> userIterator = userRideStats.entrySet().iterator();
		while(userIterator.hasNext()) {
			Map.Entry<String, Integer> userElement = (Map.Entry<String, Integer>)userIterator.next();
			if (userElement.getValue() > maxRides) {
				maxRides = userElement.getValue();
				maxUser = userElement.getKey();
			}
		}
		
		if(maxRides != 0 && !maxUser.equalsIgnoreCase("")) {
			bestUser = "\nUser with most rides this week is "
					+ maxUser + " with " + maxRides + " rides!";
		}
	
		
		
		String stats = "In the past week there were a total of "+ numWeeklyRides + " ride(s) with "
				+ userRideStats.size() + " user(s)" + "\nThere are a total of " + numRides + " rides and "
						+ numUsers + " users in the system." + bestUser;
				
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
			bikes.put(id, bike);
			s.addBike(id);
		}
		// Save station and bike data
		try {
			saveStationData();
			saveBikeData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Bikes added.";
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
		String resolved = "";
		String invalid = "";
		for (int i : issues) {
			if (mainReqs.remove(i) == null) {
				invalid += i + " ";
			}else{
				resolved += i + " ";
			};
		}
		if (!resolved.equalsIgnoreCase("")) {
			resolved = "Issue(s) " + resolved + "resolved.";
		}
		if (!invalid.equalsIgnoreCase("")) {
			invalid = "Issue(s) " + invalid + "invalid. Could not resolve.";
		}
		// Save mainreq data
		try {
			saveMainReqData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resolved + "\n" + invalid;
	}
	
	/**
	 * Creates a new user.
	 * </p>
	 * This function validates inputted payment info, and if valid, 
	 * creates a new User object and adds it to the global HashMap.
	 * @return report	boolean declaring whether user was successfully made or not.
	 */
	public boolean createUser(String username, String password, int membership, long cardNum, Integer CVV, String expDate) {
		if (paymentSystem.validate(cardNum, CVV, expDate)) {
			User newUser = new User(username,password,membership,cardNum,CVV, expDate);
			//A user will be charged upon the beginning of their first ride
			users.put(username, newUser);
			// Save user data
			try {
				saveUserData();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Checks whether a station exists or not.
	 * </p>
	 * @param stationId
	 * @return exists		boolean stating whether a station exists or not
	 */
	public boolean stationExists(int stationId) {
		boolean exists = false;
		if (stations.containsKey(stationId)) {
			exists = true;
		}
		return exists;
	}
	
	public Object getLock() {
		return this.LOCK;
	}
	
	/**
	 * Sets the windowOpen boolean within the MapApp class
	 * @param b	boolean value
	 */
	public void setWindowOpen(boolean b) {
		this.windowOpen = b;
	}

	/**
	 * @return the stations
	 */
	public Map<Integer, Station> getStations() {
		return stations;
	}

	/**
	 * @param stations the stations to set
	 */
	public void setStations(Map<Integer, Station> stations) {
		this.stations = stations;
	}

	/**
	 * @return the users
	 */
	public Map<String, User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(Map<String, User> users) {
		this.users = users;
	}

	/**
	 * @return the bikes
	 */
	public Map<Integer, Bike> getBikes() {
		return bikes;
	}

	/**
	 * @param bikes the bikes to set
	 */
	public void setBikes(Map<Integer, Bike> bikes) {
		this.bikes = bikes;
	}

	/**
	 * @return the rides
	 */
	public Map<Integer, Ride> getRides() {
		return rides;
	}

	/**
	 * @param rides the rides to set
	 */
	public void setRides(Map<Integer, Ride> rides) {
		this.rides = rides;
	}

	/**
	 * @return the mainReqs
	 */
	public Map<Integer, MainReq> getMainReqs() {
		return mainReqs;
	}

	/**
	 * @param mainReqs the mainReqs to set
	 */
	public void setMainReqs(Map<Integer, MainReq> mainReqs) {
		this.mainReqs = mainReqs;
	}

	/**
	 * @return the currRides
	 */
	public List<Integer> getCurrRides() {
		return currRides;
	}

	/**
	 * @param currRides the currRides to set
	 */
	public void setCurrRides(List<Integer> currRides) {
		this.currRides = currRides;
	}

	/**
	 * @return the lastRideId
	 */
	public int getLastRideId() {
		return lastRideId;
	}

	/**
	 * @param lastRideId the lastRideId to set
	 */
	public void setLastRideId(int lastRideId) {
		this.lastRideId = lastRideId;
	}

	/**
	 * @return the lastBikeId
	 */
	public int getLastBikeId() {
		return lastBikeId;
	}

	/**
	 * @param lastBikeId the lastBikeId to set
	 */
	public void setLastBikeId(int lastBikeId) {
		this.lastBikeId = lastBikeId;
	}

	/**
	 * @return the lastStationId
	 */
	public int getLastStationId() {
		return lastStationId;
	}

	/**
	 * @param lastStationId the lastStationId to set
	 */
	public void setLastStationId(int lastStationId) {
		this.lastStationId = lastStationId;
	}

	/**
	 * @return the lastMainReqId
	 */
	public int getLastMainReqId() {
		return lastMainReqId;
	}

	/**
	 * @param lastMainReqId the lastMainReqId to set
	 */
	public void setLastMainReqId(int lastMainReqId) {
		this.lastMainReqId = lastMainReqId;
	}	
}