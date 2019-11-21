import java.util.Map;
import java.util.HashMap;

public class Station {

    private int id = 0;
    private int avDocks = 0;
    private int capacity = 0;
    private int kiosk = 0;
    private String address = new String();
    private String name = new String();
    public Map<Integer, Bike> bikes = new HashMap<>();

    
	/**
	 * Station class constructor
	 * 
	 * TODO: 
	 */
    public Station(int id, int avDocks, int capacity, int kiosk,
            String address, String name, Map<Integer, Bike> bikes) {
        this.id = id;
        this.avDocks = avDocks;
        this.capacity = capacity;
        this.kiosk = kiosk;
        this.address = address;
        this.name = name;
        this.bikes = bikes;
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

    public int getKiosk(){
    	return this.kiosk;
    }

    public String getAddress() {
        return this.address;
    }

    public String getName() {
        return this.name;
    }
    
    public void addBike(Bike bike) {
    	// Add input bike to Station list of bikes
    	this.bikes.put(bike.getId(), bike);
    }
    
    public void removeBike(int bikeId) {
    	// Remove bike based on bike id
    	this.bikes.remove(bikeId);
    }

    public String toSaveString() {
        return (this.id + "," + this.name + "," + this.bikes.size() + "," + this.avDocks + ","
				+ this.capacity + "," + this.kiosk + "," + this.address);
    }
    
    public String toViewString(){
    	boolean kioskBool = true;
    	 if (this.kiosk == 0){
    		 kioskBool = false;
    	 }
    	 return (Integer.toString(this.id) + "\t" + Integer.toString(this.bikes.size())
    	                + "\t\t" + Integer.toString(this.avDocks) + "\t"
    	                + Integer.toString(this.capacity) + "\t" 
    	                + Boolean.toString(kioskBool) 
    	                + "\t" + this.name + " - "
    	                + this.address + "\n");  
    }

}


