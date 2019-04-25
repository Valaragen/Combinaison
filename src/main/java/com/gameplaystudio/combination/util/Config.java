package com.gameplaystudio.combination.util;

import java.io.*;
import java.util.Properties;

import static com.gameplaystudio.combination.CombinationGame.logger;

public final class Config{
    private final static String fileName = "config.properties";

    /**
     * Attribute used to set the number of allowed try in a game mode with a limited number of tries<br>
     * <i>This attribute is extracted from a file</i>
     *
     * @see #updateSettingsFromFile()
     */
    public static int nbAllowedTry;
    /**
     * Attribute used to set the number of digit a combinations will have in all Game Modes<br>
     * <i>This attribute is extracted from a file</i>
     *
     * @see #updateSettingsFromFile()
     */
    public static int combinationLength;

    private Config()
    {}

    /**
     * <p>Method that check for the file config.properties at the Root of the project</p>
     * <p>If the file is found :<br>
     * the method actualise the associated class attributes<br>
     * If the file is not found :<br>
     * The method create config.properties and write default values in it</p>
     *
     * @see #nbAllowedTry
     * @see #combinationLength
     */
    public static void updateSettingsFromFile() { //TODO Create a custom exception to limit the usage of the file. user can't set combination length > 10
        String path = System.getProperty("user.dir") + "/" + fileName;

        try {
            InputStream input = new FileInputStream(path);
            Properties prop = new Properties();


            // load a properties file
            prop.load(input);

            // get the properties values and update the associated attributes
            nbAllowedTry = Integer.parseInt(prop.getProperty("nbAllowedTry"));
            combinationLength = Integer.parseInt(prop.getProperty("combinationLength"));

        } catch (NumberFormatException | FileNotFoundException e){
            Properties prop = new Properties();

            if (e instanceof FileNotFoundException){
                logger.warn("Unable to find " + fileName);
                logger.warn("Creating " + path + " with default values");
            } else {
                logger.warn("The config file " + fileName + " contains incorrect information");
                logger.warn("Resetting settings attribute to default values");
            }

            try {
                OutputStream output = new FileOutputStream(path);

                // set default values
                prop.setProperty("nbAllowedTry", Integer.toString(5));
                prop.setProperty("combinationLength", Integer.toString(4));

                // save properties to project root folder
                prop.store(output, null);

                InputStream input = new FileInputStream(path);

                prop.load(input);



            } catch (IOException io) {
                logger.fatal("Config file " + path + " can't be created");
                io.printStackTrace();
            }

        } catch (IOException io){
            logger.fatal("An unhandled exception has occured");
            io.printStackTrace();
        }
    }
}
