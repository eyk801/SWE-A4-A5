import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Ride {
	
	private int id = 0;
	private String userId = "";
	private int bikeId = 0;
	private int startStationId = 0;
	private int endStationId = 0;
	private Timestamp startTime = new Timestamp(System.currentTimeMillis());
	private Timestamp endTime = new Timestamp(System.currentTimeMillis());
	private boolean currentRide = true;

	
	/**
	 * Ride class constructor
	 * 
	 * TODO: Figure out time stamp things
	 */
	public Ride(int id, String userId, int startStationId, int bikeId) {
		this.id = id;
		this.userId = userId;
		this.bikeId = bikeId;
		this.startStationId = startStationId;
	}

    /**
     * Can update when ride class updates
     * @return
     */
    public boolean isCurrentRide() {
    	if(currentRide == true) {
    		return true;
    	} else {
    		return false;
    	}
    }
	
	public void end(int stationId) {
		this.endStationId = stationId;
		this.endTime = new Timestamp(System.currentTimeMillis());
		this.currentRide = false;
	}
	
	public void setStartTime(String time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = formatter.parse(time);
		Timestamp ts = new Timestamp(date.getTime()); 
		this.startTime = ts;
	}
	
	public void setEndTime(String time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = formatter.parse(time);
		Timestamp ts = new Timestamp(date.getTime()); 
		this.startTime = ts;
	}
	
	public void setEndStation(int station) {
		this.endStationId = station;
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
		return this.bikeId;
	}

	public void setBikeId(int bikeID) {
		this.id = bikeId;
	}
	
	public int getStartStationId() {
		return this.startStationId;
	}
	
	public int getEndStationId() {
		return this.endStationId;
	}
	
	public String getStartTime() {
		Date date = new Date();
		date.setTime(this.startTime.getTime());
		String stringDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return stringDate;
	}
	
	public String getEndTime() {
		Date date = new Date();
		date.setTime(this.endTime.getTime());
		String stringDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return stringDate;
	}
	
    public String toSaveString() {
    	String start = getStartTime();
    	String end = getEndTime();
        return (this.id + "," + this.userId + "," + this.startStationId + "," + 
        		this.endStationId + "," this.bikeId + "," + start + "," + end);
    }
    
    public String toViewString(){
    	return (Integer.toString(this.id) + "\t" 
    					+ this.userId + "\t"
    					+ this.startStationId + "\t"
    					+ this.endStationId + "\t" 
    					+ this.bikeId + "\t"
    					+ this.startTime + "\t"
    					+ this.endTime + "\n");  
    }
}
