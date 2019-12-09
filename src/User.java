import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
	/** User bill history */
	private List<String> billHistory = new ArrayList<>();
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
	 * Return user credit card number
	 * </p>
	 * @return long credit card number
	 */
	public long getCard() {
		return this.creditCardNum;
	}
	
	/**
	 * Return user credit card CVV
	 * </p>
	 * @return int credit card CVV
	 */
	public int getCVV() {
		return this.CVV;
	}
	
	/**
	 * Return user credit card expiration date
	 * </p>
	 * @return String credit card expiration date
	 */
	public String getExprDate() {
		return this.expirationDate;
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
	 * This method decides whether an account will be charged based on the membership type.
	 * @return true if account was charged, false if not.
	 */
	public boolean chargeAccount() {
	    // Get current date/time
     	LocalDate today = LocalDate.now();
 		// Get date info from the last bill charge
     	System.out.println(billHistory.toString());
 		String entry = billHistory.get(billHistory.size()-1);
 		// Parses string to get local date object
 		LocalDate lastPayment = LocalDate.parse(entry);
     	long difference = ChronoUnit.DAYS.between(lastPayment, today);
     	// If pay-per-month
     	if (membershipType == 1) {
         	// If the difference is more than 30, charge the account
         	if (difference > 30) {
         		this.billHistory.add(today.toString());
         		return true;
         	} else {
         		return false;
         	}
     	} else if (membershipType == 2) {
     		// If the difference is more than 365, charge account
     		if (difference > 365) {
     			this.billHistory.add(today.toString());
     			return true;
     		} else {
     			return false;
     		}
     	} else {
     		 // If pay-per-ride, charge account
     		System.out.println("day to string:");
     		System.out.println(today.toString());
     		this.billHistory.add(today.toString());
     		return true;
     	}
	}
	
	/**
	 * Set the bill history using read in data
	 * @param billHistory list of dates when user was billed
	 */
	public void setBillHistory(List<String> billHistory) {
		//TODO: convert from string 00/00 format to date
		this.billHistory = billHistory;
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
	
	public String billToString() {
		// Make bill history into a csv line
    	String billString = "";
    	for (String date : billHistory) {
    		if (billString.equals("")) {
    			billString = billString + date;
    		} else {
    			billString = "," + date;
    		}
    	}
    	// catch for when bill history is empty
    	// autoset to 0
    	if (billString.equals("")) {
    		billString = "0";
    	}
    	return billString;
	}
	
    /**
     * toString method for data to write to csv data files
     * </p>
     * @return toString	the string to store in the user-data.csv
     */
    public String toSaveString() {
    	String rideIds = ridesToString();
    	String billString = billToString();
    	System.out.println("Bill string in save:");
    	System.out.println(billString);
        return (this.username + "," + this.password + "," + this.membershipType + "," + 
        		this.creditCardNum + "," + this.CVV + "," +
        		this.expirationDate + "," +
        		this.currentRideId + "," + 
        		rideHistory.size() + "," +
        		rideIds + "," +
        		billString + "," + "\n");
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
