package com.puppycrawl.tools.checkstyle.plugins.bluej;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import bluej.extensions.*;

public class PMDMenuExtension extends MenuGenerator{
    
    @Override
    public JMenuItem getToolsMenuItem(BPackage aPackage){
        final JMenuItem item = new JMenuItem("PMD");
        item.addActionListener(new PMDaction(aPackage));
        return item;
    }

    class reportAction extends AbstractAction {
        public reportAction(){
            putValue(AbstractAction.NAME, "Tool");

        }
        public void actionPerformed(ActionEvent event){
            System.out.println("ToolPress");
        }
    }
}
