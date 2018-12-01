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

public class PMD_Report_Builder {
  
  /** singleton */
  private static PMD_Report_Builder PMDReportBuilder;
  
  private String PMDPath;
  private static final String OS = System.getProperty("os.name").toLowerCase();
  
  private PMD_Report_Builder(){
    
  }
  
  public static PMD_Report_Builder getInstance(){
    if(PMDReportBuilder == null){
      PMDReportBuilder = new PMD_Report_Builder();
    } 
    
    return PMDReportBuilder;
  }
  
  public PMD_Report generatePMDReport(){
    PMD_Report report = new PMD_Report();
    getDefaultPMDPath();
    
    Set<File> files = null;
    
    try{
      files = BlueJManager.getInstance().getFiles();
    }
    catch(Exception e){
      e.printStackTrace();
    }
    
    getDefaultPMDPath();

    if(PMDPath == null){
      String warning = "Error: Please ensure that PMD is installed correctly";
      JOptionPane.showMessageDialog(null,warning);
      return report;
    }

    PMD_Runner runner;

    runner = PMD_Runner_Factory.getPMDRunner(PMDPath);
    if(files != null){
      for(File f : files){
        String output = runner.runText(f.toString());
        report.appendMessage(output);
      }
    }
    
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