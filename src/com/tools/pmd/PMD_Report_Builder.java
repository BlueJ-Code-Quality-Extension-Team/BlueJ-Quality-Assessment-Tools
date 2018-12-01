package com.tools.pmd;

import bluej.extensions.*;

import com.bluejmanager.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FilenameFilter;
import java.io.File;

import java.util.Set;
import java.util.Iterator;

import javax.swing.JOptionPane;

import java.nio.file.*;

/** Singleton class to provide all report building */
public class PMD_Report_Builder {

    /** singleton */
    private static PMD_Report_Builder PMDReportBuilder;

    private String PMDPath;
    private String checks;
    private static final String OS = System.getProperty("os.name").toLowerCase();

    private PMD_Report_Builder(){

    }
    
    /** returns singleton instance */
    public static PMD_Report_Builder getInstance(){
        if(PMDReportBuilder == null){
            PMDReportBuilder = new PMD_Report_Builder();
        }

        return PMDReportBuilder;
    }

    /**
     * Takes a set of files and generates a PMD_Report from them 
     * @param files Set of files to be evaluated 
    */
    public synchronized PMD_Report generatePMDReport(Set<File> files){
        PMD_Report report = new PMD_Report();
        BlueJPropertiesAdapter mnger = (BlueJPropertiesAdapter) BlueJManager.getInstance().getPropertiesAdapter();
        boolean defalt = Boolean.parseBoolean(mnger.getProperty("PMD.default"));
        if(defalt)
            getDefaultPMDPath();
        else{
            PMDPath = mnger.getProperty("PMD.Path");
            if(PMDPath == null || PMDPath == "")
                getDefaultPMDPath();
            String check = mnger.getProperty("PMD.Check");
            if(check != "" && check!= null && (check.toLowerCase().endsWith("xml") || check.toLowerCase().startsWith("java-")))
                checks = check;
            else
                checks = "java-quickstart";
        }

        if(PMDPath == null){
            String warning = "Error: Please ensure that PMD is installed correctly";
            JOptionPane.showMessageDialog(null,warning);
            return report;
        }

        PMD_Runner runner;
        if(defalt){
            runner = new PMD_Runner(PMDPath);
        }
        else{
            runner = new PMD_Runner(PMDPath,checks);
        }
        if(files != null){
            for(File f : files){
                String output = runner.runText(f.toString());
                report.appendMessage(output);
            }
        }

        report.finalize();
        return report;
    }

    /**
     * Requires user to unpack PMD binaries into BlueJ/lib folder
     */
    public void getDefaultPMDPath(){
        File bluejlib = new File("lib");
        FilenameFilter filter = new FilenameFilter(){
            public boolean accept(File dir, String name){
                return name.startsWith("pmd-bin");
            }
        };
        String [] children = bluejlib.list(filter);
        if(children.length != 1){
            String msg = "Please ensure that pmd-bin-* is installed once and only once in BlueJ lib folder";
            PMDPath = null;
            JOptionPane.showMessageDialog(null, msg);
        }
        else{
            File executable = new File(bluejlib, children[0]);
            if (isWindows()) {
                executable = new File(executable, "bin/pmd.bat");
            } else {
                executable = new File(executable, "bin/run.sh");
            }
            PMDPath = executable.getAbsolutePath();
        }
    }


    private boolean isWindows(){
        return (OS.indexOf("win") >= 0);
    }
}
