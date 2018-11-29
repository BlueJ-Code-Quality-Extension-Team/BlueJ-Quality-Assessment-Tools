package com.tools.checkstyle;

import com.bluejmanager.BlueJManager;

import java.io.InputStream;

public class Settings {
    /** configuration file name key */
    private static final String CONFIG_FILE_NAME_KEY = "checkstyle.configfile";

    /** properties file name key */
    private static final String PROPS_FILE_NAME_KEY = "checkstyle.propsfile";

    /** default configuration file */
    private static final String DEFAULT_CHECKS_FILE = "default_checks.xml";

    /**
     * Retrieves the extension configuration file property value.
     * @return the name of the extension configuration file.
     */
    public static String getConfigFileName()
    {
        BlueJManager manager = BlueJManager.getInstance();

        String checkFile = manager.getExtensionPropertyString(
                CONFIG_FILE_NAME_KEY,
                DEFAULT_CHECKS_FILE);

        return checkFile;
    }

    /**
     * Opens a stream connected to the specified configuration file.
     * @return An input stream connected to the resource, or null if the
     * resource cannot be opened.
     */
    public static InputStream getConfigStream()
    {
        String checkFile = Settings.getConfigFileName();

        BlueJManager manager = BlueJManager.getInstance();
        String description = "checkstyle configuration file";
        InputStream inputStream = manager.getResourceStream(
                checkFile,
                DEFAULT_CHECKS_FILE,
                description
        );

        return inputStream;
    }

    /**
     * Determines the name of the extension properties file.
     * @return the name of the extension properties file.
     */
    public static String getPropsFileName()
    {
        BlueJManager manager = BlueJManager.getInstance();

        String propFile = manager.getExtensionPropertyString(
                PROPS_FILE_NAME_KEY,
                null);

        return propFile;
    }

    /**
     * Opens a stream connected to the specified configuration file.
     * @return An input stream connected to the resource, or null if the
     * resource cannot be opened.
     */
    public static InputStream getPropertyStream()
    {
        String propFile = Settings.getPropsFileName();

        BlueJManager manager = BlueJManager.getInstance();
        String description = "checkstyle properties file";
        InputStream inputStream = manager.getResourceStream(
                propFile,
                null,
                description
        );

        return inputStream;
    }

    /**
     * Saves the name of the configuration file.
     * @param aName the name of the configuration file.
     */
    public static void saveConfigFileName(String aName)
    {
        BlueJManager manager = BlueJManager.getInstance();

        String old = manager.getExtensionPropertyString(
                CONFIG_FILE_NAME_KEY,
                null);

        if (old != null && !old.equals(""))
        {
            manager.getMissingResources().remove(old);
        }
        manager.setExtensionPropertyString(CONFIG_FILE_NAME_KEY, aName);
    }

    /**
     * Saves the name of the properties file.
     * @param aName the name of the properties file.
     */
    public static void savePropsFileName(String aName)
    {
        BlueJManager manager = BlueJManager.getInstance();

        String old = manager.getExtensionPropertyString(
                PROPS_FILE_NAME_KEY,
                null);

        if (old != null && !old.equals(""))
        {
            manager.getMissingResources().remove(old);
        }
        manager.setExtensionPropertyString(PROPS_FILE_NAME_KEY, aName);
    }
}
