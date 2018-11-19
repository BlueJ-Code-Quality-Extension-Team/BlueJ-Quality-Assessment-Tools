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

import java.awt.Frame;
import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import bluej.extensions.BClass;
import bluej.extensions.BPackage;
import bluej.extensions.BProject;
import bluej.extensions.BlueJ;
import bluej.extensions.ClassNotFoundException;
import bluej.extensions.PackageNotFoundException;
import bluej.extensions.ProjectNotOpenException;

/**
 * Manages the BlueJ object for a BlueJ extension
 * @author Rick Giles
 * @version $Id: BlueJManager.java,v 1.6 2007/08/19 03:13:53 stedwar2 Exp $
 */
public final class BlueJManager
{
    /** singleton */
    private static BlueJManager sInstance = null;

    /** Base name for extension properties */
    private static final String EXT_BASE = "qualityToolsExtension";

    /** BlueJ application proxy */
    private BlueJ mBlueJ = null;

    /**
     * Which resource files have failed to open successfully? Used to
     * remember which ones we've issued error messages about so that the
     * same messages don't pop up over and over.
     */
    private Set<String> mMissingResources = new HashSet<String>();

    /** A properties facade over mBlueJ.  Initialized lazily. */
    private BlueJPropertiesAdapter mBlueJPropertiesAdapter;

    /**
     * Returns the singleton BlueJManager.
     * @return the singleton BlueJManager.
     */
    public static synchronized BlueJManager getInstance()
    {
        if (sInstance == null)
        {
            sInstance = new BlueJManager();
        }
        return sInstance;
    }

    /**
     * Prevent users from constructing BlueJManager objects.
     */
    private BlueJManager()
    {
        // All data is initialized in declarations
    }

    /**
     * Sets the BlueJ application proxy of this BlueJManager.
     * @param aBlueJ the new  BlueJ application proxy.
     */
    public void setBlueJ(BlueJ aBlueJ)
    {
        mBlueJ = aBlueJ;
        if (mBlueJPropertiesAdapter != null)
        {
            mBlueJPropertiesAdapter.setBlueJ(aBlueJ);
        }
    }

    /**
     * Returns the classes for all valid open projects..
     * @return the set of classes for all valid open projects.
     * @throws ProjectNotOpenException if project is not open.
     * @throws PackageNotFoundException if package is not found.
     */
    public Set<BClass> getBClasses()
        throws ProjectNotOpenException,
               PackageNotFoundException
    {
        final Set<BClass> result = new HashSet<BClass>();
        final BProject[] projects = mBlueJ.getOpenProjects();
        for (int i = 0; i < projects.length; i++)
        {
            result.addAll(getBClasses(projects[i]));
        }
        return result;
    }

    /**
     * Returns classes for a BlueJ Project.
     * @param aProject BlueJ project.
     * @return The classes for aProject. If aProject is not open, returns
     * an empty set.
     * @throws ProjectNotOpenException if a project is not open.
     * @throws PackageNotFoundException if a package is not found.
     */
    public Set<BClass> getBClasses(BProject aProject)
        throws ProjectNotOpenException,
               PackageNotFoundException
    {
        final Set<BClass> result = new HashSet<BClass>();

        final BPackage[] packages = aProject.getPackages();
        for (int i = 0; i < packages.length; i++)
        {
            result.addAll(getBClasses(packages[i]));
        }
        return result;
    }

    /**
     * Returns classes for a BlueJ package.
     * @param aPackage BlueJ package.
     * @return The classes for aPackage. If aPackage is not valid, returns
     * an empty set.
     * @throws ProjectNotOpenException if a project is not open.
     * @throws PackageNotFoundException if a package is not found.
     */
    public Set<BClass> getBClasses(BPackage aPackage)
        throws ProjectNotOpenException,
               PackageNotFoundException
    {
        final BClass[] classes = aPackage.getClasses();
        final Set<BClass> result = new HashSet<BClass>(classes.length);
        for (int i = 0; i < classes.length; i++)
        {
            result.add(classes[i]);
        }
        return result;
    }

    /**
     * Returns the files for all valid open projects. All project classes
     * must be compiled.
     * @return the files for all valid open projects.
     * @throws ClassNotFoundException if a class is not found.
     * @throws ProjectNotOpenException if a project is not open.
     * @throws PackageNotFoundException if a package is not found.
     */
    public Set<File> getFiles()
            throws ClassNotFoundException,
            ProjectNotOpenException,
            PackageNotFoundException
    {
        final Set<File> result = new HashSet<File>();
        final Set<BClass> classes = getBClasses();
        for (Iterator<BClass> iter = classes.iterator(); iter.hasNext();)
        {
            BClass theClass = iter.next();
            final BPackage thePackage = theClass.getPackage();
            final BProject theProject = thePackage.getProject();
            final String projectDirName =
                    theProject.getDir().toString().replaceAll("\\\\", "/");
            String className = theClass.getJavaClass().getName();
            className = className.replaceAll("\\.", "/");
            final String fullName =
                    projectDirName + "/" + className + ".java";
            final File file = new File(fullName);
            if (file.exists())
            {
                result.add(file);
            }
        }
        return result;
    }

