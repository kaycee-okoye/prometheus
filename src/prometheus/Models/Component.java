package prometheus.Models;

import java.awt.Color;

import prometheus.Constants.CodeTemplatePlaceholders;

/**
 * Class to convert rectangle to source code
 * 
 * This is used in LanguageModel objects to convert designs to source code
 * 
 * @author Kenechukwu Okoye
 * @version 2023.11.15
 */
public class Component {
    private String name;
    private String nameInLanguage;
    private String codeTemplate;
    private boolean isRootComponent;

    /**
     * Constructor for Component class
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param name           name of the component in ComponentsWindow
     * @param nameInLanguage corresponding name of the component in the programming
     *                       language
     */
    public Component(String name, String nameInLanguage) {
        this.name = name;
        this.nameInLanguage = nameInLanguage;
        this.codeTemplate = "";
        this.isRootComponent = false;
    }

    /**
     * Constructor for Component class
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param name           name of the component in ComponentsWindow
     * @param nameInLanguage corresponding name of the component in the programming
     *                       language
     * @param codeTemplate   code template used to convert rectangle to source code
     */
    public Component(String name, String nameInLanguage, String codeTemplate) {
        this.name = name;
        this.nameInLanguage = nameInLanguage;
        this.codeTemplate = codeTemplate;
        this.isRootComponent = false;
    }

    /**
     * Constructor for Component class
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param name            name of the component in ComponentsWindow
     * @param nameInLanguage  corresponding name of the component in the programming
     *                        language
     * @param codeTemplate    code template used to convert rectangle to source code
     * @param isRootComponent whether this is topmost parent component in design
     */
    public Component(String name, String nameInLanguage, String codeTemplate, boolean isRootComponent) {
        this.name = name;
        this.nameInLanguage = nameInLanguage;
        this.codeTemplate = codeTemplate;
        this.isRootComponent = isRootComponent;
    }

    /**
     * Constructor for Component class
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param name            name of the component in ComponentsWindow
     * @param nameInLanguage  corresponding name of the component in the programming
     *                        language
     * @param isRootComponent whether this is topmost parent component in design
     */
    public Component(String name, String nameInLanguage, boolean isRootComponent) {
        this.name = name;
        this.nameInLanguage = nameInLanguage;
        this.codeTemplate = "";
        this.isRootComponent = isRootComponent;
    }

    /**
     * Getter method for name
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return name of the component in ComponentsWindow
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for nameInLanguage
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return corresponding name of the component in the programming language
     */
    public String getNameInLanguage() {
        return nameInLanguage;
    }

    /**
     * Getter method for isRootComponent
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return whether this is topmost parent component in design
     */
    public boolean getIsRootComponent() {
        return isRootComponent;
    }

    /**
     * Method to convert create source code from component
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param rect         information about component being converted to source
     *                     code
     * @param canvasWidth  width of the CanvasWindow used for design
     * @param canvasHeight height of the CanvasWindow used for design
     * @param screenWidth  width of the computer screen used for design
     * @param screenHeight height of the computer screen used for design
     * 
     * @return generated source code
     */
    public String generateCode(
            Rectangle rect,
            int canvasWidth,
            int canvasHeight,
            int screenWidth,
            int screenHeight) {
        String code = codeTemplate;
        double value = 0.0;
        for (String placeHolder : CodeTemplatePlaceholders.placeHolders) {
            if (placeHolder == "COMP_COLOR_HEX") {
                code = code.replaceAll(placeHolder, hexColor(rect.getColor()));
            } else if (placeHolder == "COMP_NAME") {
                code = code.replaceAll(placeHolder, nameInLanguage);
            } else if (placeHolder == "COMP_TEXT") {
                code = code.replaceAll(placeHolder, rect.getLabel());
            } else {
                switch (placeHolder) {
                    case "COMP_X":
                        value = rect.getX();
                        break;
                    case "COMP_Y":
                        value = rect.getY();
                        break;
                    case "COMP_WIDTH":
                        value = rect.getWidth();
                        break;
                    case "COMP_HEIGHT":
                        value = rect.getHeight();
                        break;

                    case "COMP_RELC_X":
                        value = (double) rect.getX() / (double) canvasWidth;
                        break;
                    case "COMP_RELC_Y":
                        value = (double) rect.getY() / (double) canvasHeight;
                        break;
                    case "COMP_RELC_WIDTH":
                        value = (double) rect.getWidth() / (double) canvasWidth;
                        break;
                    case "COMP_RELC_HEIGHT":
                        value = (double) rect.getHeight() / (double) canvasHeight;
                        break;

                    case "COMP_RELS_X":
                        value = (double) rect.getX() / (double) screenWidth;
                        break;
                    case "COMP_RELS_Y":
                        value = (double) rect.getY() / (double) screenHeight;
                        break;
                    case "COMP_RELS_WIDTH":
                        value = (double) rect.getWidth() / (double) screenWidth;
                        break;
                    case "COMP_RELS_HEIGHT":
                        value = (double) rect.getHeight() / (double) screenHeight;
                        break;

                    case "COMP_COLOR_R":
                        value = rect.getColor().getRed();
                        break;
                    case "COMP_COLOR_G":
                        value = rect.getColor().getGreen();
                        break;
                    case "COMP_COLOR_B":
                        value = rect.getColor().getBlue();
                        break;
                    case "COMP_COLOR_A":
                        value = rect.getColor().getAlpha();
                        break;
                }
                code = code.replaceAll(placeHolder, Double.toString(value));
            }
        }
        return code;
    }

    /**
     * Method to convert java.awt.color to hex String
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param color color to be converted to String
     * 
     * @return hex color
     */
    public String hexColor(Color color) {
        String red = Integer.toHexString(color.getRed());
        while (red.length() < 2)
            red = "0" + red;
        String blue = Integer.toHexString(color.getBlue());
        while (blue.length() < 2)
            blue = "0" + blue;
        String green = Integer.toHexString(color.getGreen());
        while (green.length() < 2)
            green = "0" + green;
        String alpha = Integer.toHexString(color.getAlpha());
        while (alpha.length() < 2)
            alpha = "0" + alpha;
        String hexCol = String.format("#%s%s%s", red, blue, green);
        return hexCol;
    }
}
