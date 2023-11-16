package prometheus.Models;

import java.awt.Color;
import java.awt.Point;

import prometheus.Constants.Constants;

/**
 * Class to store information about rectangles drawn in CanvasWindow
 * 
 * @author Kenechukwu Okoye
 * @version 2023.11.15
 */
public class Rectangle {
    private int x;
    private int y;
    private int width;
    private int height;
    private Color color = Constants.RECTANGLE_DEFAULT_COLOR;
    private String label = "";
    private String component;

    /**
     * Constructor for Rectangle class
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param component ComponentsWindow name of the component
     */
    public Rectangle(String component) {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.component = component;
    }

    /**
     * Constructor for Rectangle class
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param component ComponentsWindow name of the component
     * @param x         x-cordinate of top-left corner of rectangle in CanvasWindow
     * @param y         y-cordinate of top-left corner of rectangle in CanvasWindow
     * @param x2        x-cordinate of bottom-right corner of rectangle in
     *                  CanvasWindow
     * @param y2        y-cordinate of bottom-right corner of rectangle in
     *                  CanvasWindow
     */
    public Rectangle(String component, int x, int y, int x2, int y2) {
        this.x = x;
        this.y = y;
        this.width = x2 - x;
        this.height = y2 - y;
        this.component = component;
    }

    /**
     * Method to clear cordinates and dimensions of the rectangle
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     */
    public void clear() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    /**
     * Method to check if the cordinates and dimensions of the rectangle have not
     * been set
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return true if values are default values
     * 
     */
    public Boolean isempty() {
        return (this.x == 0 && this.y == 0 &&
                this.width == 0 && this.height == 0);
    }

    /**
     * Method to check if a point is inside the rectangle
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param p cordinates of the point being checked
     * 
     * @return true if point is inside the rectangle
     * 
     */
    public Boolean contains(Point p) {
        if (this.x <= p.getX() &&
                this.y <= p.getY() &&
                (this.x + this.width) >= p.getX() &&
                (this.y + this.height) >= p.getY()) {
            return true;
        }
        return false;
    }

    /**
     * Method to adjust cordinated of rectangle to ensure
     * positive width and height
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     */
    public void correct() {
        if (this.width < 0) {
            this.x -= Math.abs(this.width);
            this.width = Math.abs(this.width);
        }
        if (this.height < 0) {
            this.y -= Math.abs(this.height);
            this.height = Math.abs(this.height);
        }
    }

    /**
     * Getter method for x
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return x-cordinate of top-left corner of rectangle in CanvasWindow
     * 
     */
    public int getX() {
        return x;
    }

    /**
     * Setter method for x
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param x x-cordinate of top-left corner of rectangle in CanvasWindow
     * 
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Getter method for y
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return y-cordinate of top-left corner of rectangle in CanvasWindow
     * 
     */
    public int getY() {
        return y;
    }

    /**
     * Setter method for y
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param y y-cordinate of top-left corner of rectangle in CanvasWindow
     * 
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Getter method for width
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return width of the rectangle in CanvasWindow
     * 
     */
    public int getWidth() {
        return width;
    }

    /**
     * Setter method for width
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param width width of the rectangle in CanvasWindow
     * 
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Getter method for height
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return height of the rectangle in CanvasWindow
     * 
     */
    public int getHeight() {
        return height;
    }

    /**
     * Setter method for height
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param height height of the rectangle in CanvasWindow
     * 
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Getter method for color
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return color of the rectangle in CanvasWindow
     * 
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter method for color
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param color color of the rectangle in CanvasWindow
     * 
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Getter method for label
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return label of the rectangle in CanvasWindow
     * 
     */
    public String getLabel() {
        return label;
    }

    /**
     * Setter method for label
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param label label of the rectangle in CanvasWindow
     * 
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter method for component
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return ComponentsWindow name of the component
     * 
     */
    public String getComponent() {
        return component;
    }

    /**
     * Setter method for component
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param component ComponentsWindow name of the component
     * 
     */
    public void setComponent(String component) {
        this.component = component;
    }

    /**
     * Getter method for x2
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return x-cordinate of bottom-right corner of rectangle in CanvasWindow
     * 
     */
    public int getX2() {
        return this.x + this.width;
    }

    /**
     * Getter method for y2
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return y-cordinate of bottom-right corner of rectangle in CanvasWindow
     * 
     */
    public int getY2() {
        return this.y + this.height;
    }

    /**
     * Function to check if a point lies with a threshold of the perimeter of the
     * rectangle.
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param p cordinates of the point being checked
     * @return the side or vertice on the rectangle's perimeter where the point lies
     *         is returned.
     */
    public PERIMETER_LOCATION pointIsOnPerimeter(Point p) {
        PERIMETER_LOCATION perimeterLocation = PERIMETER_LOCATION.NONE;
        if ((p.getX() > x - Constants.RECTANGLE_PERIMETER_PX_THRESHOLD) &&
                (p.getX() < x + Constants.RECTANGLE_PERIMETER_PX_THRESHOLD)) {
            perimeterLocation = PERIMETER_LOCATION.LEFT;
        } else if ((p.getX() > x + width - Constants.RECTANGLE_PERIMETER_PX_THRESHOLD) &&
                (p.getX() < x + width + Constants.RECTANGLE_PERIMETER_PX_THRESHOLD)) {
            perimeterLocation = PERIMETER_LOCATION.RIGHT;
        }

        if ((p.getY() > y - Constants.RECTANGLE_PERIMETER_PX_THRESHOLD) &&
                (p.getY() < y + Constants.RECTANGLE_PERIMETER_PX_THRESHOLD)) {
            if (perimeterLocation == PERIMETER_LOCATION.LEFT) {
                perimeterLocation = PERIMETER_LOCATION.TOP_LEFT;
            } else if (perimeterLocation == PERIMETER_LOCATION.RIGHT) {
                perimeterLocation = PERIMETER_LOCATION.TOP_RIGHT;
            } else {
                perimeterLocation = PERIMETER_LOCATION.TOP;
            }
        } else if ((p.getY() > y + height - Constants.RECTANGLE_PERIMETER_PX_THRESHOLD) &&
                (p.getY() < y + height + Constants.RECTANGLE_PERIMETER_PX_THRESHOLD)) {
            if (perimeterLocation == PERIMETER_LOCATION.LEFT) {
                perimeterLocation = PERIMETER_LOCATION.BOTTOM_LEFT;
            } else if (perimeterLocation == PERIMETER_LOCATION.RIGHT) {
                perimeterLocation = PERIMETER_LOCATION.BOTTOM_RIGHT;
            } else {
                perimeterLocation = PERIMETER_LOCATION.BOTTOM;
            }
        }

        return perimeterLocation;
    }

    /**
     * Enum describing sides and corners of the rectangle
     */
    public enum PERIMETER_LOCATION {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT,

        TOP_RIGHT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT,
        TOP_LEFT,

        NONE
    }

}