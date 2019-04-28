package com.gameplaystudio.combination.util;

import com.gameplaystudio.combination.util.exceptions.InvalidSettingsInFile;
import org.apache.log4j.Level;

import java.io.*;
import java.util.Properties;

import static com.gameplaystudio.combination.CombinationGame.logger;

/**
 * This class contains all the configurable attributes of the game<br>
 * <i>this class can't be instanced</i><br>
 * all the methods and attributes are statics in order to access them in the whole project<br>
 */
public final class Config{
    /**
     *Name of the config file
     */
    private final static String configFileName = "config.properties";
    /**
     * Path to the config folder
     */
    private final static String configPath = System.getProperty("user.dir") + "/";

    /**
     * Attribute used to set the number of allowed try in a game mode with a limited number of tries<br>
     * It's set to 5 by default<br>
     * <i>This attribute can be updated from a config file</i>
     *
     * @see #updateSettingsFromFile()
     */
    public static int nbAllowedTry = 5;
    /**
     * Attribute used to set the number of digit a combinations will have in all Game Modes<br>
     * It's set to 4 by default<br>
     * <i>This attribute can be update from a config file</i>
     *
     * @see #updateSettingsFromFile()
     */
    public static int combinationLength = 4;
    /**
     * Boolean used to toggle the developer mode<br>
     * It's set to <code>false</code> by default
     * <i>This attribute can be update from a config file</i>
     *
     * @see #updateSettingsFromFile()
     */
    public static boolean devMode = false;

    /**
     * Private constructor to block instantiation
     */
    private Config()
    {}

    /**
     * <p>Method that check for the file config.properties at the Root of the project</p>
     * <p>If the file is found :<br>
     * the method actualise the associated class attributes<br>
     * If the file is not found :<br>
     * The method create config.properties and write default values in it</p>
     *
     * @throws InvalidSettingsInFile when a property contains incorrect information
     * @see #nbAllowedTry
     * @see #combinationLength
     */
    public static void updateSettingsFromFile() { //TODO Add dev option XML ?
        logger.trace("Update settings");

        String path = configPath + configFileName;
        int nbAllowedTryMin = 1;
        int combinationLengthMin = 1;
        int combinationLengthMax = 20;

        try {
            InputStream input = new FileInputStream(path);
            Properties prop = new Properties();


            // load a properties file
            prop.load(input);

            int temp_nbAllowedTry = Integer.parseInt(prop.getProperty("nbAllowedTry"));
            int temp_combinationLength = Integer.parseInt(prop.getProperty("combinationLength"));
            boolean temp_devMode = Boolean.parseBoolean(prop.getProperty("devMode"));

            if(temp_combinationLength >= combinationLengthMin && temp_combinationLength <= combinationLengthMax && temp_nbAllowedTry >= nbAllowedTryMin){
                nbAllowedTry = temp_nbAllowedTry;
                combinationLength = temp_combinationLength;
            } else {
                throw new InvalidSettingsInFile();
            }

            if (temp_devMode != devMode){
                if (temp_devMode){
                    logger.info("Dev mode has been enabled");
                    logger.setLevel(Level.DEBUG);
                } else {
                    logger.setLevel(Level.WARN);
                    logger.info("Dev mode has been disabled");
                }
            }

            logger.info("Settings has been updated from file");

        } catch (NumberFormatException | InvalidSettingsInFile | FileNotFoundException e){
            if (e instanceof FileNotFoundException){
                logger.warn("Unable to find " + configFileName);
                logger.warn("Creating " + path + " with default parameters");
            } else{
                logger.warn(configFileName + " contains incorrect parameters");
                logger.warn("Combination length min:" + combinationLengthMin + " max:" + combinationLengthMax + " nbTryAllowed min:" + nbAllowedTryMin + " devMode = true or false");
            }

            try {
                Properties prop = new Properties();
                OutputStream output = new FileOutputStream(path);

                // set default values
                prop.setProperty("nbAllowedTry", Integer.toString(5));
                prop.setProperty("combinationLength", Integer.toString(4));
                prop.setProperty("devMode", Boolean.toString(false));

                // save properties to the config path
                prop.store(output, null);

                InputStream input = new FileInputStream(path);

                prop.load(input);

                logger.warn("Settings file has been reset");

            } catch (IOException io) {
                logger.error("Unhandled exception : Config file " + path + " can't be created");
            }

        } catch (IOException io){
            logger.fatal("An unhandled exception has occurred");
            io.printStackTrace();
        }

    }
}
