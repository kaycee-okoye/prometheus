package prometheus.Constants;

/**
 * Class containing code place holders for component parameters
 * 
 * These are used in LanguageModel objects to represent parameters
 * such as the position, color and dimensions of a component.
 * 
 * @author  Kenechukwu Okoye
 * @version 2023.11.15
 */
public class CodeTemplatePlaceholders {
    public static String[] placeHolders = {
            // Absolute component parameters
            "COMP_X",
            "COMP_Y",
            "COMP_WIDTH",
            "COMP_HEIGHT",

            // Component parameters relative to canvas dimensions
            "COMP_RELC_X",
            "COMP_RELC_Y",
            "COMP_RELC_WIDTH",
            "COMP_RELC_HEIGHT",

            // Component parameters relative to screen dimensions
            "COMP_RELS_X",
            "COMP_RELS_Y",
            "COMP_RELS_WIDTH",
            "COMP_RELS_HEIGHT",

            // Component color parameters
            "COMP_COLOR_R",
            "COMP_COLOR_G",
            "COMP_COLOR_B",
            "COMP_COLOR_A",
            "COMP_COLOR_HEX",
    };
}
