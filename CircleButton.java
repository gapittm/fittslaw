package fittslaw;

import java.util.Random;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.Point2D;
import javax.swing.JButton;

public class CircleButton extends JButton {
    
    private static final Color[] COLORS =
        {Color.RED, Color.BLUE, Color.GREEN};
    
    private int sizeMax, sizeMin;
    private int xMax, yMax;
    
    public CircleButton(int sMax, int sMin, int xMax2, int yMax2) {
        super();  
        setContentAreaFilled(false);
        
        sizeMax = sMax;
        sizeMin = sMin;
        xMax = xMax2;
        yMax = yMax2;
    }
    
    /**
     * Relocates, resizes, and recolors the circle
     * Pre: Circle exists
     * Post: Bounds (dimensions and location) and color change
     * Return: Integer array [new size, distance to new circle]
     */
    public int[] reset() {
        Random rand = new Random();
        setBackground(COLORS[rand.nextInt(3)]);
        
        // New size and location
        int sizeNew = rand.nextInt(sizeMax - sizeMin) + sizeMin;
        int xNew = rand.nextInt(xMax - sizeMax);
        int yNew = rand.nextInt(yMax - sizeMax);
        
        // Save the current bounds before applying the new ones
        Rectangle current = getBounds();
        setBounds(xNew, yNew, sizeNew, sizeNew);
        
        // Distance between the centers of the two sets of bounds
        int dist = (int) Point2D.distance(current.x + current.width / 2, 
            current.y + current.height / 2, xNew + sizeNew / 2,
            yNew + sizeNew / 2);
        
        int[] temp = {sizeNew, dist};
        return temp;
    }
    
    /**
     * Draws a circle with a single color
     * Pre: Height and width are equal
     * Post: Circle is drawn
     */
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillOval(0, 0, getSize().width, getSize().width);
        super.paintComponent(g);
    }
    
    /**
     * Blank so no border is drawn
     * Pre: None
     * Post: None
     */
    @Override
    protected void paintBorder(Graphics g) {
        
    }
    
    /**
     * Determines if the cursor is in the circle
     * Pre: Height and width are equal
     * Post: True if the cursor is in the circle, false otherwise
     */
    @Override
    public boolean contains(int x, int y) {
        int radius = getSize().width / 2;
        return Point2D.distance(x, y, radius, radius) < radius;
    }
}
