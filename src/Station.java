public class Station {

    private int id = 0;
    private int bikes = 0;
    private int pedelecs = 0;
    private int avDocks = 0;
    private int mainReq = 0;
    private int capacity = 0;
    private int kiosk = 0;
    private String address = new String();
    private String name = new String();

    public Station(int id, int bikes, int pedelecs, int avDocks, int mainReq, int capacity, int kiosk,
            String address, String name) {
        this.id = id;
        this.bikes = bikes;
        this.pedelecs = pedelecs;
        this.avDocks = avDocks;
        this.mainReq = mainReq;
        this.capacity = capacity;
        this.kiosk = kiosk;
        this.address = address;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public int getBikes() {
        return this.bikes;
    }
    
    public void setBikes(int numBikes) {
        this.bikes = numBikes;
    }

    public int getPedelecs() {
        return this.pedelecs;
    }
    
    public void setPedelecs(int numPeds) {
        this.pedelecs = numPeds;
    }

    public int getAvDocks() {
        return this.avDocks;
    }

    public void setAvDocks(int avDocks) {
        this.avDocks = avDocks;
    }
    
    public int getMainReq() {
        return this.mainReq;
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

    public String toSaveString() {
        return (this.id + "," + this.name + "," + this.bikes + ","
				+ this.pedelecs + "," + this.avDocks +
				"," + this.mainReq + ","
				+ this.capacity + "," + this.kiosk + "," + this.address);
    }
    
    public String toViewString(){
    	boolean kioskBool = true;
    	 if (this.kiosk == 0){
    		 kioskBool = false;
    	 }
    	 return (Integer.toString(this.id) + "\t" + Integer.toString(this.bikes) + "\t" + Integer.toString(this.pedelecs)
    	                + "\t\t" + Integer.toString(this.avDocks) + "\t" + Integer.toString(this.mainReq) + "\t"
    	                + Integer.toString(this.capacity) + "\t" + Boolean.toString(kioskBool) + "\t" + this.name + " - "
    	                + this.address + "\n");  
    }

}


