package com.gameplaystudio.combination;

import com.gameplaystudio.combination.gameModes.GameMode;
import com.gameplaystudio.combination.gameModes.ModeChallenger;
import com.gameplaystudio.combination.gameModes.ModeDefense;
import com.gameplaystudio.combination.gameModes.ModeDuel;
import com.gameplaystudio.combination.util.Config;
import com.gameplaystudio.combination.util.Displayer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * <p>Class that isRunning all the processes in order to play a <i>GameMode</i></p>
 *
 * <p>To isRunning the game you just need to initialise a CombinationGame object and use {@link #start()}</p>
 *
 * @author Valaragen
 * @version 1.0
 * @see GameMode
 */
public class CombinationGame {

    /**
     * Logger object from the log4j library
     */
    public static final Logger logger = Logger.getLogger(CombinationGame.class);
    private static final String MENU_TITLE_TO_DISPLAY = "Bienvenue sur le jeu combinaison\n" + "Veuillez selectionner le mode de jeu souhaité";

    /**
     * Scanner object used to get user inputs
     */
    private Scanner scanner = new Scanner(System.in);

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
    private boolean isRunning;

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
     * @see #isRunning
     * @see #gameModes
     * @see #start()
     */
    private void init() {
        Config.updateSettingsFromFile();
        isRunning = true;
        gameModes.clear();
        gameModes.add(new ModeChallenger());
        gameModes.add(new ModeDefense());
        gameModes.add(new ModeDuel());
    }

    /**
     * Method used to start the program<br>
     * It initialise the attributes attributes through {@link #init()} and isRunning the logic of the game through {@link #logic()}
     */
    public void start() {
        logger.info("Starting the game");
        init();
        while (isRunning) {
            logic();
        }
        logger.info("Leaving the game");
    }

    /**
     * This method contains the core of the game<br>
     * It is executed while {@link #isRunning} value is true
     *
     * @see #quit()
     */
    private void logic() {
        displayMenu();
        chooseMode();
    }

    /**
     * Method that set {@link #isRunning} to false in order to leave the application
     */
    private void quit() {
        isRunning = false;
    }


    /**
     * Display the menu by iterating through {@link #gameModes}
     *
     * @see Displayer
     */
    private void displayMenu() {
        Displayer.displaySemiBoxed(MENU_TITLE_TO_DISPLAY, Displayer.TAG.EQUAL_SEPARATOR, 1, 1);

        StringBuilder menuToDisplay = new StringBuilder();
        int selectionNumber = 1;
        for (GameMode gameMode : gameModes) {
            menuToDisplay.append(selectionNumber).append(". ").append(gameMode.getModeName()).append("\n");
            selectionNumber++;
        }
        menuToDisplay.append("\n").append(selectionNumber).append(". Quitter l'application");

        Displayer.display(menuToDisplay.toString());
    }

    /**
     * Get the choice of the user and apply it
     * Start the according GameMode or leave the application
     * Display indications when the selection is not valid
     * It also set isRunning to false if player want to leave the application from a GameMode
     *
     * @see GameMode#start()
     * @see GameMode#isLeavingApp()
     * @see #quit()
     */
    private void chooseMode() {
        boolean choiceIsValid = false;
        int nbErrorInARow = 0;

        while (!choiceIsValid) {
            String choice = scanner.nextLine();
            //Regex that check if the user choice is an positive int with 1 or 2 digits
            if (Pattern.matches("^[0-9]{1,2}$", choice)) {
                int choiceAsInt = Integer.parseInt(choice);

                if (choiceAsInt > 0 && choiceAsInt <= gameModes.size()) {
                    choiceIsValid = true;
                    GameMode selectedGameMode = gameModes.get(choiceAsInt - 1);
                    selectedGameMode.start();

                    if (selectedGameMode.isLeavingApp()) {
                        quit();
                    }
                } else if (choiceAsInt == gameModes.size() + 1) {
                    choiceIsValid = true;
                    quit();
                }
            }
            if (!choiceIsValid) {
                nbErrorInARow++;
                String errorMessage = "Votre sélection n'est pas valide\n" +
                        "Veuillez choisir un entier compris entre " + 1 + " et " + (gameModes.size() + 1) + " inclus\n";
                Displayer.display(errorMessage);

                if (nbErrorInARow % 3 == 0) {
                    displayMenu();
                    nbErrorInARow = 0;
                }
            }
        }


    }

}
