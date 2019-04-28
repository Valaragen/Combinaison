package com.gameplaystudio.combination;

import com.gameplaystudio.combination.gameModes.GameMode;
import com.gameplaystudio.combination.gameModes.ModeChallenger;
import com.gameplaystudio.combination.gameModes.ModeDefense;
import com.gameplaystudio.combination.gameModes.ModeDuel;
import com.gameplaystudio.combination.util.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.regex.Pattern;

/**
 * <p>Class that run all the processes in order to play a <i>GameMode</i></p>
 *
 * <p>To run the game you just need to initialise a CombinationGame object and use {@link #start()}</p>
 *
 * @author Valaragen
 * @version 1.0
 * @see GameMode
 */
public class CombinationGame { //TODO More logger info

    /**
     * Logger object from the log4j library
     */
    public static final Logger logger = LogManager.getLogger(CombinationGame.class);

    /**
     * Scanner object used to get user inputs
     */
    private Scanner sc = new Scanner(System.in);

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
     *
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
     * @see Config#updateSettingsFromFile()
     * @see #run
     * @see #gameModes
     * @see #start()
     */
    private void init() {
        Config.updateSettingsFromFile();
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
            logger.trace("Enter the logic method");
            logic();
            logger.trace("Leave CombinationGame logic");
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
     * Method that set {@link #run} to false in order to leave the application
     */
    private void quit() {
        run = false;
    }


    /**
     * Display the menu by iterating through {@link #gameModes}
     */
    private void displayMenu() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Bienvenue sur le jeu combinaison");
        System.out.println("Veuillez selectionner le mode de jeu souhaité");
        System.out.println("------------------------------------------------------------------\n");

        int selectionNumber = 1;
        for (GameMode gameMode : gameModes) {
            System.out.println(selectionNumber + ". " + gameMode.getModeName());
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
     * @see GameMode#start()
     * @see GameMode#getLeaveApp()
     * @see #quit()
     */
    private void chooseMode() {
        String choice = sc.nextLine();

        //Regex that check if the user choice is an positive int with 1 or 2 digits
        if (Pattern.matches("^[0-9]{1,2}$", choice)) {
            int intChoice = Integer.parseInt(choice);

            if (intChoice > 0 && intChoice <= gameModes.size()) {
                GameMode selectedGameMode = gameModes.get(intChoice - 1);
                selectedGameMode.start();

                if (selectedGameMode.getLeaveApp()) {
                    quit();
                }
            } else if (intChoice == gameModes.size() + 1) {
                quit();
            } else {
                System.out.println("Votre sélection n'est pas valide");
                System.out.println("Veuillez choisir un entier compris entre " + 1 + " et " + (gameModes.size() + 1) + " inclus");
            }
        } else {
            System.out.println("Votre sélection n'est pas valide");
            System.out.println("Veuillez choisir un entier compris entre " + 1 + " et " + (gameModes.size() + 1) + " inclus");
        }


    }

}
