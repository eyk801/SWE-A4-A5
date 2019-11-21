import java.sql.Timestamp;

public class Ride {
	
	private int id = 0;
	private String userId = "";
	private int bikeID = 0;
	private int startStationId = 0;
	private int endStationId = 0;
	private Timestamp startTime = new Timestamp(System.currentTimeMillis());
	private Timestamp endTime = new Timestamp(System.currentTimeMillis());

	
	/**
	 * Ride class constructor
	 * 
	 * TODO: Figure out time stamp things
	 */
	public Ride(int id, String userId, int startStationId, int bikeID) {
		this.id = id;
		this.userId = userId;
		this.bikeID = bikeID;
		this.startStationId = startStationId;
		this.startTime = new Timestamp(System.currentTimeMillis());
	}
	
	public void end(int stationId) {
		this.endStationId = stationId;
		this.endTime = new Timestamp(System.currentTimeMillis());
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setUserId(String userID) {
		this.userId = userID;
	}
	
	public int getBikeId() {
		return this.bikeID;
	}
	
	public void setBikeId(int bikeID) {
		this.bikeID = bikeID;
	}
	
	public int getStartStationId() {
		return this.startStationId;
	}
	
	public int getEndStationId() {
		return this.endStationId;
	}
	
	public Timestamp getStartTime() {
		return this.startTime;
	}
	
	public Timestamp getEndTime() {
		return this.endTime;
	}
	
    public String toSaveString() {
        return (this.id + "," + this.userId + "," + this.startStationId + "," + 
        		this.endStationId + "," + this.startTime + "," + this.endTime);
    }
    
    public String toViewString(){
    	return (Integer.toString(this.id) + "\t" 
    					+ this.userId + "\t"
    					+ this.startStationId + "\t"
    					+ this.endStationId + "\t" 
    					+ this.startTime + "\t"
    					+ this.endTime + "\n");  
    }
	
	

}
