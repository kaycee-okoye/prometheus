package prometheus.Views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import prometheus.Constants.Constants;
import prometheus.Models.LanguageModel;
import prometheus.Models.Rectangle;

/**
 * Class to create a moveable floating tray that observe/edit data of a
 * highlighted rectangle in the CanvasWindow
 * 
 * @author Kenechukwu Okoye
 * @version 2023.11.15
 */
public class InformationWindow extends JWindow {
    private CanvasWindow creation;
    private LanguageModel languageModel;

    private int screenWidth;
    private int screenHeight;
    private int windowWidth;
    private int windowHeight;
    private int xloc;
    private int yloc;
    private boolean giveAccessToCanvas = true;
    int X = 0;
    int Y = 0;
    int enablestep = 0;
    DefaultComboBoxModel<String> componentSelectorModel;
    JComboBox<String> componentSelector;
    JTextField componentLabel;
    JSpinner componentColorR;
    JSpinner componentColorG;
    JSpinner componentColorB;
    JSpinner componentColorA;
    JSpinner componentXLoc;
    JSpinner componentYLoc;
    JSpinner componentWidth;
    JSpinner componentHeight;
    SpinnerNumberModel componentX1;
    SpinnerNumberModel componentY1;
    SpinnerNumberModel componentX2;
    SpinnerNumberModel componentY2;
    JButton okButton;

