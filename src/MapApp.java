import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MapApp {
	
	private AppStationVisual station;
	
	public void MapApp() {
		
		station = new AppStationVisual(500,500);
	}
	
	/**
     *  Creates and shows the GUI.
     */
    public void createGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("ValleyBike Station Map");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		// Add map image
		createMap(frame.getContentPane());
	
		// Display the window.
		frame.pack();
		frame.setVisible(true);
    }
	
    public void createMap(Container pane) {
    	pane.setLayout(new FlowLayout());
    	JPanel panel = new JPanel();
    	panel.setLayout(new FlowLayout());
    	
    	BufferedImage mapImage;
		try {
			mapImage = ImageIO.read(new File("data-files/ValleyBikeMap.png"));
			JLabel picLabel = new JLabel(new ImageIcon(mapImage));
	    	panel.add(picLabel);
		} catch (IOException e) {
			System.out.println("Invalid file name.");
			e.printStackTrace();
		}
    	pane.add(panel);
    	
    	pane.addMouseListener(new MouseListener());
		pane.addMouseMotionListener(new MouseListener());
		
    }

	
	public static void runUserMap() {
		final MapApp userMap = new MapApp();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			    userMap.createGUI();
			}
		});
	}
	
	/*
	public static void runBackendMap() {
		final MapApp backendMap = new MapApp();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			    backendMap.createAndShowGUI();
			}
		});
	}
	*/
	
    
	
	private class MouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			//e.getX();
			//e.getY();
			System.out.println("Click event at (" + e.getX() + "," + e.getY() + ")");
	
		}
		public void mousePressed(MouseEvent e) {
	
		}
		public void mouseReleased(MouseEvent e) {
	
		}
		public void mouseEntered(MouseEvent e) {
	
		}
		public void mouseExited(MouseEvent e) {
	
		}
    }

}


