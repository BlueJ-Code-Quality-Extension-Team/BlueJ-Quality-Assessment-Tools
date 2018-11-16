package com.tools.pmd;

import bluej.extensions.*;
import bluej.extensions.event.*;
import java.net.URL;

public class PMDExtension extends Extension{
    private BlueJ bluej;
    public void startup(BlueJ bluej){
        this.bluej = bluej;
        bluej.setMenuGenerator(new PMDMenuExtension());
        System.out.println("SetMenuGen");
    }

    public String getVersion () {
        return "1.0";
    }

    public boolean isCompatible(){
        return true;
    }

    public String getName () {
        return ("PMD");
    }

    public String getDescription () {
        return "PMD extension - finds unused code, empty blocks, and more!";
    }

    public URL getURL () {
        try {
            return new URL("https://pmd.github.io/");
        } catch ( Exception e ) {
            System.out.println ("Can't get PMD extension URL: "+e.getMessage());
            return null;
        }
    }
}
