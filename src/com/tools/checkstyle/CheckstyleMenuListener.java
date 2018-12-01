package com.tools.checkstyle;

import com.bluejmanager.QualityAssessmentExtension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Action listener for the Checkstyle menu item.
 * Audits files of the current package.
 * @author Rick Giles
 * @version 13-May-2003
 */
public class CheckstyleMenuListener implements ActionListener {
    /**
     * Audits the open projects and shows the results.
     * @see java.awt.event.ActionListener
     */
    public void actionPerformed(ActionEvent aEvent)
    {
        QualityAssessmentExtension.getInstance().mCheckstyleUI.showAuditFrame();
    }
}
