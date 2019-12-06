import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.*; 

public class MapApp{
	
	public MapApp() {
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.add(new App());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });		
	}

    
    public class App extends JPanel {
    	
    	private ArrayList<Point> points  = new ArrayList<Point>();
    	private JFrame dialog;
    	private boolean user = false;
    	BufferedImage mapImage;
    	JFrame frame;
    	
    	public App() {
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
    				System.out.println("Click event at (" + p + ")");
    				if (user == true) {
    					//get station from coordinates here
    					showInfo();
    				}
    				else {
    					if (confirmStation() == 0) {
    						points.add(p);
    			            System.out.println(points);
    			            
    			            if (mapImage != null) {
    			            	System.out.println("circle");
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
        }
        
        public void showInfo() {
        	JOptionPane.showMessageDialog(dialog,
        			"Info",
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
}


