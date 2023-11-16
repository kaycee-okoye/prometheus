package prometheus.Views;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import prometheus.Constants.Constants;
import prometheus.Controllers.GuiWriter;
import prometheus.Controllers.SQLHandler;
import prometheus.LanguageModels.PythonModel;
import prometheus.Models.LanguageModel;
import prometheus.Models.Rectangle;
import prometheus.Models.Rectangle.PERIMETER_LOCATION;

/**
 * Class to create the main window containing the canvas for UI design
 * 
 * @author Kenechukwu Okoye
 * @version 2023.11.15
 */
public class CanvasWindow extends JPanel {
    // List of rectangles drawn by the user
    ArrayList<Rectangle> drawnRectangles = new ArrayList<Rectangle>();
    int stackPointer = 0; // Rectangles in drawnRectangles with indices
    // greater than this value will not be drawn. This will be used to
    // implement removeLastRectangle and redrawLastRemovedRectangle functionality

    JFrame root; // Root to link all the extra windows to so they get focus with this class and
                 // close with it
    InformationWindow informationWindow; // Information window that hovers to the left of the canvas
    ComponentsWindow componentsTray; // Tray of possible components to add to GUI
    LanguageModel languageModel; // Programming language model to be used

    int highlightedRectangleIndex = -1; // Index of rectangle that is currently being edited
    boolean showGrid = false; // Whether the grid is currently being displayed

    int screenWidth; // Screen width passed from Prometheus class
    int screenHeight; // Screen height passed from Prometheus class
    int canvasWidth; // Width of canvas
    int canvasHeight; // Height of canvas
    int xlocCanvas; // X cordinate of canvas (top-left)
    int ylocCanvas; // Y cordinate of canvas (top-left)

    int x1; // Top-left x value of rectangle to be drawn
    int y1; // Top-left y value of rectangle to be drawn

    PERIMETER_LOCATION perimeterLocation = PERIMETER_LOCATION.NONE; // used
    // for resizing rectangles by dragging.

