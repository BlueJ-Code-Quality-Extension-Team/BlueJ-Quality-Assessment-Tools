package com.tools.pmd;

import bluej.extensions.BPackage;
import bluej.extensions.BlueJ;
import bluej.extensions.PreferenceGenerator;
import javax.swing.*;
import java.awt.*;




public class PMD_Preference_Panel implements PreferenceGenerator {

    private JPanel PMDPanel;
    private JTextField pmdPath;
    private JTextField pmdCheck;
    private JButton button;
    private BlueJ bluej;
    public static final String PROPERTY_PMD_PATH = "PMD.Path";
    public static final String PROPERTY_PMD_CHECK = "PMD.check";
    private JCheckBox defaultCheckbox;
    private static final int FIELD_WIDTH = 40;

    public PMD_Preference_Panel(BlueJ bluej) {
        this.bluej = bluej;
        PMDPanel = new JPanel();
        PMDPanel.setLayout(new GridLayout(2,1));
        final JPanel labelTextFieldPanel = new JPanel();
        final JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(3, 1));
        labelPanel.add(new JLabel("DefaultPMD"));
        labelPanel.add(new JLabel("Path to PMD installation:"));
        labelPanel.add(new JLabel("Path to Checks:"));
        labelTextFieldPanel.add(labelPanel);

        final JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridLayout(3,1));
        defaultCheckbox = new JCheckBox("");
        fieldPanel.add(defaultCheckbox);
        pmdPath = new JTextField(FIELD_WIDTH);
        fieldPanel.add(pmdPath);
        pmdCheck = new JTextField(FIELD_WIDTH);
        fieldPanel.add(pmdCheck);
        labelTextFieldPanel.add(fieldPanel);

        final JPanel buttonPanel = new JPanel();
        button = new JButton("OK");
        buttonPanel.add(button);
        PMDPanel.add(labelTextFieldPanel);
        PMDPanel.add(buttonPanel);


        loadValues();

    }



    public void saveValues() {

        bluej.setExtensionPropertyString("PMD.Path", pmdPath.getText());
        bluej.setExtensionPropertyString("PMD.check", pmdCheck.getText());
    }
    public void loadValues(){

        pmdPath.setText(bluej.getExtensionPropertyString("PMD.Path", ""));
        pmdCheck.setText(bluej.getExtensionPropertyString("PMD.check", ""));




    }
    public JPanel getPanel () {
        return PMDPanel;
    }


}

