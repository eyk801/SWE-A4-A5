import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class MapApp extends JPanel {
	
	private ArrayList<Point> points;
	
	public void MapApp() {
		points = new ArrayList<Point>();
	}
	
	/**
     *  Creates and shows the GUI.
     */
    public void createGUI() {
    	
    	JFrame frame = new JFrame();
    	
    	JPanel panel = new JPanel();
    	panel.setLayout(new FlowLayout());
    	
    	BufferedImage mapImage;
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

		addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                points.add(new Point(e.getX(), e.getY()));
                repaint();
            }
        });
		
		frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
        frame.setVisible(true);
    }
    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        for (Point point : points) {
            g2.fillOval(point.x, point.y, 20, 20);
        }
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

}


