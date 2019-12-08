import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.*; 

public class MapApp{
	/** Instance of the ValleyBikeSim */
	private ValleyBikeSim valleyBike = ValleyBikeSim.getInstance();
	/** New station point */
	private Point newPoint = new Point();
	/** JFrame frame */
	JFrame frame;
	
	// to call in valleybike functions: new MapApp(boolean);
	// true for user, false for employee
	public MapApp(boolean user) {
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame();
                frame.add(new App(user));
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
	}

    
    public class App extends JPanel {
    	
    	private ArrayList<Point> points  = new ArrayList<Point>();
    	private JFrame dialog;
    	private boolean user;
    	BufferedImage mapImage;
    	
    	
    	public App(boolean user) {
    		this.user = user;
    		Map<Integer, Station> stations = valleyBike.getStations();
    		
    		// Loop over stations map
    		for (Map.Entry<Integer,Station> entry : stations.entrySet()) {
    			// Add station coordinates to points list
    			points.add(entry.getValue().getPoint());
    		}
    		
    		try {
    			mapImage = ImageIO.read(new File("data-files/ValleyBikeMap.png"));
    		} catch (IOException e) {
    			System.out.println("Invalid file name.");
    			e.printStackTrace();
    		}
    		
    		addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	Point p = new Point(e.getX(), e.getY());
                	// If in user view
    				if (user == true) {
    					//get station from coordinates here
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
    						// Set the newStation point to the new point
    						setPoint(p);
    			            if (mapImage != null) {
    		                    Graphics2D g2d = mapImage.createGraphics();
    		                    g2d.setColor(Color.black);
    		                    g2d.fillOval(p.x - 10, p.y - 10, 20, 20);
    		                    g2d.dispose();
    		                    repaint();
    		                }
    			            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    					}
    				}
                }
            });   		
    	}
    	
    	@Override
        public Dimension getPreferredSize() {
            return mapImage == null ? new Dimension(200, 200) : new Dimension(mapImage.getWidth(), mapImage.getHeight());
        }
    	
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
        
        public void showInfo(Station s) {
        	JOptionPane.showMessageDialog(dialog,
        			"Station " + s.getId() + "\n" +
        					s.getName() + "\n" +
        					"Available Bikes: " + s.getNumBikes() + "\n" +
        					"Available Docks: " + s.getAvDocks() + "\n",
            		"Station Information",
            		JOptionPane.INFORMATION_MESSAGE);
        }
        
        public int confirmStation() {
        	return JOptionPane.showConfirmDialog(dialog,
        			"Is this location correct?",
        			"Confirm Station",       		
            	    JOptionPane.YES_NO_OPTION);
        }
        
    	boolean inBounds(int mouseX, int mouseY, int x, int y) {
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
	 * 
	 */
	public Point getPoint() {
		return this.newPoint;
	}
    
	/**
	 * MapApp main method.
	 * @throws ParseException 
	 */
	public static void main(String[] args){
		MapApp map = new MapApp(true);
	}
	
	
}


