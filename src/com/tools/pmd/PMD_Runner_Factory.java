package com.tools.pmd;

public class PMD_Runner_Factory{

    public static PMD_Runner getPMDRunner(String pathToPMD){
        return new PMD_Custom_Runner(pathToPMD);
    }
    public static PMD_Runner getPMDRunner(){
        return new PMD_Default_Runner();
    }
}
