package com.tools.pmd;

import bluej.extensions.event.CompileListener;
import bluej.extensions.event.CompileEvent;
import bluej.extensions.*;

import com.bluejmanager.*;

/**
 * Listens for BlueJ compile events. On compile succeed starts a new 
 * PMD_Runnable_Thread
 */

public class PMD_Compile_Listener implements CompileListener{
  /** Stubs for future use */ 
  public void compileError(CompileEvent event){
    
  }
  
  /** Stubs for future use */ 
  public void compileFailed(CompileEvent event){
    
  }
  
  /** Stubs for future use */ 
  public void compileStarted(CompileEvent event){
    
  }
  
  /** On successful compile generate a PMD report */
  public void compileSucceeded(CompileEvent event){
    Thread thread = new Thread(new PMD_Runnable_Thread());
    thread.start();
  }
  
  /** Stubs for future use */ 
  public void compileWarning(CompileEvent event){
    
  }
  
}
