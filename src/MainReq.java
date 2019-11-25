  
public class MainReq {

	private String message = "";
    private int stationId = 0;
    private int mainReqID = 0;
	
	/**
	 * Maintenance Requests class constructor
	 * 
	 * TODO: Figure out how to toString for a message that may have comments in it
	 */
	
	public MainReq(String message, int stationId, into mainReqID) {
		this.message = message;
        this.stationId = stationId;
        this.mainReqID = mainReqID;
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
    
    public int getMRID() {
        return this.mainReqID;
    }

    public void setMRID(int MRID) {
        this.mainReqID = MRID;
    }
	
    public String toSaveString() {
    	// FIX THIS LATER
        return "";
    }
    
    public String toViewString(){
    	return ("Issue " + this.mainReqID + "\t at Station " + this.stationId + "\t" + this.message + "\n");  
    }
}