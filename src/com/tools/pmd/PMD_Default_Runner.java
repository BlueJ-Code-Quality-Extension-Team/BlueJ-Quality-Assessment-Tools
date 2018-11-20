package com.tools.pmd;

import net.sourceforge.pmd.PMD;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;

public class PMD_Default_Runner implements PMD_Runner{
    final private String ruleSets = "category/java/design.xml,category/java/bestpractices.xml" +
                                    ",category/java/documentation.xml,category/java/performance.xml,category/java/multithreading.xml";
    private String javaVersionNumber;
    private String prefix = "pmd-default-runner";
    private String suffix = ".tmp";
    public String run(File file, String format){
        final String lineSeparator = System.getProperty("line.separator");
        String report;
        PMD pmd = new PMD();
        try {
            File output;
            output = File.createTempFile(prefix, suffix);
            System.out.println(output.getAbsolutePath());
            String [] args = {"-d ", file.getAbsolutePath(), " -no-cache ", " -f ", format, " -language ", " java ", " -version ", " 1.8", " -R ", ruleSets, " -r ", output.getAbsolutePath()};
            PMD.run(args);
            BufferedReader br = new BufferedReader(new FileReader(output));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            int it = 0;
            while((line = br.readLine()) != null){
                stringBuilder.append(line);
                stringBuilder.append(lineSeparator);
                it ++ ;
            }
            output.delete();
            report = stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
            report = e.getStackTrace().toString();
        }
        return report;
    }

    public String runHTML(String filename){
        File file = new File(filename);
        return run(file, "HTML");
    }
    public String runText(String filename){
        File file = new File(filename);
        return run(file, "text");
    }
    public static void main(String[] args){

        PMD_Default_Runner runner = new PMD_Default_Runner();
        BufferedReader br;
        try {
            br  = new BufferedReader(new FileReader("HelloWorld.java"));

            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while((line = br.readLine()) != null){
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            System.out.println("HelloWorld:\n" + stringBuilder.toString());

            System.out.println("Errors found for: HelloWorld");
            System.out.println(runner.runText(new File("HelloWorld.java").toString()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
