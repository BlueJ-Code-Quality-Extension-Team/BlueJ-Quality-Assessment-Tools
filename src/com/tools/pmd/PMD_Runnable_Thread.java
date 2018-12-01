package com.tools.pmd;

import com.bluejmanager.*;

class PMD_Runnable_Thread implements Runnable{

  public void run(){
    PMD_Report report = PMD_Report_Builder.getInstance().generatePMDReport();
    QualityAssessmentExtension.getInstance().mPMDReport = report;
  }
}