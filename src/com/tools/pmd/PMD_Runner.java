package com.tools.pmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Allows user to implement a custome path to PMD
 */
public class PMD_Runner{
    /** Path to PMD installation. */
    private String pathToPMD;
    private String checkToUse = "java-quickstart";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * Constructor
     *
     * @param pathToPMD Absolute path to pmd installation.
     */
    PMD_Runner(String pathToPMD){
        this.pathToPMD = pathToPMD;
    }

    PMD_Runner(String pathToPMD, String checkToUse){
        this.pathToPMD = pathToPMD;
        this.checkToUse = checkToUse;
    }

    /**
     * Constructs a text report for a given file
     * @param filepath   absolute path to java file to be evaluated
     */
    public String runText(String filepath){
        return run(filepath, "text");
    }

    /**
     * Constructs a html report for a given file
     * @param filepath   absolute path to java file to be evaluated
     */
    public String runHTML(String filepath){
        return run(filepath, "html");
    }

    /**
     * Makes the command to enter into PMD CLI. Command should be comma-deliminated to ensure compatibility with windows paths
     * @param filepath   path to the file to be evaluated
     * @param outputFormat   format for PMD to return as. See PMD documentation for options
     * @return   comma delimited PMD command
     */
    String makeCommand(String filepath, String outputFormat){
        return pathToPMD + ",-format," + outputFormat + ",-R," + checkToUse + ",-version,1.8,-language,java,-d," + filepath;
    }

    // Pulls in and runs the command.
    private String run(String filepath, String outputFormat){
        String myCommand = makeCommand(filepath, outputFormat);
        ProcessBuilder pb = new ProcessBuilder(myCommand.split(","));
        pb.redirectErrorStream(false);
        final StringBuilder output = new StringBuilder();
        try{
            final Process p = pb.start();

            Thread reader = new Thread(new Runnable() {
                public void run() {
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String s;
                    try {
                        while ((s = stdInput.readLine()) != null ){
                            output.append(s);
                            output.append(LINE_SEPARATOR);
                        }
                    } catch (IOException e) {
                        output.append(e.toString());
                        e.printStackTrace();
                    } finally {
                        try { stdInput.close(); } catch (IOException e) { /* quiet */ }
                    }
                }
            });
            reader.setDaemon(true);
            reader.start();
            p.waitFor();
        }catch(IOException | InterruptedException e){
            output.append(e);
            e.printStackTrace();
        }
        return output.toString();
    }

    public static void main(String[]args){
        String pathToPMD = args[0];
        String FileName = args[1];
        PMD_Runner runner = new PMD_Runner(pathToPMD);
        System.out.println(runner.run(FileName, "text"));
    }
}

