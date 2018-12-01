package com.tools.pmd;

import com.bluejmanager.*;

import java.util.Set;
import java.io.File;

class PMD_Runnable_Thread implements Runnable{

    public void run(){
      QualityAssessmentExtension.getInstance().mPMDReport = new PMD_Report();
        try{
            Set<File> files = BlueJManager.getInstance().getFiles();
            PMD_Report report = PMD_Report_Builder.getInstance().generatePMDReport(files);
            QualityAssessmentExtension.getInstance().mPMDReport = report;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
