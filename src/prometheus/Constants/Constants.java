package prometheus.Constants;

import java.awt.Color;

/**
 * Class containing UI constants used throughout the project
 * 
 * @author  Kenechukwu Okoye
 * @version 2023.11.15
 */
public class Constants {
    public final static double WINDOW_DEFAULT_REL_LENGTH = 0.9;

    public final static Color CANVAS_DEFAULT_COLOR = new Color(255, 255, 255, 255);
    public final static Color CANVAS_GRID_DEFAULT_COLOR = Color.GREEN;
    public final static Color CANVAS_CENTERLINE_DEFAULT_COLOR = Color.BLUE;
    public final static Color RECTANGLE_DEFAULT_COLOR = new Color(56, 182, 255, 255);
    public final static Color RECTANGLE_HIGHLIGHT_DEFAULT_COLOR = new Color(255, 0, 0, 255);

    public final static int CANVAS_GRID_NVERTICAL_DIVISIONS = 8;
    public final static int CANVAS_GRID_NHORIZONTAL_DIVISIONS = 11;

    public final static String DEFAULT_FONT_NAME = "TimesRoman";
    public final static int DEFAULT_COMPONENT_INDEX = 0;

    public final static int RECTANGLE_PERIMETER_PX_THRESHOLD = 5;
}
