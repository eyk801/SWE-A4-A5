import java.util.List;
import java.util.ArrayList;

public class User {
	private String username = "";
	private String password = "";
	private int membershipType = 0;
	private int currentRideId = 0;
	private int creditCardNum = 0;
	private int CVV = 0;
	private String expirationDate = "";
	private List<Integer> rideHistory = new ArrayList<>();
	
	/**
	 * User class constructor
	 * 
	 * TODO: Include any methods involving payment system, check toString methods
	 */
	public User(String username, String password, int membershipType, 
			int creditCardNum, int CVV, String expirationDate) {
		this.id = id;
		this.password = password;
		this.membershipType = membershipType;
		this.currentRideId = currentRideId;
		this.creditCardNum = creditCardNum;
		this.CVV = CVV;
		this.expirationDate = expirationDate;
		this.rideHistory = rideHistory;
	}
	

	/**
	 * This is where we will check that user is valid (re: payment methods)
	 * @return
	 * TODO: implement
	 */
	public boolean validUser() {
		return true;
	}
	
	public String getId() {
		return this.username;	
	}
	
	public void setId(String newId) {
		this.username = newId;
	}
	
	public void setPassword(String newPass) {
		this.password = newPass;
	}
	
	public int getType() {
		return this.membershipType;
	}
	
	public void setType(int newMembershipType) {
		this.membershipType = newMembershipType;
	}
	
	public void setRideId(int rideId) {
		this.currentRideId = rideId;
	}
	
	public void endRide() {
		// Default for no ride is rideId == 0
		this.currentRideId = 0;
	}
	
	public int getRideId() {
		return this.currentRideId;
	}
	
	public boolean onRide() {
		if (this.currentRideId == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public List<Integer> getRides() {
		return this.rideHistory;
	}
	
	public String ridesToString() {
    	// Make List rideHistory into a csv line
    	String rideIds = "";
    	for (int ride : rideHistory) {
    		rideIds = rideIds + "," + Integer.toString(ride);
    	}
    	return rideIds;
	}
	
    public String toSaveString() {
    	String rideIds = this.ridesToString();
    	
        return (this.username + "," + this.password + "," + this.membershipType + "," + 
        		this.currentRideId + "," + this.creditCardNum + "," + this.CVV + "," +
        		this.expirationDate + "," + rideIds);
    }
    
    public String toViewString(){
    	String rideIds = this.ridesToString();
    	return (this.username + "\t" 
    					+ this.password + "\t"
    					+ this.membershipType + "\t"
    					+ this.currentRideId + "\t" 
    					+ this.creditCardNum + "\t"
    					+ rideIds + "\n");  
    }
}
