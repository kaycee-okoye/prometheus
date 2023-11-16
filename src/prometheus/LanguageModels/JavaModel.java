package prometheus.LanguageModels;

import prometheus.Models.Component;
import prometheus.Models.LanguageModel;

/**
 * Child class of LanguageModel showing sample code templates for Java
 * Programming Language
 * 
 * @author Kenechukwu Okoye
 * @version 2023.11.15
 */
public class JavaModel extends LanguageModel {
        private String defaultCodeTemplate = "\n\t\tCOMP_NAME VARIABLE_NAME = COMP_NAME()" +
                        "\n\t\tVARIABLE_NAME.setLocation(COMP_X, COMP_Y)" +
                        "\n\t\tVARIABLE_NAME.setSize(COMP_WIDTH, COMP_HEIGHT)" +
                        "\n\t\tVARIABLE_NAME.setBackground('COMP_COLOR_HEX')" +
                        "\n\t\tVARIABLE_NAME.setText('COMP_TEXT')" +
                        "\n\t\tnTOP_CONTAINER_NAME.add('VARIABLE_NAME')\n\n";

        Component[] components = {
                        new Component(
                                        "Button",
                                        "JButton",
                                        defaultCodeTemplate),
                        new Component(
                                        "Label",
                                        "JLabel",
                                        defaultCodeTemplate),
                        new Component(
                                        "Text Field",
                                        "JTextField",
                                        defaultCodeTemplate),
                        new Component(
                                        "Text Area",
                                        "JTextArea",
                                        defaultCodeTemplate),
                        new Component(
                                        "Combo Box",
                                        "JComboBox",
                                        defaultCodeTemplate),
                        new Component(
                                        "Toggle Button",
                                        "JToggleButton",
                                        defaultCodeTemplate),
                        new Component(
                                        "Radio Button",
                                        "JRadioButton",
                                        defaultCodeTemplate),
                        new Component(
                                        "Check Box",
                                        "JCheckBox",
                                        defaultCodeTemplate),
                        new Component(
                                        "Slider",
                                        "JSlider",
                                        defaultCodeTemplate),
                        new Component(
                                        "JFrame",
                                        "JFrame",
                                        "COMP_NAME frame = new COMP_NAME('PROJECT_NAME');" +
                                                        "\n\t\tframe.setSize(COMP_RELS_WIDTH,COMP_RELS_HEIGHT);" +
                                                        "\n\t\tframe.setLocation('COMP_RELS_X, COMP_RELS_Y');" +
                                                        "\n\t\tframe.setResizable(false);" +
                                                        "\n\t\tJPanel nTOP_CONTAINER_NAME = new JPanel();" +
                                                        "\n\t\tTOP_CONTAINER_NAME.setBackground('COMP_COLOR_HEX');" +
                                                        "\n\t\tTOP_CONTAINER_NAME.setLayout(null);\n",
                                        true),
        };

        JavaModel() {
                super("Java");
                super.components = this.components;
        }

        @Override
        public String getPrefix() {
                return "import java.swing.*;\n" +
                                "import java.awt.*;\n" +
                                "public class PROJECT_NAME {\n" +
                                "\tpublic static void main(String[] args) {\n";
        }

        @Override
        public String getSuffix() {
                return "\n\t\tframe.add(TOP_CONTAINER_NAME);\n\t\troot.setVisible(true); \n\t}\n}";
        }

        @Override
        public String getFileExtension() {
                return ".jar";
        }
}
