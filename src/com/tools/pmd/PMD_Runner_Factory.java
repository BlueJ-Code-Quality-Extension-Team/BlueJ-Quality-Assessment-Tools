package com.tools.pmd;

/**
 * Factory for PMD_Runner interface
 */
public class PMD_Runner_Factory{
    /*
     * Returns a custom PMD runner with the supplied path
     */
    public static PMD_Runner getPMDRunner(String pathToPMD){
        return new PMD_Custom_Runner(pathToPMD);
    }
}
