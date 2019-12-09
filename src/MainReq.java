/**
 * @author      Ali Eshghi, Charlotte Gephart, Emily Kim, Ester Zhao
 * @version     1.0
 */
public class MainReq {
	/** Maintenance request id */
	private int id = 0;
	/** Id of station where mainReq is based */
    private int stationId = 0;
    /** Username of user who input the issue */
    private String username = new String();
    /** Maintenance request issue message */
	private String message = new String();
	
	/**
	 * Maintenance request constructor.
	 * </p>
	 * @param id
	 * @param username
	 * @param stationId
	 * @param message
	 */
	public MainReq(int id, String username, int stationId, String message) {
		this.id = id;
		this.username = username;
		this.stationId = stationId;
		this.message = message;
	}
	
    /**
     * Getter for maintenance request id
     * </p>
     * @return id
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Setter for mainReq id
     * </p>
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Getter for station Id
     * @return stationId
     */
	public int getStationId() {
		return this.stationId;
    }
	
	/**
	 * Getter for user id
	 * @return userId
	 */
	public String getUserId() {
		return this.username;
	}
	
	/**
	 * Getter for mainReq message
	 * @return message
	 */
	public String getMessage() {
		return this.message;
	}
	
    /**
     * toString method for data to write to csv data files
     * </p>
     * @return toString	the string to store in the mainreq-data.csv
     */
    public String toSaveString() {
    	return (this.id + "," + this.username + "," + this.stationId + "," + this.message + "\n");
    }
    
    /**
     * toString method for mainReq data to output onto console
     * </p>
     * @return toString	the string to print to the console
     */
    public String toViewString(){
    	return ("Issue " + this.id + " at Station " + this.stationId + ":\t" + this.message + "\n");  
    }
}