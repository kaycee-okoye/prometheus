package prometheus.Controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import prometheus.Models.Component;
import prometheus.Models.LanguageModel;
import prometheus.Models.Rectangle;

/**
 * Class to convert an array list of rectangles to source code
 * 
 * @author Kenechukwu Okoye
 * @version 2023.11.15
 */
public class GuiWriter {

    private BufferedWriter writer;
    private FileOutputStream fos;
    private File file; // File to write source code
    private String projectName = "Prometheus";

    private ArrayList<Rectangle> rects; // List of drawn rectangles
    private LanguageModel languageModel;

    private int canvasWidth;
    private int canvasHeight;
    private int screenWidth;
    private int screenHeight;

    /**
     * Constructor for GuiWriter class
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param rects         information about components being converted to source
     *                      code
     * @param languageModel code templates used to generate source code
     * @param canvasWidth   width of the CanvasWindow used for design
     * @param canvasHeight  height of the CanvasWindow used for design
     * @param screenWidth   width of the computer screen used for design
     * @param screenHeight  height of the computer screen used for design
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public GuiWriter(
            ArrayList<Rectangle> rects,
            LanguageModel languageModel,
            int canvasWidth,
            int canvasHeight,
            int screenWidth,
            int screenHeight) throws FileNotFoundException, IOException {

        this.rects = rects;
        this.languageModel = languageModel;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        // Generate path to directory to write source code
        String delimeter = "\\";
        String filePath = new File("").getAbsolutePath();
        if (rects.get(0).getLabel().length() > 0) {
            projectName = rects.get(0).getLabel();
        }
        String dir = filePath + delimeter + "Flames" + delimeter + projectName + delimeter;

        // Create directory
        File directory = new File(dir);
        directory.mkdirs();
        file = new File(dir + languageModel.getName() + "GUI.txt");

        writeSourceCode();
    }

    /**
     * Method to generate appropriate source code and write to file
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void writeSourceCode() throws FileNotFoundException, IOException {
        fos = new FileOutputStream(file, false);
        writer = new BufferedWriter(new OutputStreamWriter(fos));

        int varIndex = 0; // Component variable names will be comp1, comp2 etc
        String topComponentVariableName = "stem";

        // write code preamble to file
        if (languageModel.getPrefix().isEmpty() == false) {
            writer.write(
                    refactorSourceCode(
                            languageModel.getPrefix(),
                            topComponentVariableName,
                            topComponentVariableName));
            writer.flush();
            writer.newLine();
        }

        // write code from user's design to file
        for (Rectangle rect : rects) {
            String componentVariableName = "comp" + varIndex;
            String componentName = rect.getComponent();
            String code = "";

            for (Component component : languageModel.getComponents()) {
                if (component.getName() == componentName) {
                    code = component.generateCode(
                            rect, canvasWidth, canvasHeight,
                            screenWidth, screenHeight);
                    break;
                }
            }
            writer.write(refactorSourceCode(code, topComponentVariableName, componentVariableName));
            writer.flush();
            writer.newLine();
            varIndex++;
        }

        // write closing statements to file
        if (languageModel.getSuffix().isEmpty() == false) {
            writer.write(
                    refactorSourceCode(languageModel.getSuffix(), topComponentVariableName, topComponentVariableName));
            writer.flush();
            writer.newLine();
        }

        writer.close();
        fos.close();
    }

    /**
     * Method to apply values to top-level placeholder in code template
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param code                     the current code text
     * @param topComponentVariableName variable name of the topmost parent component
     *                                 in the design
     * @param componentVariableName    variable name of the current component in the
     *                                 design
     */
    private String refactorSourceCode(
            String code, String topComponentVariableName,
            String componentVariableName) {
        return code
                .replaceAll("VARIABLE_NAME", componentVariableName)
                .replaceAll("PROJECT_NAME", projectName)
                .replaceAll("TOP_CONTAINER_NAME", topComponentVariableName);

    }
}
