import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author      Ali Eshghi, Charlotte Gephart, Emily Kim, Ester Zhao
 * @version     1.0
 */
public class Ride {
	/** Ride id */
	private int id = 0;
	/** Id of user who created ride */
	private String userId = new String();
	/** Id of bike used during ride */
	private int bikeId = 0;
	/** Id of station where ride began */
	private int startStationId = 0;
	/** Id of station where ride ended */
	private int endStationId = 0;
	/** Timestamp of when ride began */
	private Timestamp startTime = new Timestamp(System.currentTimeMillis());
	/** Timestamp of when ride ended */
	private Timestamp endTime = new Timestamp(System.currentTimeMillis());
	/** Boolean for whether ride is currently happening. Default = true. */
	private boolean currentRide = true;

	
	/**
	 * Ride class constructor.
	 * </p>
	 * @param id
	 * @param userId
	 * @param bikeId
	 * @param startStationid
	 */
	public Ride(int id, String userId, int bikeId, int startStationId) {
		this.id = id;
		this.userId = userId;
		this.bikeId = bikeId;
		this.startStationId = startStationId;
	}
	
	/**
	 * Getter for ride id
	 * </p>
	 * @return id
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Getter for user id
	 * </p>
	 * @return userId
	 */
	public String getUserId() {
		return this.userId;
	}
	
	/**
	 * Setter for user id
	 * </p>
	 * @param userID	new user id
	 */
	public void setUserId(String userID) {
		this.userId = userID;
	}
	
	/**
	 * Getter for bike id
	 * </p>
	 * @return bikeId
	 */
	public int getBikeId() {
		return this.bikeId;
	}

	/**
	 * Setter for bike id
	 * </p>
	 * @param bikeID
	 */
	public void setBikeId(int bikeID) {
		this.id = bikeId;
	}
	
	/**
	 * Getter for start station id
	 * </p>
	 * @return startStationId
	 */
	public int getStartStationId() {
		return this.startStationId;
	}
	
	/**
	 * Setter for start station id
	 * </p>
	 * @param station	id of start station
	 */
	public void setStartStationId(int station) {
		this.startStationId = station;
	}
	
	/**
	 * Getter for end station id
	 * </p>
	 * @return endStationid
	 */
	public int getEndStationId() {
		return this.endStationId;
	}
	
	/**
	 * Setter for end station id
	 * </p>
	 * @param station	id of end station
	 */
	public void setEndStation(int station) {
		this.endStationId = station;
	}
	
	/**
	 * Getter for start time
	 * </p>
	 * @return startTime	the start time as a string
	 */
	public String getStartTime() {
		Date date = new Date();
		date.setTime(this.startTime.getTime());
		String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return startTime;
	}
	
	/**
	 * Setter for start time
	 * </p>
	 * @param time		the start time as a string
	 */
	public void setStartTime(String time) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = formatter.parse(time);
			Timestamp ts = new Timestamp(date.getTime()); 
			this.startTime = ts;
		} catch (java.text.ParseException e) {
            e.printStackTrace();
        }
	}

	/**
	 * Getter for end time
	 * </p>
	 * @return endTime	the end time as a string	
	 */
	public String getEndTime() {
		Date date = new Date();
		date.setTime(this.endTime.getTime());
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return endTime;
	}
	
	/**
	 * Setter for end time
	 * </p>
	 * @param time		the end time as a string
	 */
	public void setEndTime(String time) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = formatter.parse(time);
			Timestamp ts = new Timestamp(date.getTime()); 
			this.endTime = ts;
		}  catch (java.text.ParseException e) {
            e.printStackTrace();
        	}
	}
	
    /**
     * Getter for current ride boolean
     * </p>
     * @return currentRide 	boolean response on whether ride is currently happening
     */
    public boolean isCurrentRide() {
    	return this.currentRide;
    }
    
	/**
	 * Setter for current ride boolean
	 * </p>
	 * @param bool		true/false whether the ride is currently happening
	 */
	public void setCurrentRide(boolean bool) {
		this.currentRide = bool;
	}
	
	/**
	 * End a ride
	 * </p>
	 * Update all necessary variables, endStationId, endTime, and set currentRide to false.
	 * </p>
	 * @param stationId		the id of the station where the ride ended
	 */
	public void end(int stationId) {
		this.endStationId = stationId;
		this.endTime = new Timestamp(System.currentTimeMillis());
		this.currentRide = false;
	}
	
    /**
     * toString method for data to write to csv data files
     * </p>
     * @return toString	the string to store in the ride-data.csv
     */
    public String toSaveString() {
    	String start = getStartTime();
    	String end = getEndTime();
        return (this.id + "," + this.userId + "," + this.bikeId + "," + this.startStationId + "," + 
        		this.endStationId + "," + start + "," + end+ "," + this.currentRide + "\n");
    }
    
    /**
     * toString method for ride data to output onto console
     * </p>
     * @return toString	the string to print to the console
     */
    public String toViewString(){
    	return (Integer.toString(this.id) + "\t" 
    					+ this.userId + "\t"
    					+ this.startStationId + "\t"
    					+ this.endStationId + "\t" 
    					+ this.bikeId + "\t"
    					+ this.startTime + "\t"
    					+ this.endTime + "\n");  
    }
}