    /**
     * Constructor for InformationWindow class
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param creation      floating canvas window where rectangles are drawn
     * @param languageModel code templates for a specific programming language
     */
    public InformationWindow(CanvasWindow creation, LanguageModel languageModel) {
        // Initialize window based activateAllComponents screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenHeight = (int) Math.floor(screenSize.getHeight());
        screenWidth = (int) Math.floor(screenSize.getWidth());
        windowWidth = screenWidth / 4;
        windowHeight = screenHeight / 4;
        xloc = (screenWidth * 3) / 4;
        yloc = screenHeight / 4;

        // Create panel with thicker border for user to drag the window
        JPanel stem = new JPanel();
        stem.setLayout(new GridBagLayout());
        setSize(windowWidth, windowHeight);
        stem.setBorder(
                BorderFactory.createEmptyBorder(
                        3, 3, 3, 3));
        setLocation(xloc, yloc);

        GridBagConstraints constraints = new GridBagConstraints();
        JLabel componentSelector_ = new JLabel("Component: ");
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        stem.add(componentSelector_, constraints);

        componentSelectorModel = new DefaultComboBoxModel<String>(
                languageModel.getComponentNames(true));
        componentSelector = new JComboBox<String>(componentSelectorModel);
        componentSelector.setSelectedIndex(Constants.DEFAULT_COMPONENT_INDEX);
        componentSelector.setMaximumRowCount(7);
        constraints.weightx = 1;
        constraints.weighty = 0.5;
        constraints.gridwidth = 2;
        constraints.gridx = 1;
        constraints.gridy = 0;
        stem.add(componentSelector, constraints);

        JLabel componentLabel_ = new JLabel("Label: ");
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 1;
        stem.add(componentLabel_, constraints);

        componentLabel = new JTextField();
        constraints.weightx = 1;
        constraints.weighty = 0.5;
        constraints.gridwidth = 2;
        constraints.gridx = 1;
        constraints.gridy = 1;
        stem.add(componentLabel, constraints);

        JLabel componentColorR_ = new JLabel("Red: ");
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 2;
        stem.add(componentColorR_, constraints);

        componentColorR = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        componentColorR.setEditor(new JSpinner.DefaultEditor(componentColorR));
        constraints.weightx = 1;
        constraints.weighty = 0.5;
        constraints.gridwidth = 2;
        constraints.gridx = 1;
        constraints.gridy = 2;
        stem.add(componentColorR, constraints);

        JLabel componentColorG_ = new JLabel("Green: ");
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 3;
        stem.add(componentColorG_, constraints);

        componentColorG = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        componentColorG.setEditor(new JSpinner.DefaultEditor(componentColorG));
        constraints.weightx = 1;
        constraints.weighty = 0.5;
        constraints.gridwidth = 2;
        constraints.gridx = 1;
        constraints.gridy = 3;
        stem.add(componentColorG, constraints);

        JLabel componentColorB_ = new JLabel("Blue: ");
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 4;
        stem.add(componentColorB_, constraints);

        componentColorB = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        componentColorB.setEditor(new JSpinner.DefaultEditor(componentColorB));
        constraints.weightx = 1;
        constraints.weighty = 0.5;
        constraints.gridwidth = 2;
        constraints.gridx = 1;
        constraints.gridy = 4;
        stem.add(componentColorB, constraints);

        JLabel componentColorA_ = new JLabel("Alpha: ");
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 5;
        stem.add(componentColorA_, constraints);

        componentColorA = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        componentColorA.setEditor(new JSpinner.DefaultEditor(componentColorA));
        constraints.weightx = 1;
        constraints.weighty = 0.5;
        constraints.gridwidth = 2;
        constraints.gridx = 1;
        constraints.gridy = 5;
        stem.add(componentColorA, constraints);

        JLabel componentXLoc_ = new JLabel("x: ");
        constraints.gridx = 0;
        constraints.gridy = 6;
        stem.add(componentXLoc_, constraints);

        componentX1 = new SpinnerNumberModel(0, 0, creation.root.getWidth(), 1);
        componentXLoc = new JSpinner(componentX1);
        componentXLoc.setEditor(new JSpinner.DefaultEditor(componentXLoc));
        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.weightx = 1;
        constraints.weighty = 0.5;
        constraints.gridwidth = 2;
        stem.add(componentXLoc, constraints);

        JLabel componentYLoc_ = new JLabel("y: ");
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 7;
        stem.add(componentYLoc_, constraints);

        componentY1 = new SpinnerNumberModel(0, 0, creation.root.getHeight(), 1);
        componentYLoc = new JSpinner(componentY1);
        componentYLoc.setEditor(new JSpinner.DefaultEditor(componentYLoc));
        constraints.gridwidth = 7;
        constraints.gridx = 1;
        constraints.gridy = 7;
        constraints.weightx = 1;
        constraints.weighty = 0.5;
        constraints.gridwidth = 2;
        stem.add(componentYLoc, constraints);

        JLabel componentWidth_ = new JLabel("Width: ");
        constraints.gridx = 0;
        constraints.gridy = 8;
        stem.add(componentWidth_, constraints);

        componentX2 = new SpinnerNumberModel(0, 0, creation.root.getWidth(), 1);
        componentWidth = new JSpinner(componentX2);
        componentWidth.setEditor(new JSpinner.DefaultEditor(componentWidth));
        constraints.gridx = 1;
        constraints.gridy = 8;
        constraints.weightx = 1;
        constraints.weighty = 0.5;
        constraints.gridwidth = 2;
        stem.add(componentWidth, constraints);

        JLabel componentHeight_ = new JLabel("Height: ");
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 9;
        stem.add(componentHeight_, constraints);

        componentY2 = new SpinnerNumberModel(0, 0, creation.root.getHeight(), 1);
        componentHeight = new JSpinner(componentY2);
        componentHeight.setEditor(new JSpinner.DefaultEditor(componentHeight));
        constraints.gridx = 1;
        constraints.gridy = 9;
        constraints.weightx = 1;
        constraints.weighty = 0.5;
        constraints.gridwidth = 2;
        stem.add(componentHeight, constraints);

        okButton = new JButton("Done");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = 8;
        constraints.gridx = 0;
        constraints.gridy = 10;
        stem.add(okButton, constraints);

        // Make window draggable
        addListeners();
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                X = me.getX();
                Y = me.getY();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent me) {
                Point p = getLocation();
                setLocation(p.x + (me.getX() - X), p.y + (me.getY() - Y));
            }
        });

        add(stem);
        setVisible(true);
        deactivateAllComponents();

        this.creation = creation;
        this.languageModel = languageModel;
    }

    /**
     * Method to gather all updates made in the InformationWindow
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return values of all components in the InformationWindow
     */
    public ArrayList<String> collect_updates() {
        ArrayList<String> updates = new ArrayList<String>();
        updates.add(componentSelector.getSelectedItem().toString());
        updates.add(componentLabel.getText());
        updates.add(componentColorR.getValue().toString());
        updates.add(componentColorG.getValue().toString());
        updates.add(componentColorB.getValue().toString());
        updates.add(componentColorA.getValue().toString());
        updates.add(componentXLoc.getValue().toString());
        updates.add(componentYLoc.getValue().toString());
        updates.add(componentWidth.getValue().toString());
        updates.add(componentHeight.getValue().toString());
        return updates;
    }

    /**
     * Method to display currently highlighted rectangle's in the InformationWindow
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void display() {
        Rectangle current = creation.drawnRectangles.get(creation.highlightedRectangleIndex);
        componentLabel.setText(current.getLabel());
        componentColorR.setValue(current.getColor().getRed());
        componentColorG.setValue(current.getColor().getGreen());
        componentColorB.setValue(current.getColor().getBlue());
        componentColorA.setValue(current.getColor().getAlpha());
        componentXLoc.setValue(current.getX());
        componentYLoc.setValue(current.getY());
        componentWidth.setValue(current.getWidth());
        componentHeight.setValue(current.getHeight());

        if (creation.highlightedRectangleIndex != -1) {
            String rootComponentName = languageModel.getRootComponent().getName();
            if (creation.highlightedRectangleIndex == 0) {
                // If highlighted rectangle is the root component,
                // make this component available in the selector and select that
                if (componentSelectorModel.getIndexOf(rootComponentName) == -1) {
                    componentSelector.addItem(rootComponentName);
                }
                componentSelector.setSelectedItem(rootComponentName);

                // Prevent jframe from growing larger than the screen
                componentX1.setMaximum(screenWidth);
                componentXLoc.setValue(creation.root.getLocation().x);
                componentY1.setMaximum(screenHeight);
                componentYLoc.setValue(creation.root.getLocation().y);

                componentX2.setMaximum(screenWidth - creation.root.getLocation().x);
                componentWidth.setValue(creation.root.getWidth());
                componentY2.setMaximum(screenHeight - creation.root.getLocation().y);
                componentHeight.setValue(creation.root.getHeight());

                activateAllComponents(); // Turn on components in information window and start checking for state
                                         // changes
                componentSelector.setEnabled(false); // Show that it's a jframe but don't allow the user change it to
                                                     // something else
            } else {
                if (componentSelectorModel.getIndexOf(rootComponentName) != -1) {
                    componentSelector.removeItem(rootComponentName); // So the user doesn't make another root component
                }
                componentSelector.setSelectedItem(current.getComponent());

                // Display current values and set bounds based on components
                componentX1.setMaximum(creation.root.getWidth());
                componentY1.setMaximum(creation.root.getHeight());

                componentX2.setMaximum(creation.root.getWidth() - current.getX());
                componentY2.setMaximum(creation.root.getHeight() - current.getY());
                activateAllComponents(); // Turn on components in information window and start checking for state
                                         // changes
            }
        }
    }

    /**
     * Method to reflect changes made by editing information window on the
     * CanvasWindow
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void updateScreen() {
        if (giveAccessToCanvas) {
            ArrayList<String> updates = collect_updates();
            Rectangle current = creation.drawnRectangles.get(creation.highlightedRectangleIndex);
            current.setComponent(updates.get(0));
            current.setLabel(updates.get(1));
            current.setColor(
                    new Color(
                            Integer.parseInt(updates.get(2)),
                            Integer.parseInt(updates.get(3)),
                            Integer.parseInt(updates.get(4)),
                            Integer.parseInt(updates.get(5))));
            current.setX(Integer.parseInt(updates.get(6)));
            current.setY(Integer.parseInt(updates.get(7)));
            current.setWidth(Integer.parseInt(updates.get(8)));
            current.setHeight(Integer.parseInt(updates.get(9)));

            // If jframe is edited, reflect that
            if (creation.highlightedRectangleIndex == 0) {
                creation.canvasWidth = current.getWidth();
                creation.canvasWidth = current.getHeight();
                creation.root.setTitle(current.getLabel());
                creation.root.setSize(current.getWidth(), current.getHeight());
                creation.root.setLocation(current.getX(), current.getY());
                creation.xlocCanvas = current.getX();
                creation.ylocCanvas = current.getY();
                current.setX(0);
                current.setY(0);
            }
            creation.drawnRectangles.set(creation.highlightedRectangleIndex, current);
            creation.root.repaint();
        }
    }

    /**
     * Method to enforce bounds in the InformationWindow
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void updateConstraints() {
        Rectangle current = creation.drawnRectangles.get(creation.highlightedRectangleIndex);
        if (creation.highlightedRectangleIndex == 0) { // jframe
            componentX2.setMaximum(screenWidth);
            componentY2.setMaximum(screenHeight);
        } else {
            // Don't allow the component to grow larger than the canvas
            componentX1.setMaximum(creation.canvasWidth);
            if (current.getX() > creation.canvasWidth) {
                componentXLoc.setValue(creation.canvasWidth);
                current.setX(creation.canvasWidth);
            }
            componentY1.setMaximum(creation.canvasHeight);
            if (current.getY() > creation.canvasHeight) {
                componentYLoc.setValue(creation.canvasHeight);
                current.setY(creation.canvasHeight);
            }
            componentX2.setMaximum(creation.canvasWidth - current.getX());
            if (current.getWidth() > (creation.canvasWidth - current.getX())) {
                componentWidth.setValue(creation.canvasWidth - current.getX());
                current.setWidth(creation.canvasWidth - current.getX());
            }
            componentY2.setMaximum(creation.canvasHeight - current.getY());
            if (current.getHeight() > (creation.canvasHeight - current.getY())) {
                componentHeight.setValue(creation.canvasHeight - current.getY());
                current.setHeight(creation.canvasHeight - current.getY());
            }
        }
        creation.drawnRectangles.set(creation.highlightedRectangleIndex, current); // Apply change
        creation.root.repaint();
    }

    /**
     * Method to activate all the components in the InformationWindow
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void activateAllComponents() {
        enablestep = 1;
        componentSelector.setEnabled(true);
        componentLabel.setEditable(true);
        componentLabel.setFocusable(true);
        componentLabel.setCaretPosition(0);
        componentColorR.setEnabled(true);
        componentColorG.setEnabled(true);
        componentColorB.setEnabled(true);
        componentColorA.setEnabled(true);
        componentXLoc.setEnabled(true);
        componentYLoc.setEnabled(true);
        componentWidth.setEnabled(true);
        componentHeight.setEnabled(true);
        componentSelector.requestFocus();
    }

    /**
     * Method to deactivate all the components in the InformationWindow
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void deactivateAllComponents() {
        enablestep = 0;
        componentSelector.setEnabled(false);
        componentLabel.setEditable(false);
        componentColorR.setEnabled(false);
        componentColorG.setEnabled(false);
        componentColorB.setEnabled(false);
        componentColorA.setEnabled(false);
        componentXLoc.setEnabled(false);
        componentYLoc.setEnabled(false);
        componentWidth.setEnabled(false);
        componentHeight.setEnabled(false);
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

    /**
     * Method to add all appropriate listeners to the components of the
     * InformationWindow
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void addListeners() {
        componentLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (creation.highlightedRectangleIndex != -1) {
                    Object inputText = JOptionPane.showInputDialog(
                            creation.root,
                            "Enter Text:",
                            creation.drawnRectangles.get(creation.highlightedRectangleIndex).getLabel());
                    if (inputText != null && inputText.toString() != null) {
                        componentLabel.setText(inputText.toString());
                    }
                    updateScreen();
                }
                creation.makeVisible();
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (creation.highlightedRectangleIndex != -1) {
                    // Only make changes when there's something currently being edited (something
                    // that should accept the changes)
                    updateScreen(); // Collect all the values in the information window and update the currently
                                    // editted rectangle to reflect those changes
                    creation.highlightedRectangleIndex = -1; // Stop editing rectangle
                    deactivateAllComponents(); // Disable all the components of information window
                    // In this order or app will have issues because states of components will
                    // change and trigger an update
                }
            }
        });

        componentXLoc.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (enablestep != 0) {
                    // Check if state changed after initial setup
                    updateConstraints();
                    updateScreen();
                    updateConstraints();
                    // In this order or app will have issues because states of components will
                    // change and trigger an update
                    // and also so double-clicking is not required
                }
            }
        });

        componentYLoc.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Same as previous change listener
                if (enablestep != 0) {
                    updateConstraints();
                    updateScreen();
                    updateConstraints();
                }
            }
        });

        componentWidth.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Same as previous change listener
                if (enablestep != 0) {
                    updateConstraints();
                    updateScreen();
                    updateConstraints();
                }
            }
        });

        componentHeight.addChangeListener(new ChangeListener() {
            // Same as previous change listener
            @Override
            public void stateChanged(ChangeEvent e) {
                if (enablestep != 0) {
                    updateConstraints();
                    updateScreen();
                    updateConstraints();
                }
            }
        });

        componentColorR.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (enablestep != 0) {
                    // Check if state changed after initial setup
                    updateScreen();
                }
            }
        });

        componentColorG.addChangeListener(new ChangeListener() {
            // Same as previous change listener
            @Override
            public void stateChanged(ChangeEvent e) {
                if (enablestep != 0) {
                    updateScreen();
                }
            }
        });

        componentColorB.addChangeListener(new ChangeListener() {
            // Same as previous change listener
            @Override
            public void stateChanged(ChangeEvent e) {
                if (enablestep != 0) {
                    updateScreen();
                }
            }
        });

        componentColorA.addChangeListener(new ChangeListener() {
            // Same as previous change listener
            @Override
            public void stateChanged(ChangeEvent e) {
                if (enablestep != 0) {
                    updateScreen();
                }
            }
        });
    }

    /**
     * Method to peak at properties of a rectangle without editing
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void tempDisplay(int highlightedRectangleIndex) {
        if (creation.highlightedRectangleIndex == -1) {
            creation.highlightedRectangleIndex = highlightedRectangleIndex;
            display();
            deactivateAllComponents();
            creation.highlightedRectangleIndex = -1;
        }
    }

    /**
     * Method to set whether changes to the information window should affect the
     * canvas
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     */
    public void setCanvasAccess(boolean giveAccessToCanvas) {
        this.giveAccessToCanvas = giveAccessToCanvas;
    }
}
