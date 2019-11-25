import java.util.*;

public class Station {

    private int id = 0;
    private int avDocks = 0;
    private int capacity = 0;
    private boolean kiosk = false;
    private String address = new String();
    private String name = new String();
    private List<Integer> bikeIds = new ArrayList<>();

    
	/**
	 * Station class constructor
	 * 
	 * TODO: 
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

    public int getId() {
        return this.id;
    }

    public int getAvDocks() {
        return this.avDocks;
    }

    public void setAvDocks(int avDocks) {
        this.avDocks = avDocks;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean getKiosk(){
    	return this.kiosk;
    }

    public String getAddress() {
        return this.address;
    }

    public String getName() {
        return this.name;
    }
    
    public int getNumBikes() {
    	return this.bikeIds.size();
    }
    
    public List<Integer> getBikeIds() {
    	return this.bikeIds;
    }
    
    public void addBike(int bikeId) {
    	// Add input bike to Station list of bikes
    	this.bikeIds.add(bikeId);
    }
    
    public void removeBike(int bikeId) {
    	// Remove bike based on bike id
    	int index = this.bikeIds.indexOf(bikeId);
    	this.bikeIds.remove(index);
    }

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
    
    public String toViewString(){
    	 return (Integer.toString(this.id) + "\t" + Integer.toString(this.bikeIds.size())
    	                + "\t" + Integer.toString(this.avDocks) + "\t"
    	                + Integer.toString(this.capacity) + "\t\t" 
    	                + Boolean.toString(kiosk) 
    	                + "\t" + this.name + " - "
    	                + this.address + "\n");  
    }

}


