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

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * 
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
    private static String OS = System.getProperty("os.name").toLowerCase();


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


    public BClass [] addClasses(BPackage aPackage){
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
    
    public void getPMDPath(){
        File executable = new File(projectDir, "pmd-bin-6.9.0");
        if (isWindows()) {
            executable = new File(executable, "bin/pmd.bat");
        } else {
            executable = new File(executable, "bin/run.sh");
        }
        PMDPath = executable.getAbsolutePath();

    }
    public void actionPerformed(ActionEvent event){
        PMD_Runner runner;
        if(useDefault == false){
            getPMDPath();
            runner = PMD_Runner_Factory.getPMDRunner(PMDPath);

        }
        else
            runner = PMD_Runner_Factory.getPMDRunner();

        StringBuilder msg = new StringBuilder("Any problems found are displayed below:");
        msg.append(LINE_SEPARATOR);
        for(int i = 0; i < filenames.length; i++){
            String output = runner.runText(filePaths[i]);
            msg.append(output);
            msg.append(LINE_SEPARATOR);
        }

        JOptionPane.showMessageDialog(null, msg);
    }

    private boolean isWindows(){
        return (OS.indexOf("win") >= 0);
    }
}
