import java.awt.Point;
import java.util.*;

/**
 * @author      Ali Eshghi, Charlotte Gephart, Emily Kim, Ester Zhao
 * @version     1.0
 */
public class Station {
	/** Station id */
    private int id = 0;
    /** Number of available docks at station */
    private int avDocks = 0;
    /** Total number of docks at station */
    private int capacity = 0;
    /** Whether the station has a kiosk */
    private boolean kiosk = false;
    /** Station address */
    private String address = new String();
    /** Station name */
    private String name = new String();
    /** List of bikes ids of bikes docked at the station */
    private List<Integer> bikeIds = new ArrayList<>();
    /** Station map coordinates */
    private Point point = new Point();

    
	/**
	 * Station class constructor.
	 * </p>
	 * @param id
	 * @param avDocks
	 * @param capacity
	 * @param kiosk
	 * @param address
	 * @param name
	 * @param bikeIds
	 */
    public Station(int id, int avDocks, int capacity, boolean kiosk,
            String address, String name, List<Integer> bikeIds) {
        this.id = id;
        this.avDocks = avDocks;
        this.capacity = capacity;
        this.kiosk = kiosk;
        this.address = address;
        this.name = name;
        this.bikeIds = bikeIds;
    }
    
    /**
     * Gets the station id.
     * </p>
     * @return id 	the station id
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Sets the station id
     * @param id	the station id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the number of available docks.
     * </p>
     * @return avDocks	the total number of available docks at the station
     */
    public int getAvDocks() {
        return this.avDocks;
    }
    
    /**
     * Sets the number of available docks.
     * </p>
     * @param avDocks	the total number of available docks at the station
     */
    public void setAvDocks(int avDocks) {
        this.avDocks = avDocks;
    }
    
    /**
     * Gets the total capacity for the station
     * </p>
     * @return capacity	the total capacity for the station
     */
    public int getCapacity() {
        return this.capacity;
    }
    
    /**
     * Gets whether there is a kiosk at the station
     * </p>
     * @return	kiosk	boolean stating true if there is a kiosk, false if not
     */
    public boolean getKiosk(){
    	return this.kiosk;
    }
    
    /**
     * Gets the address of a station
     * </p>
     * @return address	the address of the station
     */
    public String getAddress() {
        return this.address;
    }
    
    /**
     * Gets the name of a station
     * </p>
     * @return name		the name of the station
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Gets the number of bikes currently at the station
     * </p>
     * @return numBikes	the number of bikes currently at the station
     */
    public int getNumBikes() {
    	return this.bikeIds.size();
    }
    
    /**
     * Gets the list of bike ids for all bikes currently at the station
     * </p>
     * @return bikeIds	the list of bike ids for all bikes currently at the station
     */
    public List<Integer> getBikeIds() {
    	return this.bikeIds;
    }
    
    /**
     * Adds a bike to the station.
     * </p>
     * @param bikeId	the id of the bike to add to the station
     */
    public void addBike(int bikeId) {
    	// Add input bike to Station list of bikes
    	this.bikeIds.add(bikeId);
    	this.avDocks = this.avDocks - 1;
    }
    
    /**
     * Removes a bike from the station
     * </p>
     * @param bikeId	the id of the bike to remove from the station
     */
    public void removeBike(int bikeId) {
    	// Remove bike based on bike id
    	int index = this.bikeIds.indexOf(bikeId);
    	this.bikeIds.remove(index);
    	this.avDocks = this.avDocks + 1;
    }
    
    /**
     * Returns the station map coordinates
     * @return
     */
    public Point getPoint() {
    	return this.point;
    }
    
    /**
     * Sets the coordinations of the station map point
     * @param x
     * @param y
     */
    public void setPoint(Point p) {
    	this.point.setLocation(p);
    }
    
    
    /**
     * toString method for station data to write to csv data files
     * </p>
     * @return toString	the string to store in the station-data.csv
     */
    public String toSaveString() {
    	// Put bikeIds in csv format
    	String bikes = "";
    	for (int bike : bikeIds) {
    		if (bikes.equals("")) {
    			bikes = bikes + bike;
    		} else {
    			bikes = bikes  + "," + bike;
    		}
    	}
        return (this.id + "," + this.name + "," + this.avDocks + "," + this.capacity + "," +
				this.kiosk + "," + this.address + "," + bikes + "\n");
    }
    
    /**
     * toString method for station data to output onto console
     * </p>
     * @return toString	the string to print to the console
     */
    public String toViewString(){
    	 return (Integer.toString(this.id) + "\t" + Integer.toString(this.bikeIds.size())
    	                + "\t" + Integer.toString(this.avDocks) + "\t"
    	                + Integer.toString(this.capacity) + "\t\t" 
    	                + Boolean.toString(kiosk) 
    	                + "\t" + this.name + " - "
    	                + this.address + "\n");  
    }

}


