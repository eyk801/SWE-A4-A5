import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

/**
 * @author      Charlotte Gephart, Ester Zhao
 * @version     1.0
 */
public class MapApp{
	/** Instance of the ValleyBikeSim */
	private ValleyBikeSim valleyBike = ValleyBikeSim.getInstance();
	/** New station point */
	private Point newPoint = new Point();
	/** JFrame frame */
	JFrame frame;
	
	/**
	 * MapApp class constructor.
	 * </p>
	 * Creates JFrame and calls App class
	 */ 
	public MapApp(boolean user) {
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame();
                frame.add(new App(user));
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
	}

	/**
	 *  @author      Charlotte Gephart, Ester Zhao
	 *  @version     1.0
	 */ 
    public class App extends JPanel {
    	private static final long serialVersionUID = (long) 0.01;
    	/** ArrayList of station points on the map */
    	private ArrayList<Point> points  = new ArrayList<Point>();
    	/** JFrame dialog box for station information and adding station confirmation */
    	private JFrame dialog;
    	/** Map image for map background */
    	BufferedImage mapImage;
    	/** Logo image for dialog icon */
    	ImageIcon icon;
    	
    	/**
    	 * App class constructor.
    	 * </p>
    	 * Initializes class variables, calls method to get stations and get existing station points
    	 * Reads in and loads map image
    	 * Adds MouseListener and provides protocol on click activity
    	 */
    	public App(boolean user) {
    		Map<Integer, Station> stations = valleyBike.getStations();
    		
    		// Loop over stations map
    		for (Map.Entry<Integer,Station> entry : stations.entrySet()) {
    			// Add station coordinates to points list
    			points.add(entry.getValue().getPoint());
    		}
    		
    		// Load in map image
    		try {
    			mapImage = ImageIO.read(new File("data-files/ValleyBikeMap.png"));
    		} catch (IOException e) {
    			System.out.println("Invalid file name.");
    			e.printStackTrace();
    		}
    		
    		icon = new ImageIcon(new ImageIcon("data-files/ValleyBikeLogo.jpg").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));
    		
    		// instruction dialog box for addStation
    		if (user == false) {
    			addInstruction();
    		}
    		
    		// Add MouseListener and declare protocol for action
    		addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	Point p = new Point(e.getX(), e.getY());
                	// If in user view
    				if (user == true) {
    					//get station from coordinates here and show station info
    					for (Point pt : points) {
    						if (inBounds(p.x, p.y, pt.x, pt.y)) {
    							for (Map.Entry<Integer,Station> s : stations.entrySet()) {
    				    			if (inBounds(p.x, p.y, s.getValue().getPoint().x, s.getValue().getPoint().y)) {
    				    				showInfo(s.getValue());
    				    			}
    				    		}    							
    						}
    					}    					
    				}
    				else { // if in employee view				
    					if (confirmStation() == 0) {
    						points.add(p);
    						// Set the newStation point to the new point and draw new station onto map
    						setPoint(p);
    			            if (mapImage != null) {
    		                    Graphics2D g2d = mapImage.createGraphics();
    		                    g2d.setColor(Color.black);
    		                    g2d.fillOval(p.x - 10, p.y - 10, 20, 20);
    		                    g2d.dispose();
    		                    repaint();
    		                }
    			            // Update valleybike window open
    			            valleyBike.setWindowOpen(false);
    			            // Unlock valleybike wait
    			            Object LOCK = valleyBike.getLock();
    			            synchronized (LOCK) {
    			                LOCK.notifyAll();
    			            }
    			            // close window
    			            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    					}
    				}
                }
            });   		
    	}
    	
    	/**
    	 * Creates new Dimension
    	 * </p>
    	 * Sets frame dimension to size of mapImage
    	 * @return Dimension for mapImage JFrame
    	 */
    	@Override
        public Dimension getPreferredSize() {
            return mapImage == null ? new Dimension(200, 200) : new Dimension(mapImage.getWidth(), mapImage.getHeight());
        }
    	
    	/**
    	 * Paints graphics onto frame
    	 * </p>
    	 * Paints mapImage to background and existing station points to frame
    	 */
        @Override
        protected void paintComponent(Graphics g) {
        	super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(mapImage, 0, 0, this);
            g2d.dispose();
            
            for (Point pt : points) {
				if (mapImage != null) {
                    Graphics2D gr = mapImage.createGraphics();
                    gr.setColor(Color.black);
                    gr.fillOval(pt.x - 10, pt.y - 10, 20, 20);
                    gr.dispose();
                    repaint();
                }
			}
        }
        
        /**
    	 * Shows station info dialog
    	 * </p>
    	 * Creates a dialog box in a frame to display station information
    	 */
        public void showInfo(Station s) {
        	JOptionPane.showMessageDialog(dialog,
        			"Station " + s.getId() + "\n" +
        					s.getName() + "\n" +
        					"Available Bikes: " + s.getNumBikes() + "\n" +
        					"Available Docks: " + s.getAvDocks() + "\n",
            		"Station Information",
            		JOptionPane.INFORMATION_MESSAGE,
            		icon);
        }
        
        /**
    	 * Shows addStation instruction dialog
    	 * </p>
    	 * Creates a dialog box in a frame to provide instruction during addStation
    	 */
        public void addInstruction() {
        	JOptionPane.showMessageDialog(dialog,
        			"Please click the new station location on the map.",
            		"Add Station",
            		JOptionPane.INFORMATION_MESSAGE,
            		icon);
        }
        
        /**
    	 * Shows station confirmation dialog
    	 * </p>
    	 * Creates a dialog box in a frame to request confirmation of correct location during station addition
    	 * @return 0 if user clicks "yes" option, 1 if user clicks "no" option
    	 */
        public int confirmStation() {
        	return JOptionPane.showConfirmDialog(dialog,
        			"Is this location correct?",
        			"Confirm Station",       		
            	    JOptionPane.YES_NO_OPTION,
            	    JOptionPane.QUESTION_MESSAGE,
            		icon);
        }
        
        /**
    	 * Checks if a click was inside a station map marker
    	 * </p>
    	 * @return true if the click was inside the coordinates + tolerance, else false
    	 */
    	private boolean inBounds(int mouseX, int mouseY, int x, int y) {
    	    return ((mouseX >= x - 10) && (mouseY >= y - 10) && (mouseX < x + 10) && (mouseY < y + 10));
    	}
    	
    }
    
	/**
	 * Set the new station coordinates
	 * @param p - the new station coordinates
	 */
	public void setPoint(Point p) {
		this.newPoint = p;
	}
	
	/**
	 * Get the station point
	 * @return newPoint
	 */
	public Point getPoint() {
		return this.newPoint;
	}
	
}


