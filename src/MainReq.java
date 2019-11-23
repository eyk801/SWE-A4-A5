//package src;


public class MainReq {

	private String message = "";
	private int stationId = 0;
	
	/**
	 * Maintenance Requests class constructor
	 * 
	 * TODO: Figure out how to toString for a message that may have comments in it
	 */
	
	public MainReq(String message, int stationId) {
		this.message = message;
		this.stationId = stationId;
	}
	
	public void changeMessage(String newMessage) {
		this.message = newMessage;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public int getStationId() {
		return this.stationId;
	}
	
    public String toSaveString() {
    	// FIX THIS LATER
        return "";
    }
    
    public String toViewString(){
    	return (this.message + "\t" + this.stationId + "\n");  
    }
}