    /**
     * Finds the current frame of the BlueJ application.
     * @return the current frame of the BlueJ application.
     */
    public Frame getCurrentFrame()
    {
        try
        {
            // Try to return the current package's frame first, if possible.
            // This is in an exception handler because getFrame()
            // may throw declared exceptions.  It is also possible for
            // getCurrentPackage() to return null, but as long as we have to
            // use a try/catch block, we'll just let it handle that case, too.
            return mBlueJ.getCurrentPackage().getFrame();
        }
        catch (Exception e)
        {
            return mBlueJ.getCurrentFrame();
        }
    }

    /**
     * Finds the current frame of the BlueJ application.
     * @return the current frame of the BlueJ application.
     */
    public Properties getPropertiesAdapter()
    {
        if (mBlueJPropertiesAdapter == null)
        {
            mBlueJPropertiesAdapter =
                new BlueJPropertiesAdapter(mBlueJ, System.getProperties());
        }
        return mBlueJPropertiesAdapter;
    }

    /**
     * Opens a stream connected to a named resource, which could resolve to
     * a local file resource, a resource embedded in a jar file on the
     * classpath, or a URL.  If the resource cannot be opened, a warning
     * message dialog will be shown, unless both the default and description
     * are omitted.
     * @param name The name/location of the resource to open.  Cannot be null.
     * @param defaultName The optional name/location of a secondary resource
     * to use if the named resource cannot be located or opened.  Can be null
     * if there is not backup resource to use.
     * @param description An optional brief description of the resource to
     * use in error messages.
     * @return An input stream connected to the resource, or null if the
     * resource (and, if specified, the secondary resource) cannot be opened.
     */
    public InputStream getResourceStream(
        String name, String defaultName, String description)
    {
        InputStream result = null;
        if (name != null && !name.isEmpty())
        {
            result = getResourceStream(name);
            if (result == null && defaultName != null && description != null)
            {
                missingResourceMessage(name, defaultName, description);
            }
        }
        if (result == null && defaultName != null && !defaultName.equals(name))
        {
            result = getResourceStream(defaultName);
            if (result == null)
            {
                missingResourceMessage(defaultName, null, description);
            }
        }
        return result;
    }


    /**
     * Pops up a warning message indicating that a named resource cannot
     * be found.
     * @param name The name/location of the resource that is missing.
     * Cannot be null.
     * @param defaultName If non-null, the phrase "Using default: ..." will be
     * added to the message, using this name.
     * @param description An optional brief description of the resource to
     * use in error messages.
     */
    private void missingResourceMessage(
        String name, String defaultName, String description)
    {
        if (!mMissingResources.contains(name))
        {
            if (description == null)
            {
                description = "resource";
            }
            javax.swing.JOptionPane.showMessageDialog(
                getCurrentFrame(),
                "Cannot find specified " + description + ":\n"
                + "  \"" + name + "\"\nas a file, on the classpath, "
                + "or as a URL."
                + ((defaultName != null)
                    ? ("\nUsing default: " + defaultName)
                    : "")
                + ".",
                "Checkstyle Extension",
                javax.swing.JOptionPane.WARNING_MESSAGE );
            mMissingResources.add(name);
        }
    }


    /**
     * Tries to resolve a resource name by checking for a file, searching
     * the classpath, or verifying the reachability of a URL.
     * @param resourceLocation The name/location of the resource to resolve
     * @return The full name of the resource as a file name or URL if it
     * could be located, or null if no such resource could be found.
     */
    private InputStream getResourceStream(String resourceLocation)
    {
        if (resourceLocation == null || resourceLocation.equals(""))
        {
            return null;
        }

        // Search for a file name first (absolute or relative)
        try
        {
            File file = new File(resourceLocation);
            // If absolute or relative to system cwd, try relative to
            // BlueJ lib dir
            if (!file.exists())
            {
                file = new File(mBlueJ.getSystemLibDir(), resourceLocation);
            }
            // or relative to BlueJ lib/extensions
            if (!file.exists())
            {
                file = new File(mBlueJ.getSystemLibDir() + "/extensions",
                    resourceLocation);
            }
            if (file.exists())
            {
                return new FileInputStream(file);
            }
        }
        catch (Exception e)
        {
            // ignore it and try the next option
        }

        // Now search the classpath of this class
        try
        {
            return getClass().getClassLoader().getResource(resourceLocation)
                .openStream();
        }
        catch (Exception e)
        {
            // ignore it and try the next option
        }

        // Now look for a URL instead
        try
        {
            return new java.net.URL(resourceLocation).openStream();
        }
        catch (Exception e)
        {
            // ignore it and try the next option
        }
        return null;
    }

    /**
     * Get a property from the extension property store.
     * @param propertyName The property to fetch
     * @param def The default if property is not found
     * @return The fetched property or default
     */
    public String getExtensionPropertyString(String propertyName, String def){
        String prop = EXT_BASE + "." + propertyName;
        return mBlueJ.getExtensionPropertyString(prop, def);
    }

    /**
     * Stores a property in the extension property store.
     * @param propertyName The name of the property
     * @param value The value of the property
     */
    public void setExtensionPropertyString(String propertyName, String value){
        String prop = EXT_BASE + "." + propertyName;
        mBlueJ.setExtensionPropertyString(prop, value);
    }

    /**
     * Exposes the missing resources list
     * @return The missing resources list
     */
    public Set<String> getMissingResources(){
        return mMissingResources;
    }
}
