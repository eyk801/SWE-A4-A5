import java.util.List;
import java.util.ArrayList;

/**
 * @author      Ali Eshghi, Charlotte Gephart, Emily Kim, Ester Zhao
 * @version     1.0
 */
public class User {
	/** User unique username */
	private String username = new String();
	/** User password */
	private String password = new String();
	/** Membership type (0-2) */
	private int membershipType = 0;
	/** User credit card number */
	private long creditCardNum = 0;
	/** User credit card CVV number */
	private int CVV = 0;
	/** User credit card expiration date */
	private String expirationDate = new String();
	/** List of user's past ride ids */
	private List<Integer> rideHistory = new ArrayList<>();
	/** Ride id number for user current ride. Default to 0.*/
	private int currentRideId = 0;
	
	/**
	 * User class constructor.
	 * </p>
	 * @param username
	 * @param password
	 * @param membershipType
	 * @param creditCardNum
	 * @param CVV
	 * @param expirationDate
	 */
	public User(String username, String password, int membershipType, 
			long creditCardNum, int CVV, String expirationDate) {
		this.username = username;
		this.password = password;
		this.membershipType = membershipType;
		this.creditCardNum = creditCardNum;
		this.CVV = CVV;
		this.expirationDate = expirationDate;
	}
	
	/**
	 * Add ride to user history.
	 * </p>
	 * @param rideID	the id of user's ride
	 */
	public void addUserRide(int rideID) {
		rideHistory.add(rideID);
	}
	
	/**
	 * Getter for user id.
	 * </p>
	 * @return username	
	 */
	public String getId() {
		return this.username;	
	}
	
	/**
	 * Setter for username.
	 * @param newId			new username
	 */
	public void setId(String newId) {
		this.username = newId;
	}
	
	/**
	 * Getter for current ride id.
	 * </p>
	 * @return currentRideId
	 */
	public int getCurrentRideId() {
		return this.currentRideId;
	}
	
	/**
	 * Setter for current ride id.
	 * </p>
	 * @param currRideId	new user ride id
	 */
	public void setCurrentRideId(int currRideId) {
		this.currentRideId = currRideId;
	}
	
	/**
	 * Getter for user password.
	 * </p>
	 * @return password
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * Setter for user password
	 * </p>
	 * @param newPass		new user password
	 */
	public void setPassword(String newPass) {
		this.password = newPass;
	}
	
	/**
	 * Getter for user membership type
	 * </p>
	 * @return membershipyType
	 */
	public int getType() {
		return this.membershipType;
	}
	
	/**
	 * Setter for user membershipy type
	 * </p>
	 * @param newMembershipType		new membership type
	 */
	public void setType(int newMembershipType) {
		this.membershipType = newMembershipType;
	}
	
	/**
	 * Getter for user ride history
	 * </p>
	 * @return rideHistory		List of user's previous ride ids
	 */
	public List<Integer> getRides() {
		return this.rideHistory;
	}
	
	/**
	 * Setter for user ride history
	 * </p>
	 * @param rides		List of user's prev ride ids
	 */
	public void setRides(List<Integer> rides) {
		this.rideHistory = rides;
	}
	
	/**
	 * End a user ride.
	 * </p>
	 * Update ride history and reset currentRideId to 0.
	 */
	public void endRide() {
		// Add ride to user history
		addUserRide(this.currentRideId);
		// Default for no ride is rideId == 0
		this.currentRideId = 0;
	}
	
	/**
	 * Checks whether a user is currently on a ride.
	 * </p>
	 * @return boolean	false if the currentRideId is 0, and true otherwise
	 */
	public boolean onRide() {
		if (this.currentRideId == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Converts user ride history to a csv style string.
	 * </p>
	 * @return rideIds	String in csv style of user ride history ids
	 */
	public String ridesToString() {
    	// Make List rideHistory into a csv line
    	String rideIds = "";
    	for (int ride : rideHistory) {
    		if (rideIds.equals("")) {
    			rideIds = rideIds + Integer.toString(ride);
    		} else {
    			rideIds = rideIds + "," + Integer.toString(ride);
    		}
    	}
    	// Catch for when ride history is empty
    	// Auto set to 0
    	if (rideIds.equals("")) {
    		rideIds = "0";
    	}
    	return rideIds;
	}
	
    /**
     * toString method for data to write to csv data files
     * </p>
     * @return toString	the string to store in the user-data.csv
     */
    public String toSaveString() {
    	String rideIds = this.ridesToString();
        return (this.username + "," + this.password + "," + this.membershipType + "," + 
        		this.creditCardNum + "," + this.CVV + "," +
        		this.expirationDate + "," +
        		this.currentRideId + "," + 
        		rideIds + "\n");
    }
    
    /**
     * toString method for user data to output onto console
     * </p>
     * @return toString	the string to print to the console
     */
    public String toViewString(){
    	// Clarify membership type
    	String membership = new String();
    	if (this.membershipType == 0) {
    		membership = "Pay-per-ride";
    	} else if (this.membershipType == 1) {
    		membership = "Pay-per-month";
    	} else {
    		membership = "Pay-per-year";
    	}
    	String rideIds = this.ridesToString();
    	return (" " + this.username + "\t" 
    					+ this.password + "\t"
    					+ membership + "\t"
    					+ this.currentRideId + "\t\t" 
    					+ this.creditCardNum + "\t"
    					+ rideIds + "\n");  
    }
}
