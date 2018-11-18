package com.tools.pmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//
public class PMD_Custom_Runner implements PMD_Runner{
    String pathToPMD;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public PMD_Custom_Runner(String pathToPMD){
        this.pathToPMD = pathToPMD;
    }

    public String runText(String filename){
        return run(filename, "text");
    }
    public String runHTM(String filename){
        return run(filename, "htm");
    }
    
    //output format should be "text" or "htm"
    private String run(String FileName, String outputFormat){
        String myCommand = pathToPMD + ",-format," + outputFormat + ",-R,java-quickstart,-version,1.8,-language,java,-d," + FileName;
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
        }catch(IOException e){
            output.append(e);
            e.printStackTrace();
        }catch(InterruptedException e){
            output.append(e);
            e.printStackTrace();
        }
        return output.toString();
    }
    public static void main(String[]args){
        String pathToPMD = args[0];
        String FileName = args[1];
        PMD_Custom_Runner runner = new PMD_Custom_Runner(pathToPMD);
        System.out.println(runner.run(FileName, "text"));
    }
}

