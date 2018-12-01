package com.tools.checkstyle;

import bluej.extensions.BlueJ;
import bluej.extensions.event.*;
import com.bluejmanager.BlueJManager;
import com.bluejmanager.QualityAssessmentExtension;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class UI {

    /** Singleton instance. */
    private static UI mInstance;

    /** Display audit results. */
    private AuditFrame mFrame = null;

    /** Periodically checks for file set changes. */
    private Timer mTimer;

    /** Files being compiled. */
    private Set<File> mCompilingFiles = new HashSet<File>();

    /** Interval between audit checks (milliseconds). */
    private static final int AUDIT_CHECK_INTERVAL = 2000;

    /** Private constructor for singleton. */
    private UI(){
        final ActionListener listener = new FilesChangeListener();
        mTimer = new Timer(AUDIT_CHECK_INTERVAL, listener);
        mTimer.start();
    }

    /** Public method to access singleton */
    public static UI getCheckstyleUI(){
        if (mInstance == null){
            mInstance = new UI();
            return  mInstance;
        }
        else{
            return mInstance;
        }
    }

    /**
     * Add listeners needed by checkstyle to the BlueJ instance.
     * @param aBlueJ The BlueJ instance to add listeners to
     */
    public void addListeners(BlueJ aBlueJ){
        // register listeners
        aBlueJ.addApplicationListener(new CheckstyleApplicationListener());
        aBlueJ.addPackageListener(new CheckstylePackageListener());
        aBlueJ.addCompileListener(new CheckstyleCompileListener());
    }

    /**
     * Saves audit window information.
     * @see bluej.extensions.Extension#terminate()
     */
    public void terminate()
    {
        saveAuditFrame(mFrame);
        mCompilingFiles.clear();
        mTimer.stop();
    }

    /**
     * Creates and installs an audit frame
     */
    private synchronized void buildAuditFrame()
    {
        /** @see java.awt.event.WindowAdapter */
        final class AuditFrameListener extends WindowAdapter
        {
            /** @see java.awt.event.WindowListener */
            public void windowOpened(WindowEvent aEvent)
            {
                updateTimer();
            }

            /** @see java.awt.event.WindowListener */
            public void windowClosed(WindowEvent aEvent)
            {
                updateTimer();
            }

            /** @see java.awt.event.WindowListener */
            public void windowIconified(WindowEvent aEvent)
            {
                updateTimer();
            }

            /** @see java.awt.event.WindowListener */
            public void windowDeiconified(WindowEvent aEvent)
            {
                updateTimer();
            }
        }

        if (mFrame == null) {
            mFrame = new AuditFrame();
            mFrame.addWindowListener(new AuditFrameListener());
            initAuditFrame(mFrame);
            mFrame.pack();
        }
    }

    /**
     * Initializes an audit frame from extension properties.
     * @param aFrame the audit frame to initialize.
     */
    public void initAuditFrame(AuditFrame aFrame)
    {
        BlueJManager manager = BlueJManager.getInstance();

        // location and size
        final String frameDimensions =
                manager.getExtensionPropertyString(
                        Settings.FRAME_DIMENSIONS_KEY, "");
        if (!frameDimensions.equals(""))
        {
            final StringTokenizer tokenizer =
                    new StringTokenizer(frameDimensions);
            final int x = Integer.parseInt(tokenizer.nextToken());
            final int y = Integer.parseInt(tokenizer.nextToken());
            aFrame.setLocation(x, y);
            // aFrame.setSize(width, height);
        }
        else
        {
            final Frame bluejFrame = manager.getCurrentFrame();
            Point corner = new Point(0, 0);
            if (bluejFrame != null) {
                corner = bluejFrame.getLocation();
            }

            int offset = Settings.FRAME_OFFSET;
            corner.translate(offset, offset);
            aFrame.setLocation(corner);
        }
        if (Boolean.valueOf(manager.getExtensionPropertyString(
                Settings.IS_OPEN_KEY, "false")).booleanValue())
        {
            aFrame.setVisible(true);
        }
    }

    /**
     * Saves audit frame information in properties.
     * @param aFrame the frame to save.
     */
    public void saveAuditFrame(AuditFrame aFrame)
    {
        BlueJManager manager = BlueJManager.getInstance();

        manager.setExtensionPropertyString(
                Settings.IS_OPEN_KEY, "" + aFrame.isShowing());
        final String frameDimensions = (int) aFrame.getLocation().getX() + " "
                + (int) aFrame.getLocation().getY() + " "
                + (int) aFrame.getSize().getWidth() + " "
                + (int) aFrame.getSize().getHeight();
        manager.setExtensionPropertyString(
                Settings.FRAME_DIMENSIONS_KEY, frameDimensions);
    }

    /**
     * Refreshes the audit view. If there is an error, report it.
     */
    public void refreshView()
    {
        if (mFrame.isShowing()) {
            final Auditor auditor;
            try {
                auditor = BlueJChecker.processAllFiles();
            }
            catch (CheckstyleException ex) {
                QualityAssessmentExtension.error(ex);
                return;
            }
            viewAudit(auditor);
        }
    }

    /**
     * Shows the audit frame.
     */
    public void showAuditFrame()
    {
        buildAuditFrame();
        mFrame.setVisible(true);
        refreshView();
    }

    /**
     * Updates view of audit results.
     * @param aAuditor the auditor with audit results
     */
    public synchronized void viewAudit(final Auditor aAuditor)
    {
        // execute on the application's event-dispatch thread
        final Runnable update = new Runnable()
        {
            public void run()
            {
                if (mFrame != null) {
                    mFrame.setAuditor(aAuditor);
                }
            }
        };
        SwingUtilities.invokeLater(update);
    }

    /**
     * Starts or stops timer.
     */
    private void updateTimer()
    {
        if (mCompilingFiles.isEmpty() && mFrame.isShowing()) {
            mTimer.start();
        }
        else {
            mTimer.stop();
        }
    }

    /** @see bluej.extensions.event.PackageListener */
    private class CheckstylePackageListener implements PackageListener
    {
        /** @see bluej.extensions.event.PackageListener */
        public void packageOpened(PackageEvent aEvent)
        {
            // refreshView();
        }

        /** @see bluej.extensions.event.PackageListener */
        public void packageClosing(PackageEvent aEvent)
        {
            // refreshView();
        }
    }

    /** @see bluej.extensions.event.CompileListener */
    private class CheckstyleCompileListener implements CompileListener
    {
        /** @see bluej.extensions.event.CompileListener */
        public void compileStarted(CompileEvent aEvent)
        {
            recordCompileStart(aEvent.getFiles());
        }

        /**
         * Records the start of compilation of a set of files.
         * @param aFiles the set of files being compiled.
         */
        private void recordCompileStart(File[] aFiles)
        {
            for (int i = 0; i < aFiles.length; i++) {
                mCompilingFiles.add(aFiles[i]);
            }
            updateTimer();
        }

        /** @see bluej.extensions.event.CompileListener */
        public void compileSucceeded(CompileEvent aEvent)
        {
            recordCompileEnd(aEvent.getFiles());
            if (mCompilingFiles.isEmpty()) {
                refreshView();
            }
        }

        /** @see bluej.extensions.event.CompileListener */
        public void compileError(CompileEvent aEvent)
        {
            recordCompileEnd(aEvent.getFiles());
        }

        /** @see bluej.extensions.event.CompileListener */
        public void compileWarning(CompileEvent aEvent)
        {
            recordCompileEnd(aEvent.getFiles());
        }

        /** @see bluej.extensions.event.CompileListener */
        public void compileFailed(CompileEvent aEvent)
        {
            recordCompileEnd(aEvent.getFiles());
        }

        /**
         * Records the end of compilation of a set of files.
         * @param aFiles the set of files ending compilation.
         */
        private void recordCompileEnd(File[] aFiles)
        {
            for (int i = 0; i < aFiles.length; i++) {
                mCompilingFiles.remove(aFiles[i]);
            }
            updateTimer();
        }
    }

    /** @see bluej.extensions.event.ApplicationListener */
    private class CheckstyleApplicationListener implements ApplicationListener
    {

        /**
         * Initializes the audit window.
         * @see bluej.extensions.event.ApplicationListener
         */
        public void blueJReady(ApplicationEvent aEvent)
        {
            buildAuditFrame();
            refreshView();
        }
    }
}
