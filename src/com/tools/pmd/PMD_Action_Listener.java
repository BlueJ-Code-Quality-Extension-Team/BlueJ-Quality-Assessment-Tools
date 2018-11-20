package com.tools.pmd;

import java.io.File;

import java.nio.file.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;

import bluej.extensions.*;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FilenameFilter;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.net.URL;
import java.net.URLClassLoader;
/**
 * Interface between bluejextension and PMD_Runners
 */
//TODO: Implement Properties and fix file path for command
public class PMD_Action_Listener implements ActionListener {
    //Use default or custom setting - will be expanded when properties are up
    Boolean useDefault = true;

    BProject bProject;
    String projectDir;
    String PMDPath;
    String [] filenames;
    String [] filePaths;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String OS = System.getProperty("os.name").toLowerCase();

    public PMD_Action_Listener(BPackage aPackage){
        try{
            this.bProject = aPackage.getProject();
            projectDir=bProject.getDir().toString();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        BClass [] classes;
        classes = addClasses(aPackage);
        setClassesProperties(classes);
    }

    public void updateClasses(BPackage aPackage){
        setClassesProperties(addClasses(aPackage));
    }


    private BClass [] addClasses(BPackage aPackage){
        BClass [] classes;
        try{
            classes = aPackage.getClasses();
        }catch(ProjectNotOpenException e){
            classes = null;
            e.printStackTrace();
        }catch(PackageNotFoundException e){
            classes = null;
            e.printStackTrace();
        }

        return classes;
    }

    private void setClassesProperties(BClass [] classes){
        if(classes != null){
            filenames = new String[classes.length];
            filePaths = new String[classes.length];
            int iterCount = 0;
            try{
                for(BClass cls : classes){
                    filenames[iterCount] = cls.getJavaFile().toString();
                    filePaths[iterCount] = cls.getJavaFile().getAbsolutePath();
                    iterCount++;
                }
            }catch(ProjectNotOpenException e){
                e.printStackTrace();
            }catch(PackageNotFoundException e){
                e.printStackTrace();
            }
            for(int i = 0; i < filenames.length; i++){
                try{
                    filePaths[i] = filenames[i];
                }catch(Exception e){e.printStackTrace();}
            }
        }
    }

    public void actionPerformed(ActionEvent event){
        getDefaultPMDPath();

        if(PMDPath == null){
            String warning = "Error: Please ensure that PMD is installed correctly";
            JOptionPane.showMessageDialog(null,warning);
            return;
        }

        PMD_Runner runner;

        runner = PMD_Runner_Factory.getPMDRunner(PMDPath);

        StringBuilder msg = new StringBuilder("Any problems found are displayed below:");
        msg.append(LINE_SEPARATOR);
        for(int i = 0; i < filenames.length; i++){
            String output = runner.runText(filePaths[i]);
            msg.append(output);
            msg.append(LINE_SEPARATOR);
        }

        JOptionPane.showMessageDialog(null, msg);
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
            String msg = "Please ensure that pmd-bin-* is installed only once in BlueJ lib folder";
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
