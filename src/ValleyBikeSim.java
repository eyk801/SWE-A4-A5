/*
 * hello
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
	private Integer rideID = 0;
	private PaymentSys paymentSystem = new PaymentSys();


	/**
	 * ValleyBikeSim class constructor
	 * 
	 * TODO: Remove and re-organize program so that this is no longer necessary
	 * or so that the object does more - Alicia says object could be potentially useful
	 * can also avoid the issue with static method stuff
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
				int kiosk = Integer.parseInt(values[4]);
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
									Integer.parseInt(values[3]),Integer.parseInt(values[4]),
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
	 * TODO: test - implement exception throw
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
	 * TODO: update for station info format
	 * 
	 */
	public void viewStationList() {
		System.out.println("ID	Bikes	Pedelecs	AvDocs	MainReq	Cap	Kiosk	Name - Address");
		Iterator<Integer> keyIterator = stations.keySet().iterator();
		while(keyIterator.hasNext()){
			Integer id = (Integer) keyIterator.next();
			System.out.println(this.stations.get(id).toViewString());
		}
	}

	/**
	 * 
	 * Function that reads in a ride data file that contains all the rides for one
	 * day of service and outputs stats for the day
	 * 
	 * TODO: Ok for now but could simulate bike movement by pushing events to stack to further validate
	 * and check stations for Available Docks, pedelecs, etc
	 * TODO: update??? move all system input to the controller
	 * 
	 * Employee can access, but not user
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
	 * Adds station to program's treemap of stations
	 * Station ID is automatically assigned
	 * 
	 * 
	 * Company method
	 */
	public void addStation(Integer id,Integer capacity,Integer kiosk,String address,String name) {

		Station s = new Station(id, capacity, capacity, kiosk, address, name, bikeIds);
		this.stations.put(id, s);
		System.out.println("Station successfully added to the system.");
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
	 * TODO: update this for just bikes
	 * 
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
	 * TODO check membership status and charge account accordingly
	 * @param username
	 * @param stationId 
	 * @return
	 */
	public String checkOutBike(String username, Integer stationId) {
		// assign ride ID
		rideID += 1;

		// check membership status and charge accordingly, add ride to user ride list
		User currentUser = users.get(username);
		if (currentUser.getType() == 0) {
			System.out.println("Pay-per-ride. Your card will be charged $2");
		}				
		currentUser.addUserRide(rideID);
		
		Integer bikeID = 0;
		Station s = stations.get(stationId);
		ArrayList bikesList = s.getBikeIds();
		for (Integer bID : bikesList) {
			Bike currBike = bikes.get(bID);
			if (currBike.getUserId().equals("")) {
				bikeID = bID;
			}
		}

		// create new Ride and add to current rides list
		Ride ride = new Ride(rideID, username, stationId, bikeID);
		currRides.add(rideID);

		Bike b = bikes.get(bikeID);
		b.setCheckedOut(true);
		b.setLastStationId(stationId);
		b.setUserId(username);
				
		// add ride to rides hashmap
		rides.put(rideID, ride);

		return "Bike " + bikeID + " successfully checked out by " + username + ", Ride ID " + rideID;
	};
	
	/**
	 * Check in bike method for user
	 * TODO: Implement method
	 * @param username
	 * @param stationId 
	 * @return String "success"
	 */
	public String checkInBike(String username, Integer stationId) {
		
		User currentUser = users.get(username);
		
		Integer currRideID = currentUser.getCurrentRide();

		Ride r = rides.get(currRideID);
		Integer bikeID = r.getBikeId();
		
		Bike b = bikes.get(bikeID);
		b.setLastStationId(stationId);
		b.setUserId("");
		b.setCheckedOut(false);
				
		r.end(stationId);

		return "Successfully checked in.";
	}
	
	/**
	 * View user history
	 * TODO: Implement method
	 * @param username
	 * @return
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
	 * TODO: Implement method
	 * @param username
	 * @return
	 */
	public String viewAccount(String username) {
		User currentUser = users.get(username);
		return "Account Information: \n" + currentUser.toViewString();
	}
	
	//TODO: Implement method
	public String reportIssue(String username, String issueMessage) {
		return "Issue reported";
	}
	
	/*
	 * TODO: Return system stats string
	 * 
	 * 
	 */
	public String viewSystemOverview() {
		String systemStats = "System Stats: " + "\n" + "\n" + "Stations:" + "\n";
		systemStats.concat("ID	Bikes	Pedelecs	AvDocs	MainReq	Cap	Kiosk	Name - Address");
		Iterator<Entry<Integer, Station>> stationsIterator = stations.entrySet().iterator();
		while(stationsIterator.hasNext()){
			Map.Entry<Integer, Station> stationElement = (Map.Entry<Integer, Station>)stationsIterator.next();
			Station station = stationElement.getValue();
			systemStats.concat("\n" + station.toViewString());
			List<Integer> stationBikes = station.getBikeIds();
			systemStats.concat("\n" + "Station Bikes: ");
			for (Integer id : stationBikes) {
				systemStats.concat(Integer.toString(id) + " ");
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
		return systemStats;
	}
	
	//TODO: Implement method
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
		int bikeId = 0;

		for (numBikes : this.bikes.values()){
			numBikes = numBikes.get(); 
		}
		// create new Bike and add to HashMap
		boolean checkedOut;
		Bike bike = new Bike(id, lastStationId, userId, checkedOut);

		Bike b = bikes.get(bikeId);
		b.setCheckedOut(false);
		bikes.put(bikeId, bike);
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
	 * Method checks if payment params are valid
	 * and creates a new user object and adds it to user hashmap if so 
	 * TODO: call payment system method on user
	 * @return boolean to controller for whether or not user was successfully added to system
	 */
	public boolean createUser(String username, String password, Integer membership, Integer cardNum, Integer CVV, String expDate) {
		if(paymentSystem.validate(cardNum, CVV, expDate) == true) {
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
//		System.out.println("Welcome to the ValleyBike Simulator.\n");
//		// calls option menu
//		try {
//			sim.execute();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}