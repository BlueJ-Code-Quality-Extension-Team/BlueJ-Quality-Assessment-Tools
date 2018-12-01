package com.tools.pmd;

import bluej.extensions.PreferenceGenerator;
import com.bluejmanager.BlueJManager;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Manages PMD extension panel in BlueJ preferences..
 */
public class PMD_Preference_Panel implements PreferenceGenerator {

    /** The panel for this tool to display in preferences. */
    private JPanel PMDPanel;

    /** The path to the PMD distribution to use. */
    private JTextField pmdPath;

    /** The file containing checks for PMD to run */
    private JTextField pmdCheck;
    
    /** Checkbox to use default pmd location */
    private JCheckBox defaultCheckbox;

    /** Path to PMD distribution */
    private static final String PROPERTY_PMD_PATH = "PMD.Path";

    /** Path to file with list of PMD checks to perform. */
    private static final String PROPERTY_PMD_CHECK = "PMD.Check";
    
    /** BlueJ default state property */
    private static final String PROPERTY_PMD_DEFAULT = "PMD.default";

    /** Field width for text fields */
    private static final int FIELD_WIDTH = 40;
    
    /** Constructor */
    public PMD_Preference_Panel() {
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
        defaultCheckbox.setSelected(true);
        fieldPanel.add(defaultCheckbox);

        pmdPath = new JTextField(FIELD_WIDTH);
        fieldPanel.add(pmdPath);
        pmdCheck = new JTextField(FIELD_WIDTH);
        fieldPanel.add(pmdCheck);
        labelTextFieldPanel.add(fieldPanel);

        final JPanel buttonPanel = new JPanel();
        JButton button = new JButton("OK");
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                saveValues();
            }
        });
        buttonPanel.add(button);
        PMDPanel.add(labelTextFieldPanel);
        PMDPanel.add(buttonPanel);

        loadValues();
    }
    
    /** Saves values of PMD_Path, PMD_Check, and PMD_default using the Bluej Manager */
    public void saveValues() {
        BlueJManager manager = BlueJManager.getInstance();
        manager.setExtensionPropertyString(PROPERTY_PMD_PATH, pmdPath.getText());
        manager.setExtensionPropertyString(PROPERTY_PMD_CHECK, pmdCheck.getText());
        manager.setExtensionPropertyString(PROPERTY_PMD_DEFAULT, Boolean.toString(defaultCheckbox.isSelected()) );
        loadValues();
    }

    /** Loads values of PMD_Path, PMD_Check, and PMD_default using the Bluej Manager */
    public void loadValues(){
        BlueJManager manger = BlueJManager.getInstance();

        String path = manger.getExtensionPropertyString(PROPERTY_PMD_PATH, "");
        String check = manger.getExtensionPropertyString(PROPERTY_PMD_CHECK, "java-quickstart");
        String defaultCB = manger.getExtensionPropertyString(PROPERTY_PMD_DEFAULT, "true");
        
        pmdPath.setText(path);
        pmdCheck.setText(check);
        defaultCheckbox.setSelected(Boolean.parseBoolean(defaultCB));
    }

    
    /** Returns the preferences jpanel */
    public JPanel getPanel () {
        return PMDPanel;
    }
}

