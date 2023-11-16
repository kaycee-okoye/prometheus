package prometheus.Views;

import javax.swing.*;

import prometheus.Models.LanguageModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Class to create a moveable floating tray with components to be added to CanvasWindow
 * 
 * @author Kenechukwu Okoye
 * @version 2023.11.15
 */
public class ComponentsWindow extends JWindow {

    private int X = 0;
    private int Y = 0;
    private int trayWidth;
    private int trayHeight;
    private int xloc;
    private int yloc;
    private String comp;
    JLabel mousexy;

    /**
     * Constructor for ComponentsWindow class
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param languageModel code templates for a specific programming language
     */
    public ComponentsWindow(LanguageModel languageModel) {
        // Set the size and location based on screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) Math.floor(screenSize.getWidth());
        int screenHeight = (int) Math.floor(screenSize.getHeight());
        trayWidth = screenWidth / 10;
        trayHeight = screenHeight / 2;
        xloc = screenWidth / 13;
        yloc = screenHeight / 4;

        JPanel stem = new JPanel();
        setSize(trayWidth, trayHeight);
        stem.setBorder(
                BorderFactory.createEmptyBorder(
                        3, 3, 0, 3)); // Draw a border for user to drag the tray around
        setLocation(xloc, yloc);

        addMouseListener(new MouseAdapter() {
            // Get cordinates of user click on the tray border
            public void mousePressed(MouseEvent me) {
                X = me.getX();
                Y = me.getY();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            // Move tray based based on displacement of user mouse-drag
            public void mouseDragged(MouseEvent me) {
                Point p = getLocation();
                setLocation(p.x + (me.getX() - X), p.y + (me.getY() - Y));
            }
        });

        GridLayout grid = new GridLayout(0, 1);
        stem.setLayout(grid);

        for (String component : languageModel.getComponentNames(false)) {
            // Initialize default component
            if (comp == null) {
                comp = component;
            }

            // Buttons that when clicked changes the component being drawn
            JButton button = new JButton(component);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    comp = component;
                }
            });
            stem.add(button);
        }

        // Label that will keep track of the mouses coordinates in the GUI builder
        mousexy = new JLabel("x, y", SwingConstants.CENTER);
        stem.add(mousexy);
        add(stem);
        setVisible(true);
    }

    /**
     * Getter method for X
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return x-cordinate of mouse on screen
     */
    public int getX() {
        return X;
    }

    /**
     * Setter method for Y
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return y-cordinate of mouse on screen
     */
    public int getY() {
        return Y;
    }

    /**
     * Setter method for comp
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return name of component currently being drawn
     */
    public String getComp() {
        return comp;
    }

    /**
     * Setter method for mousexy
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return x,y cordinates of the mouse on the screen
     */
    public String getMouseXY() {
        return mousexy.getText();
    }

    /**
     * Setter method for X
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param X x-cordinate of mouse on screen
     */
    public void setX(int X) {
        this.X = X;
    }

    /**
     * Setter method for Y
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param Y y-cordinate of mouse on screen
     */
    public void setY(int Y) {
        this.Y = Y;
    }

    /**
     * Setter method for comp
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param comp name of component currently being drawn
     */
    public void setComp(String comp) {
        this.comp = comp;
    }

    /**
     * Method to set the text at the bottom of the ComponentWindow
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param text text to use, usually the cordinates of the mouse in the window
     */
    public void setMouseXY(String text) {
        mousexy.setText(text);
    }

    /**
     * Method to make the close window
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void close() {
        dispose();
    }

    /**
     * Method to make toggle win
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void visibility() {
        setVisible(!isVisible());
    }

    /**
     * Method to make the Window visible
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void makeVisible() {
        setVisible(true);
    }
}
