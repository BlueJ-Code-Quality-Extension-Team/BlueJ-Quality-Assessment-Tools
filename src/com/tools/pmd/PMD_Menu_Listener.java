package com.tools.pmd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.bluejmanager.QualityAssessmentExtension;

/**
 * Adds listener to PMD Menu. On click displays the generated report.
 */
public class PMD_Menu_Listener implements ActionListener {
    /**
     * Shows PMD results when menu item is clicked.
     * @see java.awt.event.ActionListener
     */
    public void actionPerformed(ActionEvent event) {
        String msg = QualityAssessmentExtension.getInstance().mPMDReport.getMessage();
        JOptionPane.showMessageDialog(null, msg);
    }
}
