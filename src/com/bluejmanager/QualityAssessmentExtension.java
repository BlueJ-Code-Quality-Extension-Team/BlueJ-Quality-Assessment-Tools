////////////////////////////////////////////////////////////////////////////////
// BlueJ Quality Extension Tools extension:
//    Checks Java source code for adherence to a set of rules.
// Copyright (C) 2003-2004  Rick Giles
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////

package com.bluejmanager;

import java.awt.Frame;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

import bluej.extensions.BlueJ;
import bluej.extensions.Extension;

import com.tools.checkstyle.*;
import com.tools.pmd.*;

/**
 * BlueJ extension for Checkstyle.
 * @author Rick Giles
 * @author CS4250 Students (MSU Denver)
 * @version 1.11
 */
public class QualityAssessmentExtension extends Extension
{
    /** true if the log factory has been initialized */
    public static boolean mLogInitialized = false;

    /** Factory for creating org.apache.commons.logging.Log instances */
    public static LogFactory mLogFactory;

    /** singleton */
    private static QualityAssessmentExtension sInstance;

    /** BlueJ tools menu item for Quality Assessment Tools */
    private ExtensionMenu mMenu;

    /** extension name */
    private static final String NAME = "Quality Assessment Tools";

    /** extension description */
    private static final String DESCRIPTION =
        "Various tools to analyze Java source code.";

    /**  extension version */
    private static final String VERSION = "0.0.1";

    /** extension URL */
    private static final String URL =
        "https://github.com/BlueJ-Code-Quality-Extension-" +
                "Team/BlueJ-Quality-Assessment-Tools/";

    /** Handles display and events for checkstyle tool */
    public com.tools.checkstyle.UI mCheckstyleUI;
    
    /** Holds PMD report */
    public com.tools.pmd.PMD_Report mPMDReport;

   /**
     * Returns the single QualityAssessmentExtension instance.
     * @return  the single QualityAssessmentExtension instance.
     */
    public static QualityAssessmentExtension getInstance()
    {
        return sInstance;
    }

    /**
     * Constructs a <code>QualityAssessmentExtension</code>.
     */
    public QualityAssessmentExtension()
    {
        sInstance = this; // establish singleton extension

        try {
            mLogFactory = LogFactory.getFactory();
            mLogInitialized = true;
        }
        catch (LogConfigurationException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    /** @see bluej.extensions.Extension#startup(bluej.extensions.BlueJ) */
    public void startup(BlueJ aBlueJ)
    {
        // establish singleton manager for the BlueJ application proxy
        BlueJManager.getInstance().setBlueJ(aBlueJ);

        mCheckstyleUI = UI.getCheckstyleUI();
        mCheckstyleUI.addListeners(aBlueJ);

        mPMDReport = new PMD_Report();
        aBlueJ.addCompileListener(new com.tools.pmd.PMD_Compile_Listener());

        // install menu item
        mMenu = new ExtensionMenu();
        aBlueJ.setMenuGenerator(mMenu);

        // install preferences handler
        Preferences myPreferences = new Preferences();
        aBlueJ.setPreferenceGenerator(myPreferences);
    }

    /**
     * Saves audit window information.
     * @see bluej.extensions.Extension#terminate()
     */
    public void terminate()
    {
        mCheckstyleUI.terminate();
    }

    /** @see bluej.extensions.Extension#getDescription() */
    public String getDescription()
    {
        return DESCRIPTION;
    }

    /** @see bluej.extensions.Extension#getName() */
    public String getName()
    {
        return NAME;
    }

    /** @see bluej.extensions.Extension#getURL() */
    public URL getURL()
    {
        URL result = null;
        try {
            result = new URL(URL);
        }
        catch (MalformedURLException e) {
            error(e);
        }
        return result;
    }

    /** @see bluej.extensions.Extension#getVersion() */
    public String getVersion()
    {
        return VERSION;
    }

    /** @see bluej.extensions.Extension#isCompatible() */
    public boolean isCompatible()
    {
        return true;
    }

    /**
     * Reports an error message.
     * @param aMessage the message to report.
     */
    public static void error(String aMessage)
    {
        Frame frame = BlueJManager.getInstance().getCurrentFrame();
        JOptionPane.showMessageDialog(frame, aMessage);
        if (mLogInitialized) {
            final Log log = mLogFactory.getInstance(QualityAssessmentExtension.class);
            log.info(aMessage);
        }
        else {
            System.out.println(aMessage);
        }
    }

    /**
     * Reports an exception.
     * @param aException the exception to report.
     */
    public static void error(Exception aException)
    {
        aException.printStackTrace();
        error("" + aException);
    }
}
