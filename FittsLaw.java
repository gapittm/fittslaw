package fittslaw;

import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileWriter;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.*;
import javax.swing.Timer;

public class FittsLaw extends JPanel implements ActionListener {

    // Constants
    static private final int XDIM = 1000, YDIM = 1000;
    static private final int SMAX = 200, SMIN = 10;
    static private final int TRIALNUM = 50;
    
    // Counters
    private int trials = TRIALNUM;
    private double count = 5.5;
    private long timeStart, timeDiff;
    
    // Components
    static private FittsLaw content;
    static private CircleButton buttonCir;
    static private Timer timer;
    static private JLabel countdown;
    
    // File output
    static private File outFile;
    static private FileWriter output;
    
    public FittsLaw() {
        
        // Set up circle and set random attributes
        buttonCir = new CircleButton(SMAX, SMIN, XDIM, YDIM);
        buttonCir.setActionCommand("select");
        buttonCir.addActionListener(this);
        buttonCir.reset();
        this.add(buttonCir);
        
        // Hide the circle until needed
        buttonCir.setEnabled(false);
        buttonCir.setVisible(false);
        
        // Set up countdown
        timer = new Timer(1000, this);
        timer.setInitialDelay(0);
        timer.setActionCommand("count");
        timer.addActionListener(this);
        
        countdown = new JLabel(Double.toString(count), JLabel.CENTER);
        countdown.setBounds(XDIM / 2 - 10, YDIM / 2 - 10, 20, 20);
        
        // Set up output
        outFile = new File("output.csv");
        
        try {
            output = new FileWriter(outFile, false);
        } catch (IOException ex) {
            System.err.println("IO Error: " + ex);
        }
    }
    
    /**
     * Creates the high level swing components
     * Uses an instance to create the rest
     * Pre: None
     * Post: All components created and displayed
     */
    private static void setup() {
        // Frame for everything
        JFrame frame = new JFrame("Fitts Law");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(XDIM, YDIM));
        
        // Panel containing the circle and countdown
        content = new FittsLaw();
        content.setOpaque(true);
        content.setBackground(new Color(230, 230, 230));
        content.setPreferredSize(new Dimension(XDIM, YDIM));
        content.setLayout(null);
        content.add(countdown);
        
        // Finalize and display frame
        frame.add(content);
        frame.pack();
        frame.setVisible(true);
        
        // Starting prompt
        JOptionPane.showMessageDialog(content,
            "Click the circles as fast as possible! Select OK to start.", 
            "Fitt's Law", JOptionPane.INFORMATION_MESSAGE);
        
        // Start countdown
        timer.start();
    }
    
    /**
     * Circle and timer actions
     * Pre: Circle / Timer exists
     * Post: Reset circle, disable timer,
     *       display pop up and close program
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            
            // Circle is selected
            case "select":
                
                // Reset circle and record results
                if(trials > 0){
                    
                    // Size and distance
                    int[] newCir = buttonCir.reset();
                    
                    // Time to click
                    timeDiff = TimeUnit.NANOSECONDS.
                        toMillis(System.nanoTime() - timeStart);
                    
                    // Create output line and write to file
                    String line = Integer.toString(TRIALNUM + 1 - trials) + "," +
                        Integer.toString(newCir[0]) + "," +
                        Integer.toString(newCir[1]) + "," +
                        Long.toString(timeDiff) + "\n";
                    
                    try {
                        output.write(line);
                    } catch (IOException ex) {
                        System.err.println("IO Error: " + ex);
                    }
                    
                    // Prepare for next click
                    timeStart = System.nanoTime();
                    trials--;
                    
                // Display finish popup, then exit program
                } else {
                    try {
                        output.close();
                    } catch (IOException ex) {
                        System.err.println("IO Error: " + ex);
                    }
                    
                    JOptionPane.showMessageDialog(content,
                        "Thanks for playing!", "Fitt's Law",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    System.exit(0);
                }
                
                break;
                
            // Timer counts down
            case "count":
                
                // Disable timer and start trials
                if(count < 0){
                    if(timer.isRunning()){
                        timer.stop();
                        countdown.setVisible(false);
                        buttonCir.setEnabled(true);
                        buttonCir.setVisible(true);
                        timeStart = System.nanoTime();
                    }
                } else {
                    // Triggered twice per second for some reason, so
                    // decrements were decreased by half.
                    //System.out.println(count);
                    countdown.setText(Double.toString(count));
                    count -= 0.5;
                }
                
                break;
                
            default:
                break;
        }
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setup();
            }
        });
    }
    
}
