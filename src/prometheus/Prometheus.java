package prometheus;

import java.awt.Dimension;
import java.awt.Toolkit;

import prometheus.Constants.Constants;
import prometheus.Views.CanvasWindow;

public class Prometheus {
    public static void main(String[] args) {
        // Entry point into application. Launches the user-interface

        // Get screen dimensions of current device
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final double screenWidth = screenSize.getWidth();
        final double screenHeight = screenSize.getHeight();
        final double defaultWindowLength = Constants.WINDOW_DEFAULT_REL_LENGTH * Math.min(screenWidth, screenHeight);

        // Launch the GUI builder using default dimensions
        new CanvasWindow(
                (int) screenWidth, (int) screenHeight,
                (int) defaultWindowLength, (int) defaultWindowLength);
    }
}
