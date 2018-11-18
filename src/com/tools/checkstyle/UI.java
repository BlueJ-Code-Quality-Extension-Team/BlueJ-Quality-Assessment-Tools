package com.tools.checkstyle;

import bluej.extensions.BlueJ;
import bluej.extensions.event.*;
import com.bluejmanager.BlueJManager;
import com.bluejmanager.QualityAssessmentExtension;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

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
        BlueJManager.getInstance().saveAuditFrame(mFrame);
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
            BlueJManager.getInstance().initAuditFrame(mFrame);
            mFrame.pack();
        }
    }

    /**
     * Refreshes the audit view. If there is an error, report it.
     */
    public void refreshView()
    {
        if (mFrame.isShowing()) {
            final BlueJChecker checker = new BlueJChecker();
            final Auditor auditor;
            try {
                auditor = checker.processAllFiles();
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
        public void compileError(CompileEvent aEvent)
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

        /** @see bluej.extensions.event.CompileListener */
        public void compileWarning(CompileEvent aEvent)
        {
            recordCompileEnd(aEvent.getFiles());
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
        public void compileFailed(CompileEvent aEvent)
        {
            recordCompileEnd(aEvent.getFiles());
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
