public class MainReq {
	
	private int id = 0;
    private int stationId = 0;
    private String username = "";
	private String message = "";
	
	/**
	 * Maintenance Requests class constructor
	 * 
	 * TODO: Figure out how to toString for a message that may have comments in it
	 */
	
	public MainReq(int id, String username, int stationId, String message) {
		this.id = id;
		this.username = username;
		this.stationId = stationId;
		this.message = message;
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
    
    public int getId() {
        return this.id;
    }

    public void setId(int MRID) {
        this.id = MRID;
    }
	
    public String toSaveString() {
    	return (this.id + "," + this.username + "," + this.stationId + "," + this.message);
    }
    
    public String toViewString(){
    	return ("Issue " + this.id + "\t at Station " + this.stationId + "\t" + this.message + "\n");  
    }
}