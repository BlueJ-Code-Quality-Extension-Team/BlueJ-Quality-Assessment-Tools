package com.tools.pmd;

/** Used by PMD_Report_Builder to construct the PMD message */
public class PMD_Report {
  private StringBuilder msg;
  private boolean finalized;
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  private static final String EXPLANATION = "Any problems found are displayed below:";
  private static final String NOT_FINISHED = "The PMD report is not ready yet. "+
  "It will be ready after you compile and after the PMD checks are finished.";
  
  /** Constructor, readys the StringBuilder with EXPLANATION as as header */
  public PMD_Report(){
    msg = new StringBuilder(EXPLANATION);
    msg.append(LINE_SEPARATOR);
    finalized = false;
  }
  

  /** Adds a line to the message if not finalized */
  public void appendMessage(String input){
    if(!finalized){
      msg.append(input);
      msg.append(LINE_SEPARATOR);
    }
  }

  /** Returns NOT_FINISHED if report is not finalized, otherwise returns message */
  public String getMessage(){
    if(!finalized){
      return NOT_FINISHED;
    } else {
      return msg.toString();
    }
  }
  
  /** Finalize the report so it is ready to display. It cannot be edited further. */
  public void finalize(){
    finalized = true;
  }
}
