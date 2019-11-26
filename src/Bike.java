/**
 * @author      Ali Eshghi, Charlotte Gephart, Emily Kim, Ester Zhao
 * @version     1.0
 */
public class Bike {
	/** Bike id */
    private int id = 0;
    /** Id of last station bike was docked at */
    private int lastStationId = 0;
    /** User id, if bike is checked out. Default = empty string */
    String userId = new String();
    /** Boolean checkedOut, true if bike is checked out. Default = false */
    boolean checkedOut = false;
    
	/**
	 * Bike class constructor
	 * </p>
	 * @param id
	 * @param lastStationId
	 * @param userId
	 * @param checkedOut
	 */
    public Bike(int id, int lastStationId, String userId, boolean checkedOut) {
    	this.id = id;
    	this.lastStationId = lastStationId;
    	this.userId = userId;
    	this.checkedOut = checkedOut;
    }
    
    /** 
     * Getter for bike id.
     * </p>
     * @return id
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Getter for id of last station bike was docked at.
     * </p>
     * @return lastStationId
     */
    public int getLastStationId() {
    	return this.lastStationId;
    }
    
    /**
     * Setter for id of last station bike was docked at.
     * </p>
     * @param stationID		id of last station bike was docked at
     */
    public void setLastStationId(Integer stationID) {
    	this.lastStationId = stationID;
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
     * Setter for user id.
     * </p>
     * @param userID	id of user that has checked out bike
     */
    public void setUserId(String userID) {
    	this.userId = userID;
    }
    
    /**
     * Getter for checkedOut
     * </p>
     * @return checkedOut
     */
    public boolean checkedOut() {
    	return this.checkedOut;
    }
    
    /**
     * Setter for checkedOut
     * </p>
     * @param status	updated checkedOut status
     */
    public void setCheckedOut(boolean status) {
    	this.checkedOut = status;
    }

    /**
     * toString method for data to write to csv data files
     * </p>
     * @return toString	the string to store in the bike-data.csv
     */
    public String toSaveString() {
        return (this.id + "," + this.lastStationId + "," + this.userId + "," + this.checkedOut + "\n");
    }
    
    /**
     * toString method for bike data to output onto console
     * </p>
     * @return toString	the string to print to the console
     */
    public String toViewString(){
    	return (Integer.toString(this.id) + "\t" 
    					+ this.lastStationId + "\t"
    					+ this.userId + "\t"
    					+ this.checkedOut + "\n");  
    }
	
}
