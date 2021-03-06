////////////////////////////////////////////////////////////////////////////////
// BlueJ Checkstyle extension:
//    Checks Java source code for adherence to a set of rules.
// Copyright (C) 2003-2004  Rick Giles
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////
package com.bluejmanager;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bluej.extensions.BPackage;
import bluej.extensions.MenuGenerator;

import com.tools.checkstyle.CheckstyleMenuListener;
import com.tools.pmd.PMD_Menu_Listener;

/**
 * Manages the Checkstyle extension menu item
 * @author Rick Giles
 * @version $Id: ExtensionMenu.java,v 1.5 2007/08/19 03:13:53 stedwar2 Exp $
 */
public class ExtensionMenu extends MenuGenerator
{
    /**
     * @see bluej.extensions.MenuGenerator#getToolsMenuItem(bluej.extensions.BPackage)
     */
    public JMenuItem getToolsMenuItem(BPackage aPackage) {
        final JMenu extMenu = new JMenu("Quality Tools");

        final JMenuItem checkMenu = new JMenuItem("Checkstyle");
        checkMenu.addActionListener(new CheckstyleMenuListener());

        final JMenuItem pmdMenu = new JMenuItem("PMD");
        pmdMenu.addActionListener(new PMD_Menu_Listener());

        extMenu.add(checkMenu);
        extMenu.add(pmdMenu);

        return extMenu;
    }

    /**
     * @see bluej.extensions.MenuGenerator#getMenuItem()
     * @deprecated
     * Deprecated as of BlueJ 1.3.5.
     * Added for compatibility with BlueJ 1.3.0.
     */
    public JMenuItem getMenuItem() {
        final JMenuItem item = new JMenuItem("Checkstyle");
        item.addActionListener(new CheckstyleMenuListener());
        return item;
    }
}
