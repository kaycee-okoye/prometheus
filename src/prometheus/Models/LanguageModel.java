package prometheus.Models;

import java.util.ArrayList;

/**
 * Template class for storing code templates for a specific programming language
 * 
 * This class is to be extended and its method overriden for each programming
 * language
 * that the user is to be able to generate source code for. LanguageModel
 * holds information about a programming language necessary for the
 * GUIWriter to create source code for the programming language
 * 
 * @author Kenechukwu Okoye
 * @version 2023.11.15
 */
public abstract class LanguageModel {
    private String name;
    protected Component[] components;

    /**
     * Constructor for LanguageModel class
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param name the programming language the model belongs to
     */
    public LanguageModel(String name) {
        this.name = name;
    }

    /**
     * Getter method for name
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return the programming language the model belongs to
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for components
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return all component code templates used in the LanguageModel
     */
    public Component[] getComponents() {
        return components;
    }

    /**
     * Getter method for root component
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return code template for top-most parent component in the LanguageModel
     */
    public Component getRootComponent() {
        for (Component component : components) {
            if (component.getIsRootComponent() == true) {
                return component;
            }
        }
        return new Component("NONE", "NONE");
    }

    /**
     * Getter method for component names
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @param includeRootComponent whether to include name of top-most parent
     *                             component.
     *                             This might be set to False to prevent given the
     *                             user the option of adding another
     *                             root component in the CanvasWindow
     * 
     * @return names of all available components in the LanguageModel
     */
    public String[] getComponentNames(boolean includeRootComponent) {
        ArrayList<String> componentNames = new ArrayList<String>();
        for (Component component : components) {
            if (component.getIsRootComponent() != true || includeRootComponent) {
                componentNames.add(component.getName());
            }
        }
        return componentNames.toArray(new String[componentNames.size()]);
    }

    /**
     * Getter method for code preamble
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return code preamble used in the programming language
     */
    public abstract String getPrefix();

    /**
     * Getter method for code suffix
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return code closing statements used in the programming language
     */
    public abstract String getSuffix();

    /**
     * Getter method for source code file extension
     * 
     * @author Kenechukwu Okoye
     * @version 2023.11.15
     * 
     * @return source code file extensions for the programming language
     */
    public abstract String getFileExtension();
}
