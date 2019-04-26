package com.gameplaystudio.combination.util;

import com.gameplaystudio.combination.util.exceptions.InvalidSettingsInFile;

import java.io.*;
import java.util.Properties;

import static com.gameplaystudio.combination.CombinationGame.logger;

/**
 * This class contains all the configurable attributes of the game<br>
 * this class can't be instanced<br>
 * all the methods and attributes are statics<br>
 * in order to access them in the whole project<br>
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
     * <i>This attribute can be updated from a config file</i>
     *
     * @see #updateSettingsFromFile()
     */
    public static int nbAllowedTry;
    /**
     * Attribute used to set the number of digit a combinations will have in all Game Modes<br>
     * <i>This attribute can be update from a config file</i>
     *
     * @see #updateSettingsFromFile()
     */
    public static int combinationLength;

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
     * @throws NumberFormatException when a property value can't be parsed to an int
     * @throws FileNotFoundException when the file do not exist for the specified path
     * @throws InvalidSettingsInFile when a property contains incorrect information
     * @see #nbAllowedTry
     * @see #combinationLength
     */
    public static void updateSettingsFromFile() {
        logger.info("Update settings");
        
        String path = configPath + configFileName;
        var nbAllowedTryMin = 1;
        var combinationLengthMin = 1;
        var combinationLengthMax = 20;

        try {
            InputStream input = new FileInputStream(path);
            Properties prop = new Properties();


            // load a properties file
            prop.load(input);

            var tempNbAllowedTry = Integer.parseInt(prop.getProperty("nbAllowedTry"));
            var tempCombinationLength = Integer.parseInt(prop.getProperty("combinationLength"));

            if(tempCombinationLength >= combinationLengthMin && tempCombinationLength <= combinationLengthMax && tempNbAllowedTry >= nbAllowedTryMin){
                // get the properties values and update the associated attributes
                nbAllowedTry = tempNbAllowedTry;
                combinationLength = tempCombinationLength;
            } else {
                throw new InvalidSettingsInFile();
            }
            logger.info("Settings has been updated from file");

        } catch (NumberFormatException | InvalidSettingsInFile | FileNotFoundException e){
            if (e instanceof FileNotFoundException){
                logger.warn("Unable to find " + configFileName);
                logger.warn("Creating " + path + " with default values");
            } else if (e instanceof NumberFormatException){
                logger.warn("The config file " + configFileName + " must contain only integers for settings values");
                logger.warn("Combination length min:" + combinationLengthMin + " max:" + combinationLengthMax + " and nbTryAllowed min:" + nbAllowedTryMin);
                logger.warn("Resetting settings attributes to defaults values");
            } else {
                logger.warn("The config file " + configFileName + " contains incorrect information");
                logger.warn("Combination length min:" + combinationLengthMin + " max:" + combinationLengthMax + " and nbTryAllowed min:" + nbAllowedTryMin);
                logger.warn("Resetting settings attributes to defaults values");
            }

            try {
                Properties prop = new Properties();
                OutputStream output = new FileOutputStream(path);

                // set default values
                prop.setProperty("nbAllowedTry", Integer.toString(5));
                prop.setProperty("combinationLength", Integer.toString(4));

                // save properties to the config path
                prop.store(output, null);

                InputStream input = new FileInputStream(path);

                prop.load(input);

                logger.info("Settings file has been reset");

            } catch (IOException io) {
                logger.error("Unhandled exception : Config file " + path + " can't be created");
            }

        } catch (IOException io){
            logger.fatal("An unhandled exception has occurred");
            io.printStackTrace();
        }

    }
}
