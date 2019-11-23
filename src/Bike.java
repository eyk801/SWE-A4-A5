public class Bike {
	
    private int id = 0;
    private int lastStationId = 0;
    String userId = "";
    boolean checkedOut = false;
    
    
	/**
	 * Bike class constructor
	 * 
	 * TODO: 
	 */
    
    public Bike(int id, int lastStationId, String userId, boolean checkedOut) {
    	this.id = id;
    	this.lastStationId = lastStationId;
    	this.userId = userId;
    	this.checkedOut = checkedOut;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getLastStationId() {
    	return this.lastStationId;
    }
    
    public String getUserId() {
    	return this.getUserId();
    }
    
    public void setUserId(String userID) {
    	this.userId = userID;
    }
    
    public boolean checkedOut() {
    	return this.checkedOut;
    }
    
    public void setCheckedOut(boolean status) {
    	this.checkedOut = status;
    }
    
    public void setLastStationId(Integer stationID) {
    	this.lastStationId = stationID;
    }
    
    public String toSaveString() {
        return (this.id + "," + this.lastStationId + "," + this.userId + "," + this.checkedOut);
    }
    
    public String toViewString(){
    	return (Integer.toString(this.id) + "\t" 
    					+ this.lastStationId + "\t"
    					+ this.userId + "\t"
    					+ this.checkedOut + "\n");  
    }
	
}
