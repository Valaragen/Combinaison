package com.gameplaystudio.combination;

import com.gameplaystudio.Main;
import com.gameplaystudio.combination.gameModes.GameMode;
import com.gameplaystudio.combination.gameModes.ModeChallenger;
import com.gameplaystudio.combination.gameModes.ModeDefense;
import com.gameplaystudio.combination.gameModes.ModeDuel;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * <p>Class that run all the processes in order to play a <i>GameMode</i></p>
 *
 * <p>To run the game you just need to initialise a CombinationGame object and use {@link #start()}</p>
 *
 * @see GameMode
 *
 * @author Valaragen
 * @version 1.0
 */
public class CombinationGame {

    /**
     * Logger object from the log4j library
     */
    public final static Logger logger = Logger.getLogger(Main.class);

    /**
     * Scanner object used to get user inputs
     */
    private Scanner sc = new Scanner(System.in);

    /**
     * Attribute used to set the number of allowed try in a game mode with a limited number of tries<br>
     * <i>This attribute is extracted from a file</i>
     *
     * @see #updateAttributesFromFile()
     * @see #init()
     */
    public static int nbAllowedTry;
    /**
     * Attribute used to set the number of digit a combinations will have in all Game Modes<br>
     * <i>This attribute is extracted from a file</i>
     *
     * @see #updateAttributesFromFile()
     * @see #init()
     */
    public static int combinationLength;


    /**
     * <p>This attribute represent the running state of the application<br>
     * It must be initialised to true at the start or the application will not start properly</p>
     *
     * <p>When it's set to false the program should stop running</p>
     *
     * @see #init()
     * @see #start()
     * @see #quit()
     */
    private boolean run;

    /**
     * List that contains all the playable {@link GameMode}<br>
     * @see GameMode
     * @see #init()
     */
    private List<GameMode> gameModes = new ArrayList<>();




    /**
     * <p>Initialise all the class attributes that could change while using the application<br>
     * Some attributes are initialised from a file</p>
     *
     *
     * <p><b>This method really need to contain an initialisation of ALL the attributes that could change during the game<br>
     * in order to reinitialise the game just by using this method or at each game start</b></p>
     *
     * @see #updateAttributesFromFile()
     * @see #run
     * @see #gameModes
     * @see #start()
     */
    private void init() {
        updateAttributesFromFile();
        run = true;
        gameModes.clear();
        gameModes.add(new ModeChallenger());
        gameModes.add(new ModeDefense());
        gameModes.add(new ModeDuel());
    }

    /**
     * Method used to start the program<br>
     * It initialise the attributes attributes through {@link #init()} and run the logic of the game through {@link #logic()}
     */
    public void start() {
        init();
        while (run) {
            logic();
        }
    }

    /**
     * This method contains the core of the game<br>
     * It is executed while {@link #run} value is true
     *
     * @see #quit()
     */
    private void logic() {
        displayMenu();
        chooseMode();
    }

    /**
     * Method that set {@link #run} to false in order to leave the game
     */
    private void quit() {
        run = false;
    }

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
    private void updateAttributesFromFile() { //TODO Create a custom exception to limit the usage of the file. user can't set combination length > 10
        String fileName = "config.properties";
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

    /**
     * Display the menu by iterate through {@link #gameModes}
     *
     * @see java.util.List
     */
    private void displayMenu() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Bienvenue sur le jeu combinaison");
        System.out.println("Veuillez selectionner le mode de jeu souhaité");
        System.out.println("------------------------------------------------------------------\n");

        int selectionNumber = 1;
        for (GameMode gameMode : gameModes) {
            System.out.println(selectionNumber + ". " + gameMode.getNameInMenu());
            selectionNumber++;
        }

        System.out.println("\n" + selectionNumber + ". Quitter l'application");
    }

    /**
     * Get the choice of the user and apply it
     * Start the according GameMode or leave the application
     * Display indications when the selection is not valid
     * It also set run to false if player want to leave the application from a GameMode
     *
     * @see GameMode#start() GameMode.start()
     * @see GameMode#getLeaveApp()
     * @see #quit()
     */
    private void chooseMode() {
        String choice = sc.nextLine();

        if (Pattern.matches("^[0-9]{1,2}$", choice)) {
            int intChoice = Integer.parseInt(choice);

            if (intChoice > 0 && intChoice <= gameModes.size()) {
                GameMode selectedGameMode = gameModes.get(intChoice - 1);
                selectedGameMode.start();
                updateAttributesFromFile();

                if(selectedGameMode.getLeaveApp()){
                    quit();
                }
            } else if (intChoice == gameModes.size() + 1) {
                quit();
            } else {
                System.out.println("Votre sélection n'est pas valide");
                System.out.println("Veuillez choisir un entier compris entre " + 1 + " et " + (gameModes.size()+1) + " inclus");
            }
        } else {
            System.out.println("Votre sélection n'est pas valide");
            System.out.println("Veuillez choisir un entier compris entre " + 1 + " et " + (gameModes.size()+1) + " inclus 1111");
        }


    }

}