    /**
     * Constructor for the CanvasWindow class
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param screen_width width of the current screen
     * @param screen_height height of the current screen
     * @param canvas_width width of the CanvasWindow
     * @param canvas_height height of the CanvasWindow
     */
    public CanvasWindow(int screen_width, int screen_height, int canvas_width, int canvas_height) {
        // Instantiate dimensions of the GUI
        this.screenWidth = screen_width;
        this.screenHeight = screen_height;
        this.canvasWidth = canvas_width;
        this.canvasHeight = canvas_height;

        // Center the canvas on the screen
        xlocCanvas = ((screenWidth - canvasWidth) / 2);
        ylocCanvas = ((screenHeight - canvasHeight) / 2);

        // Initialize the root jFrame
        root = new JFrame("Prometheus");
        root.setSize(canvasWidth, canvasHeight);
        root.setLocation(xlocCanvas, ylocCanvas);
        root.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setFocusable(true);

        // Add a rectangle to the drawnRectangles to paint the canvas white and manage
        // it in the future.
        // Also used to deleteAllRectangles the canvas when repainting.
        Rectangle first = new Rectangle("jframe", 0, 0, canvasWidth, canvasHeight);
        first.setColor(Constants.CANVAS_DEFAULT_COLOR);
        first.setLabel("Prometheus");
        drawnRectangles.add(first);

        addMouseListener(new MouseAdapter() {

            // User clicks on canvas
            public void mousePressed(MouseEvent me) {
                // Get x and y cordinates of click and initiate rectangle being drawn
                x1 = me.getX();
                y1 = me.getY();
                if (highlightedRectangleIndex == 0) {
                    clearCordinates();
                }

                // Check if user clicked on a currently displayed rectangle
                int selectedRectangle = pointIsContainedByRect(me.getPoint());
                if (selectedRectangle != -1) {
                    // Check if a rectangle was already being edited
                    if (highlightedRectangleIndex != -1 && selectedRectangle != highlightedRectangleIndex) {
                        clearCordinates(); // Clear cordinates of last rectangle being drawn
                        JOptionPane.showMessageDialog(
                                CanvasWindow.this,
                                "Press 'Done' on information window before editing a new rectangle.",
                                "Done?",
                                JOptionPane.PLAIN_MESSAGE);
                    } else {
                        highlightedRectangleIndex = selectedRectangle; // Let the app know a new rectangle is being
                                                                       // edited
                        informationWindow.display(); // Display the rectangle's information on the information window
                        repaint(); // Repaint canvas
                        root.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }
                } else {
                    pruneStack(); // Make it impossible to redrawLastRemovedRectangle past undoes since it's a new
                                  // path
                    drawnRectangles.add(new Rectangle(componentsTray.getComp())); // Add new rectangle to list of
                                                                                  // rectangles to be displayed
                    stackPointer++; // Update stack pointer
                }
            }

            // User released mouse after clicking on canvas
            public void mouseReleased(MouseEvent me) {
                perimeterLocation = PERIMETER_LOCATION.NONE;
                int indexOfLastRectangle = drawnRectangles.size() - 1;
                // Get x and y cordinates of where mouse was released
                int x2 = me.getX();
                int y2 = me.getY();

                // Check that a rectangle is not currently being edited
                if (highlightedRectangleIndex == -1) {
                    // Check if mouse was released at the same point it was clicked
                    // i.e. a rectange of zero width and height was drawn

                    if (x1 == x2 && y1 == y2) {
                        // Remove zero-dimension rectangle from list.
                        drawnRectangles.remove(indexOfLastRectangle);
                        stackPointer--; // Update stack pointer

                        // Display jframe in information window
                        highlightedRectangleIndex = 0;
                        informationWindow.display();
                        repaint(); // Repaint canvas
                    }
                    // Check if current rectangle was dragged into another rectangle
                    else if (currentRectangleOverlapsWithOther()) {
                        JOptionPane.showMessageDialog(
                                CanvasWindow.this,
                                "You can't draw a component over another",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Create rectangle from users drag
                        Rectangle rect = new Rectangle(componentsTray.getComp(), x1, y1, x2, y2);
                        rect.correct(); // Adjust x1 and y1 (if necessary) to make sure width and height are positive

                        // Update list of rectangles to be drawn
                        // highlightedRectangleIndex = indexOfLastRectangle;
                        drawnRectangles.set(indexOfLastRectangle, rect);
                        informationWindow.tempDisplay(indexOfLastRectangle);
                        repaint(); // Repaint canvas
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {

            // User drags mouse while clicking on canvas
            public void mouseDragged(MouseEvent me) {
                // Get current x and y cordinates of the mouse
                int x2 = me.getX();
                int y2 = me.getY();

                // Confirm that user is not currently editing a rectangle
                if (highlightedRectangleIndex == -1) {
                    int indexOfLastRectangle = drawnRectangles.size() - 1;

                    // Create temp rectangle based on user's drag and update the list of rectangles
                    // to be drawn
                    drawnRectangles.set(indexOfLastRectangle, new Rectangle(componentsTray.getComp(), x1, y1, x2, y2));
                    informationWindow.tempDisplay(indexOfLastRectangle);
                    repaint(); // Repaint canvas
                } else {
                    // Check if highlighted rectangle was dragged
                    if ((x1 != x2 || y1 != y2) && (x1 != 0 && y1 != 0)) {
                        Rectangle highlightedRect = drawnRectangles.get(highlightedRectangleIndex);

                        int distX = (x2 - x1);
                        int distY = (y2 - y1);

                        // Calculate new position of rectangle
                        int newX = highlightedRect.getX();
                        int newY = highlightedRect.getY();
                        int newWidth = highlightedRect.getWidth();
                        int newHeight = highlightedRect.getHeight();
                        if (perimeterLocation == PERIMETER_LOCATION.NONE) {
                            newX += distX;
                            newY += distY;
                        } else if (perimeterLocation == PERIMETER_LOCATION.TOP) {
                            newY += distY;
                            newHeight -= distY;
                        } else if (perimeterLocation == PERIMETER_LOCATION.BOTTOM) {
                            newHeight += distY;
                        } else if (perimeterLocation == PERIMETER_LOCATION.RIGHT) {
                            newWidth += distX;
                        } else if (perimeterLocation == PERIMETER_LOCATION.LEFT) {
                            newX += distX;
                            newWidth -= distX;
                        } else if (perimeterLocation == PERIMETER_LOCATION.TOP_RIGHT) {
                            newY += distY;
                            newHeight -= distY;
                            newWidth += distX;
                        } else if (perimeterLocation == PERIMETER_LOCATION.BOTTOM_RIGHT) {
                            newHeight += distY;
                            newWidth += distX;
                        } else if (perimeterLocation == PERIMETER_LOCATION.BOTTOM_LEFT) {
                            newHeight += distY;
                            newX += distX;
                            newWidth -= distX;
                        } else if (perimeterLocation == PERIMETER_LOCATION.TOP_LEFT) {
                            newY += distY;
                            newHeight -= distY;
                            newX += distX;
                            newWidth -= distX;
                        }

                        // Move highlighted rectangle based on user user drag
                        // if the new location is within constraints

                        informationWindow.setCanvasAccess(false);

                        if (perimeterLocation == PERIMETER_LOCATION.NONE) {
                            if (newX > 0 &&
                                    (newX + highlightedRect.getWidth() < canvas_width)) {
                                highlightedRect.setX(newX);

                                // update current mouse location for future mouse drag events
                                x1 = x2;

                                // Reflect new position of rectangle in information window
                                informationWindow.componentXLoc.setValue(highlightedRect.getX());
                            }

                            if (newY > 0 &&
                                    (newY + highlightedRect.getHeight() < canvas_height)) {
                                highlightedRect.setY(newY);

                                // update current mouse location for future mouse drag events
                                y1 = y2;

                                // Reflect new position of rectangle in information window
                                informationWindow.componentYLoc.setValue(highlightedRect.getY());
                            }
                        } else {
                            highlightedRect.setX(newX);
                            highlightedRect.setY(newY);
                            highlightedRect.setWidth(newWidth);
                            highlightedRect.setHeight(newHeight);
                            highlightedRect.correct();

                            informationWindow.componentXLoc.setValue(highlightedRect.getX());
                            informationWindow.componentYLoc.setValue(highlightedRect.getY());
                            informationWindow.componentWidth.setValue(highlightedRect.getWidth());
                            informationWindow.componentHeight.setValue(highlightedRect.getHeight());

                            x1 = x2;
                            y1 = y2;
                        }

                        drawnRectangles.set(highlightedRectangleIndex, highlightedRect);
                        repaint();

                        informationWindow.setCanvasAccess(true);
                    }

                }
            }

            public void mouseMoved(MouseEvent me) throws NullPointerException {
                // User moves mouse without without clicking on canvas

                try {
                    root.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                    // Get x and y cordinates of the mouse and display at the bottom of the
                    // components tray window.
                    int x = (int) me.getX();
                    int y = (int) me.getY();
                    componentsTray.mousexy.setText(x + " , " + y);

                    // If a recatangle is not currently being edited and the mouse is hovering over
                    // a drawn rectangle
                    // Display its information in the information window without allowing user to
                    // edit
                    int hoveringRectangleIndex = pointIsContainedByRect(me.getPoint());
                    if (highlightedRectangleIndex == -1 && hoveringRectangleIndex != -1) {
                        informationWindow.tempDisplay(hoveringRectangleIndex);
                    } else if (highlightedRectangleIndex != -1 && hoveringRectangleIndex == highlightedRectangleIndex) {
                        Rectangle highlightedRect = drawnRectangles.get(highlightedRectangleIndex);
                        perimeterLocation = highlightedRect.pointIsOnPerimeter(me.getPoint());
                        if (perimeterLocation == PERIMETER_LOCATION.TOP) {
                            root.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
                        } else if (perimeterLocation == PERIMETER_LOCATION.BOTTOM) {
                            root.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
                        } else if (perimeterLocation == PERIMETER_LOCATION.RIGHT) {
                            root.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
                        } else if (perimeterLocation == PERIMETER_LOCATION.LEFT) {
                            root.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
                        } else if (perimeterLocation == PERIMETER_LOCATION.TOP_RIGHT) {
                            root.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
                        } else if (perimeterLocation == PERIMETER_LOCATION.BOTTOM_RIGHT) {
                            root.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
                        } else if (perimeterLocation == PERIMETER_LOCATION.BOTTOM_LEFT) {
                            root.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
                        } else if (perimeterLocation == PERIMETER_LOCATION.TOP_LEFT) {
                            root.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
                        } else {
                            root.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        }
                    }
                } catch (NullPointerException e) {
                }
            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                // Defined key listeners here so pressing and holding the key doesn't cause
                // problems

                // Backspace pressed
                if (ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    removeLastRectangle(); // Remove last drawn rectangle
                }
                // Space bar pressed
                if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
                    redrawLastRemovedRectangle(); // Re-draw last removed rectangle
                }
                // Delete button pressed
                if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
                    deleteAllRectangles(); // Delete all rectangles on screen
                }
                // Enter button pressed
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    // If currently editing a rectangle, mark as done
                    if (highlightedRectangleIndex != -1) {
                        informationWindow.okButton.doClick();
                        repaint();
                    }
                }
                // Letter G button pressed
                if (ke.getKeyCode() == KeyEvent.VK_G) {
                    // Toggle grid display
                    showGrid = !showGrid;
                    repaint();
                }
                // Letter H button pressed
                if (ke.getKeyCode() == KeyEvent.VK_H) {
                    visibility(); // Toggle visibility of tool windows.
                }
                // Letter L button pressed
                if (ke.getKeyCode() == KeyEvent.VK_L) {
                    // Load a database

                    JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                    jfc.setDialogTitle("Load an old project");
                    jfc.setAcceptAllFileFilterUsed(false);
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("SQLite File", "db");
                    jfc.addChoosableFileFilter(filter);

                    File workingDirectory = new File(System.getProperty("user.dir") + "\\Flames");
                    jfc.setCurrentDirectory(workingDirectory);

                    int returnValue = jfc.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        try {
                            String filePath = jfc.getSelectedFile().getPath();
                            // This method, gets the name of the database from the file path
                            SQLHandler handler = new SQLHandler(filePath.substring(0, filePath.lastIndexOf(".")));
                            ArrayList<Rectangle> rects = handler.getRectangles();
                            setSize(rects.get(0).getWidth(), rects.get(0).getHeight());
                            setLocation(rects.get(0).getX(), rects.get(0).getY());
                            setRectangles(rects);
                        } catch (ClassNotFoundException e1) {
                        } catch (SQLException e1) {
                        }
                    }
                }
            }
        });

        root.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent ce) {
                // Reflect changes from the canvas window being resized by user
                // into information window.

                canvasWidth = ce.getComponent().getWidth();
                canvasHeight = ce.getComponent().getHeight();

                Rectangle first = drawnRectangles.get(0);
                first.setWidth(canvasWidth);
                first.setHeight(canvasHeight);

                drawnRectangles.set(0, first);

                if (highlightedRectangleIndex == 0) {
                    informationWindow.setCanvasAccess(false);
                    informationWindow.componentWidth.setValue(canvasWidth);
                    informationWindow.componentHeight.setValue(canvasHeight);
                    informationWindow.setCanvasAccess(true);
                }
            }

            @Override
            public void componentMoved(ComponentEvent ce) {
                // Reflect changes from the canvas window being dragged by user
                // into information window.

                xlocCanvas = ce.getComponent().getX();
                ylocCanvas = ce.getComponent().getY();

                Rectangle first = drawnRectangles.get(0);
                first.setX(xlocCanvas);
                first.setY(ylocCanvas);

                drawnRectangles.set(0, first);

                if (highlightedRectangleIndex == 0) {
                    informationWindow.setCanvasAccess(false);
                    informationWindow.componentXLoc.setValue(xlocCanvas);
                    informationWindow.componentYLoc.setValue(ylocCanvas);
                    informationWindow.setCanvasAccess(true);
                }
            }

            @Override
            public void componentShown(ComponentEvent ce) {
            }

            @Override
            public void componentHidden(ComponentEvent ce) {
            }
        });

        root.addWindowListener(new WindowAdapter() {
            @Override
            // If the close button is pressed
            public void windowClosing(WindowEvent e) {
                try {
                    int result = JOptionPane.showConfirmDialog(
                            root,
                            "Save/Update Project?",
                            "Prometheus",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        new GuiWriter(
                                drawnRectangles, languageModel,
                                canvas_width, canvas_height,
                                screen_width, screen_height);
                        // write the gui code to a file
                        new SQLHandler(drawnRectangles); // save the rectangles in a database in case you want to
                        // access again
                        close(); // close all everything
                    } else if (result == JOptionPane.NO_OPTION) {
                        close(); // close eveything
                    }
                } catch (IOException ex) {
                } catch (ClassNotFoundException e1) {
                } catch (SQLException e1) {
                }
            }
        });

        // Add menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem(new AbstractAction("Save As") {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new GuiWriter(
                            drawnRectangles, languageModel,
                            canvas_width, canvas_height,
                            screen_width, screen_height);
                    // write the gui code to a file
                    new SQLHandler(drawnRectangles); // save the rectangles in a database in case you want to
                    // access again
                } catch (IOException | ClassNotFoundException | SQLException e1) {
                    JOptionPane.showMessageDialog(root, "Unable to save drawing");
                }
            }

        });
        JMenuItem loadItem = new JMenuItem(new AbstractAction("Load File") {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("Load an old project");
                jfc.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("SQLite File", "db");
                jfc.addChoosableFileFilter(filter);

                File workingDirectory = new File(System.getProperty("user.dir") + "\\Flames");
                jfc.setCurrentDirectory(workingDirectory);

                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        String filePath = jfc.getSelectedFile().getPath();
                        // This method, gets the name of the database from the file path
                        SQLHandler handler = new SQLHandler(filePath.substring(0, filePath.lastIndexOf(".")));
                        ArrayList<Rectangle> rects = handler.getRectangles();
                        setSize(rects.get(0).getWidth(), rects.get(0).getHeight());
                        setLocation(rects.get(0).getX(), rects.get(0).getY());
                        setRectangles(rects);
                    } catch (ClassNotFoundException | SQLException e1) {
                    }
                }
            }

        });
        menu.add(saveItem);
        menu.add(loadItem);
        menuBar.add(menu);
        root.setJMenuBar(menuBar);

        // Launch GUI and link all windows to root jframe
        root.add(CanvasWindow.this);
        root.setVisible(true);

        languageModel = new PythonModel();
        informationWindow = new InformationWindow(this, languageModel);
        componentsTray = new ComponentsWindow(languageModel);

    }

    @Override
    protected void paintComponent(Graphics g) {
        // Clear screen
        super.paintComponent(g);

        // Loop through rectangles to be drawn
        for (int i = 0; i < Math.min(stackPointer + 1, drawnRectangles.size()); i++) {
            Rectangle rect = drawnRectangles.get(i);
            int rectX = 0;
            int rectY = 0;
            if (i != 0) {
                rectX = rect.getX();
                rectY = rect.getY();
            }

            // Draw and fill rectangle based on specifications
            g.setColor(rect.getColor());
            g.fillRect(rectX, rectY, rect.getWidth(), rect.getHeight());

            // Draw rectangle label
            if (rect.getLabel() != "" && drawnRectangles.indexOf(rect) != 0) {
                int fontSize = Math.min(rect.getHeight(), rect.getWidth()) / 3;
                g.setFont(new Font(Constants.DEFAULT_FONT_NAME, Font.PLAIN, fontSize));

                // Center label in rectangle
                int canvasWidth = g.getFontMetrics().stringWidth(rect.getLabel());
                int margin = (rect.getWidth() - canvasWidth) / 2;

                // Draw label in rectangle
                g.setColor(drawnRectangles.get(0).getColor());
                g.drawString(rect.getLabel(), rect.getX() + margin,
                        rect.getY() + (rect.getHeight() / 2) + (fontSize / 2));
            }
        }

        // Check if a rectangle was selected
        if (highlightedRectangleIndex != -1) {
            Rectangle highlightedRect = drawnRectangles.get(highlightedRectangleIndex);
            int highlightedRectX = 0;
            int highlightedRectY = 0;
            if (highlightedRectangleIndex != 0) {
                highlightedRectX = highlightedRect.getX();
                highlightedRectY = highlightedRect.getY();
            }

            Graphics2D g2 = (Graphics2D) g; // This is the only one that can set a line's thickness
            Stroke straight = new BasicStroke(2); // A thicker line for drawing border around selected rectangle
            Stroke dashed = new BasicStroke(
                    1,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL,
                    0,
                    new float[] { 9 },
                    0); // A dashed line

            // Draw an outline around rectangle currently being edited to highlight it
            g2.setStroke(straight); // Make line thicker
            g2.setColor(Constants.RECTANGLE_HIGHLIGHT_DEFAULT_COLOR);
            g2.drawRect(
                    highlightedRectX,
                    highlightedRectY,
                    highlightedRect.getWidth(),
                    highlightedRect.getHeight());

            if (highlightedRectangleIndex != 0) {
                // Draw horizontal & vertical dashed line through center of highlighted
                // rectangle
                g2.setStroke(dashed);
                g2.drawLine(
                        0,
                        (highlightedRectY) + (highlightedRect.getHeight() / 2),
                        canvasWidth,
                        (highlightedRectY) + (highlightedRect.getHeight() / 2));
                g2.drawLine(
                        (highlightedRectX) + (highlightedRect.getWidth() / 2),
                        0,
                        (highlightedRectX) + (highlightedRect.getWidth() / 2),
                        canvasHeight);

                // Draw horizontal line parallel to top & bottom of highlighted rectangle
                g2.drawLine(
                        0,
                        (highlightedRectY),
                        canvasWidth,
                        (highlightedRectY));
                g2.drawLine(
                        0,
                        (highlightedRectY) + (highlightedRect.getHeight()),
                        canvasWidth,
                        (highlightedRectY) + (highlightedRect.getHeight()));

                // Draw vertical line parallel to left and right of highlighted rectangle
                g2.drawLine(
                        (highlightedRectX),
                        0,
                        (highlightedRectX),
                        canvasHeight);
                g2.drawLine(
                        (highlightedRectX) + (highlightedRect.getWidth()),
                        0,
                        (highlightedRectX) + (highlightedRect.getWidth()),
                        canvasHeight);
            }
        }

        // Check if the gridlines on the canvas are set to visible
        if (showGrid) {
            Graphics2D g2 = (Graphics2D) g;
            Stroke dashed = new BasicStroke(
                    1,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL,
                    0,
                    new float[] { 9 },
                    0);

            // Draw grid lines
            g2.setStroke(dashed); // Set stroke to dashed line
            g2.setColor(Constants.CANVAS_GRID_DEFAULT_COLOR);
            int ydivs = Constants.CANVAS_GRID_NVERTICAL_DIVISIONS; // No of vertical divisions in the grid
            for (int i = 1; i < ydivs; i++) {
                g2.drawLine(canvasWidth * i / ydivs, 0, canvasWidth * i / ydivs, canvasHeight);
            }
            int xdivs = Constants.CANVAS_GRID_NHORIZONTAL_DIVISIONS; // No of horizontal divisions in the showGrid
            for (int i = 1; i < xdivs; i++) {
                g2.drawLine(0, canvasHeight * i / xdivs, canvasWidth, canvasHeight * i / xdivs);
            }

            // Draw horizontal and vertical center lines
            g2.setColor(Constants.CANVAS_CENTERLINE_DEFAULT_COLOR);
            g2.drawLine(0, canvasHeight * 5 / 11, canvasWidth, canvasHeight * 5 / 11); // draw horizontal line through
                                                                                       // the center
            g2.drawLine(canvasWidth / 2, 0, canvasWidth / 2, canvasHeight); // draw vertical line through the center
        }
    }

    /**
     * Method to clear cordinates of rectangle being drawn
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    private void clearCordinates() {
        //
        x1 = 0;
        y1 = 0;
    }

    /**
     * Method to check if point is contained by any of the displayed rectangles
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    private int pointIsContainedByRect(Point p) {
        for (Rectangle rect : drawnRectangles) {
            if (rect.contains(p)
                    && drawnRectangles.indexOf(rect) != 0
                    && drawnRectangles.indexOf(rect) <= stackPointer) {
                return drawnRectangles.indexOf(rect);
            }
        }
        return -1;
    }

    /**
     * Method to check if drawn rectangle crossed another rectangle
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return true if the rectangle being drawn overlaps any of the other
     *         rectangles
     */
    public boolean currentRectangleOverlapsWithOther() {
        // TODO implement actual algorithm to test this
        // without affecting app performance
        return false;
    }

    /**
     * Method to remove most recent rectangle on current path from display
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void removeLastRectangle() {
        // Confirm that a rectangle is not currently selected
        if (highlightedRectangleIndex == -1) {
            stackPointer = Math.max(stackPointer - 1, 0);
            repaint();
        }
    }

    /**
     * Method to draw recently undone rectangle on current path (if any)
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void redrawLastRemovedRectangle() {
        // Confirm that a rectangle is not currently selected
        if (highlightedRectangleIndex == -1) {
            stackPointer = Math.min(stackPointer + 1, drawnRectangles.size() - 1);
            repaint();
        }
    }

    /**
     * Method to remove all rectangles on current path from display
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void deleteAllRectangles() {
        // Confirm that a rectangle is not currently selected
        if (highlightedRectangleIndex == -1) {
            Rectangle first = drawnRectangles.get(0);
            drawnRectangles.clear();
            drawnRectangles.add(first);
            stackPointer = 0;
            repaint();
        }
    }

    /**
     * Method delete all rectangles with indices greater than stackPointer
     * 
     * This deletes all rectangles not currently being displayed
     * i.e. no more re-doing
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void pruneStack() {
        while (drawnRectangles.size() > stackPointer + 1) {
            drawnRectangles.remove(drawnRectangles.size() - 1);
        }
    }

    /**
     * Setter method for drawnRectangles
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param drawnRectangles rectangles to be drawn on canvas
     */
    public void setRectangles(ArrayList<Rectangle> drawnRectangles) {
        this.drawnRectangles = drawnRectangles;
        stackPointer = this.drawnRectangles.size() - 1;
        repaint();
    }

    /**
     * Method to close all windows
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void close() {
        componentsTray.close();
        informationWindow.close();
        root.dispose();
    }

    /**
     * Method to toggle visibility of tool windows.
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void visibility() {
        componentsTray.visibility();
        informationWindow.visibility();
    }

    /**
     * Method to make tool windows visible
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void makeVisible() {
        informationWindow.makeVisible();
        componentsTray.makeVisible();
    }
}
