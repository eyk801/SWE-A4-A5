import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;

public class AppStationVisual extends JComponent{
	
	private int x;
	private int y;
	private int diameter = 10;
	private Color c = Color.black;

	AppStationVisual(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(c);
		g.fillOval(x, y, diameter, diameter);		
	}
	
	
}
