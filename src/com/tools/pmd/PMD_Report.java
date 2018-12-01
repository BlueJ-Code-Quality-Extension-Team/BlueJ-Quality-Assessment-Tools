package com.tools.pmd;

/** Used by PMD_Report_Builder to construct the PMD message */
public class PMD_Report {
  private StringBuilder msg;
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  private static final String EXPLANATION = "Any problems found are displayed below:";
  
  /** Constructor, readys the StringBuilder with EXPLANATION as as header */
  public PMD_Report(){
    msg = new StringBuilder(EXPLANATION);
    msg.append(LINE_SEPARATOR);
  }
  
  /** Adds a line to the message */
  public void appendMessage(String input){
    msg.append(input);
    msg.append(LINE_SEPARATOR);
  }

  /** Returns the message */
  public String getMessage(){
    return msg.toString();
  }
  
}
