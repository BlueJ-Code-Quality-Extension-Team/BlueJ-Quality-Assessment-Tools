package com.tools.pmd;

import java.io.File;

import java.nio.file.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;

import net.sourceforge.pmd.PMDConfiguration;
import bluej.extensions.*;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

//TODO: Implement Properties and fix file path for command
public class PMDaction implements ActionListener {
    BProject bProject;
    String projectDir;
    String PMDPath;
    File executable;
    String [] filenames;
    String [] filePaths;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static String OS = System.getProperty("os.name").toLowerCase();


    public PMDaction(BPackage aPackage){
        try{
            this.bProject = aPackage.getProject();
            projectDir=bProject.getDir().toString();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        FileSystem fs = FileSystems.getDefault();
        PathMatcher pmdMatcher = fs.getPathMatcher("glob:pmd*");
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

            executable = new File(projectDir, "pmd-bin-6.9.0");
            if (isWindows()) {
                executable = new File(executable, "bin/pmd.bat");
            } else {
                executable = new File(executable, "bin/run.sh");
            }
            PMDPath = executable.getAbsolutePath();
        }
    }

    public void actionPerformed(ActionEvent event){
        PMDrunner runner = new PMDrunner(PMDPath);
        StringBuilder msg = new StringBuilder("Any problems found are displayed below:");
        msg.append(LINE_SEPARATOR);
        for(int i = 0; i < filenames.length; i++){
            String output = runner.run(filePaths[i]);
            msg.append(output);
            msg.append(LINE_SEPARATOR);
        }

        JOptionPane.showMessageDialog(null, msg);
    }

    private boolean isWindows(){
        return (OS.indexOf("win") >= 0);
    }
}
