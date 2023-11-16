package prometheus.LanguageModels;

import prometheus.Models.Component;
import prometheus.Models.LanguageModel;

/**
 * Child class of LanguageModel showing sample code templates for Python
 * Programming Language
 * 
 * @author Kenechukwu Okoye
 * @version 2023.11.15
 */
public class PythonModel extends LanguageModel {
        private String defaultCodeTemplate = "\nVARIABLE_NAME = tkinter.COMP_NAME(TOP_CONTAINER_NAME, text = 'COMP_TEXT', background = 'COMP_COLOR_HEX')"
                        +
                        "\nVARIABLE_NAME.place(relx = COMP_RELC_X, rely = COMP_RELC_Y, relwidth = COMP_RELC_WIDTH, relheight = COMP_RELC_HEIGHT)";
        Component[] components = {
                        new Component(
                                        "Button",
                                        "Button",
                                        defaultCodeTemplate),
                        new Component(
                                        "Label",
                                        "Label",
                                        defaultCodeTemplate),
                        new Component(
                                        "Text Field",
                                        "Entry",
                                        defaultCodeTemplate),
                        new Component(
                                        "Text Area",
                                        "Message",
                                        defaultCodeTemplate),
                        new Component(
                                        "Combo Box",
                                        "Listbox",
                                        defaultCodeTemplate),
                        new Component(
                                        "Toggle Button",
                                        "Checkbutton",
                                        defaultCodeTemplate),
                        new Component(
                                        "Radio Button",
                                        "Radiobutton",
                                        defaultCodeTemplate),
                        new Component(
                                        "Check Box",
                                        "Checkbutton",
                                        defaultCodeTemplate),
                        new Component(
                                        "Slider",
                                        "Scale",
                                        defaultCodeTemplate),
                        new Component(
                                        "Top Widget",
                                        "",
                                        "TOP_CONTAINER_NAME = tkinter.Tk(className='PROJECT_NAME')\nTOP_CONTAINER_NAME.configure(background = 'COMP_COLOR_HEX')"
                                                        +
                                                        "\nTOP_CONTAINER_NAME.geometry('COMP_RELS_WIDTHxCOMP_RELS_HEIGHT')\n",
                                        true),
        };

        public PythonModel() {
                super("Python");
                super.components = this.components;
        }

        @Override
        public String getPrefix() {
                return "import tkinter\n";
        }

        @Override
        public String getSuffix() {
                return "top.mainloop()";
        }

        @Override
        public String getFileExtension() {
                return ".py";
        }
}
