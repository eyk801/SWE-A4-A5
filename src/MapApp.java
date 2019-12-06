import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.*; 

public class MapApp extends JPanel {
	
	private ArrayList<Point> points  = new ArrayList<Point>();
	private JFrame dialog;
	private boolean userClick = false;
	private boolean user;
	BufferedImage mapImage;
	
	public void MapApp() {

	}
	
	/**
     *  Creates and shows the GUI.
     */
    public void createGUI() {

    	JFrame frame = new JFrame();
    	
    	JPanel panel = new JPanel();
    	panel.setLayout(new FlowLayout());
    	
    	//BufferedImage mapImage;
    	int imageWidth;
    	int imageHeight;
		try {
			mapImage = ImageIO.read(new File("data-files/ValleyBikeMap.png"));
			JLabel picLabel = new JLabel(new ImageIcon(mapImage));
			imageWidth = mapImage.getWidth();
			imageHeight = mapImage.getHeight();
			frame.setSize(imageWidth, imageHeight);
	    	panel.add(picLabel);
		} catch (IOException e) {
			System.out.println("Invalid file name.");
			e.printStackTrace();
		}
		
		frame.addMouseListener(new MouseListener());
		frame.addMouseMotionListener(new MouseListener());
		
		frame.add(panel);		
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    

    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(mapImage, 0, 0, this);
        g2d.dispose();
    }
    
    public void showInfo() {
    	if (userClick == true) {
    		JOptionPane.showMessageDialog(dialog,
        		    "Info",
        		    "Station Information",
        		    JOptionPane.INFORMATION_MESSAGE);
    		userClick = false;
    	}
    	
    }

	
	public static void runUserMap() {
		final MapApp userMap = new MapApp();
		userMap.user = true;
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			    userMap.createGUI();
			}
		});
	}
	

	public static void runBackendMap() {
		final MapApp backendMap = new MapApp();
		backendMap.user = false;
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			    backendMap.createGUI();
			}
		});
	}
	
	boolean inBounds(int mouseX, int mouseY, int x, int y) {
	    return ((mouseX >= x - 10) && (mouseY >= y - 10) && (mouseX < x + 10) && (mouseY < y + 10));
	}

	
	private class MouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			Point p = new Point(e.getX(), e.getY());
			System.out.println("Click event at (" + p + ")");
			if (user == false) {
				userClick = true;
				showInfo();
			}
			else {
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
			}
	

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


