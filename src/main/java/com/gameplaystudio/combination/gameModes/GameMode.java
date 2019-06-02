package com.gameplaystudio.combination.gameModes;

import com.gameplaystudio.combination.CombinationGame;
import com.gameplaystudio.combination.util.Config;
import com.gameplaystudio.combination.util.Displayer;
import org.apache.log4j.Logger;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * GameMode class is designed to be the superclass of all Game Modes
 */
public abstract class GameMode {

    /**
     * Enum of all available Game modes
     */
    protected enum MODE {
        MODE_DUEL("Mode Duel"),
        MODE_CHALLENGER("Mode Challenger"),
        MODE_DEFENSE("Mode Defense");
        private String name;
        MODE(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    private static final String CHOOSE_COMBINATION_INDICATION_TO_DISPLAY = "Veuillez définir une combinaison de ";
    private static final String GOOD_CHOICE = "Très bon choix !";
    private static final String WELCOME_TEXT = "Bienvenue dans le ";
    private static final String REPLAY_MENU_TO_DISPLAY = "Souhaitez vous rejouer ?" + Displayer.CARRIAGE_RETURN
            + "1.Rejouer" + Displayer.CARRIAGE_RETURN
            + "2.Retourner au menu" + Displayer.CARRIAGE_RETURN
            + "3.Quitter l'application";
    private static final String REPLAY_ERROR_MESSAGE = "Votre sélection n'est pas valide" + Displayer.CARRIAGE_RETURN
            + "Veuillez choisir un entier compris entre 1 et 3 inclus" + Displayer.CARRIAGE_RETURN;

    static final String THE_COMBINATION_WAS = "La combinaison était";
    static final String CHOOSE_COMBINATION_ERROR_MESSAGE = "Votre combinaison n'est pas valide, merci d'entrer une combinaison de ";
    static final String DIGITS = " chiffres";
    static final String ALLOWED_TRIES = " éssais autorisés !" + Displayer.CARRIAGE_RETURN;
    static final String YOU_USED = "Vous avez mis ";
    static final String NEW_PROPOSITION = "Nouvelle proposition : ";
    static final String ANSWER = "Réponse : ";
    static final String ATTEMPT = "Essai ";
    static final String DOUBLE_PIPE_SEPARATOR = " || ";
    static final String SINGLE_PIPE_SEPARATOR = " | ";
    static final String SLASH_SEPARATOR = "/";
    static final String RIGHTWARD_ARROW = " -> ";
    static final String TRY = " éssai";
    static final String TRIES = " éssais";

    /**
     * Set the logger used for all GameMode child
     */
    final Logger logger = CombinationGame.logger;
    /**
     * Scanner object used to get user inputs
     */
    Scanner scanner = new Scanner(System.in);
    /**
     * The secret combination of the computer, the player has to find it
     */
    String computerSecretCombination;
    /**
     * The secret combination of the player, the computer has to find it
     */
    String playerSecretCombination;
    /**
     * The player guess about the computer combination
     */
    String playerGuess;
    /**
     * The computer guess about the player combination
     */
    String computerGuess;
    /**
     * <p>This attribute represent the running state of the Game MODE<br>
     * It must be initialised to true at the start or the Game MODE will not start properly</p>
     *
     * <p>When it's set to false the Game MODE should stop running</p>
     *
     * @see #init()
     * @see #start()
     * @see #stop()
     * @see GameMode
     */
    private boolean isRunning;

    /**
     * Boolean that represent the will to leave the app
     */
    private boolean leavingApp = false;

    /**
     * Getter for the name of the GameMode<br>
     * It return a string that represent the name of the Game MODE<br>
     * <i>This method must be override in every class that extends GameMode</i>
     *
     * @return Return the name of the Game MODE as a String
     * @see GameMode
     */
    public abstract String getModeName();

    /**
     * Getter for the {@link #leavingApp} boolean
     *
     * @return Return <code>true</code> if the player want to leave the application else return <code>false</code>
     * @see CombinationGame
     */
    public boolean isLeavingApp() {
        return leavingApp;
    }

    /**
     * <p>Initialise all the class attributes that could change while using the {@link GameMode}<br>
     * Some attributes are initialised from a file</p>
     *
     *
     * <p><b>This method really need to contain an initialisation of ALL the attributes that could change during the game<br>
     * in order to reinitialise the game just by using this method or at each game start</b></p>
     *
     * @see Config#updateSettingsFromFile()
     * @see #isRunning
     * @see #start()
     */
    private void init() {
        Config.updateSettingsFromFile();
        isRunning = true;
        computerSecretCombination = "";
        playerSecretCombination = "";
        playerGuess = "";
        computerGuess = "";
    }

    /**
     * Method used to start the program<br>
     * It initialise the attributes attributes through {@link #init()} and isRunning the logic of the game through {@link #logic()}
     */
    public void start() {
        logger.info("Start " + getModeName());
        displayGreeting();
        init();
        while (isRunning) {
            logic();
        }
        logger.info("Leave " + getModeName());
    }

    /**
     * This method contains the core of the Game MODE<br>
     * It is executed while {@link #isRunning} value is true
     * <i>This method must be override in every class that extends GameMode</i>
     *
     * @see #stop()
     */
    protected abstract void logic();

    /**
     * Method that set {@link #isRunning} to false in order to leave the current GameMode
     */
    private void stop() {
        isRunning = false;
    }

    /**
     * Method that set {@link #isRunning} to false and {@link #leavingApp} to true in order to leave the application
     *
     * @see CombinationGame
     */
    private void leaveApp() {
        isRunning = false;
        leavingApp = true;
    }

    private void displayGreeting() {
        String textToDisplay = WELCOME_TEXT + getModeName();

        Displayer.displaySemiBoxed(textToDisplay, Displayer.TAG.EQUAL_SEPARATOR, 1, 1);
    }

    /**
     * This method show a menu to the player and get his choice<br>
     * The player can replay the current Game MODE, go back to menu or leave the application
     *
     * @see #logic()
     * @see #stop()
     * @see #leaveApp()
     */
    void showReplayMenu() {
        boolean choiceIsValid = false;
        int nbErrorInARow = 0;

        Displayer.display(REPLAY_MENU_TO_DISPLAY);

        while (!choiceIsValid) {

            String choice = scanner.nextLine();

            //Regex that check if the user choice is an positive int with 1 digit
            if (Pattern.matches("^[0-9]$", choice)) {
                choiceIsValid = true;
                switch (choice) {
                    case "1":
                        logger.info("variables's re-initialisation");
                        init();
                        break;
                    case "2":
                        stop();
                        break;
                    case "3":
                        leaveApp();
                        break;
                    default:
                        choiceIsValid = false;
                        break;
                }
            } else {
                nbErrorInARow++;
                Displayer.display(REPLAY_ERROR_MESSAGE);
                if (nbErrorInARow % 3 == 0) {
                    Displayer.display(REPLAY_MENU_TO_DISPLAY);
                    nbErrorInARow = 0;
                }
            }
        }
    }

    /**
     * Generate a combination of positive integers<br>
     * The number of digit is taken from the application config file<br>
     *
     * @return Return the combination as a {@link String}
     * @see Config
     * @see Config#updateSettingsFromFile()
     */
    String generateCombination() {
        StringBuilder combinationBuilder = new StringBuilder();

        for (int i = 0; i < Config.combinationLength; i++) {
            combinationBuilder.append((int) (Math.random() * 10));
        }

        return combinationBuilder.toString();
    }

    /**
     * This method ask the player to enter a valid combination based of the length in config file and a regex<br>
     * It return the choice of the player when the combination match the requirements<br>
     * The combination length is taken from the config file<br>
     *
     * @return Return the player combination as a string
     * @see Config#combinationLength
     */
    String chooseCombination() {
        return chooseCombination("");
    }

    /**
     * This method ask the player to enter a valid combination based of the length in config file and a regex<br>
     * It return the choice of the player when the combination match the requirements<br>
     *
     * @param informationToDisplay indications you want to display to inform the player the future utility of the asked combination
     * @return Return the player combination as a string
     * @see Config#combinationLength
     */
    String chooseCombination(String informationToDisplay) {
        boolean choiceIsValid = false;
        String choice;

        informationToDisplay = CHOOSE_COMBINATION_INDICATION_TO_DISPLAY + Config.combinationLength + DIGITS + (informationToDisplay.isEmpty() ? "" : Displayer.CARRIAGE_RETURN) + informationToDisplay;
        Displayer.displaySemiBoxed(informationToDisplay, Displayer.TAG.LINE_SEPARATOR);

        do {
            choice = scanner.nextLine();
            String resultToDisplay;
            if (Pattern.matches("^[0-9]+$", choice) && choice.length() == Config.combinationLength) {
                resultToDisplay = GOOD_CHOICE;
                choiceIsValid = true;
            } else {
                resultToDisplay = CHOOSE_COMBINATION_ERROR_MESSAGE + Config.combinationLength + DIGITS + Displayer.CARRIAGE_RETURN;
            }
            Displayer.display(resultToDisplay);
        } while (!choiceIsValid);
        return choice;
    }

    /**
     * Method that return an hint composed of '=','-' or '+' chars<br>
     * = -> if the digit is correct<br>
     * + -> if the digit to find is bigger<br>
     * - -> if the digit to find is smaller
     *
     * @param combinationGuess  The combination to test
     * @param combinationToFind The combination to find
     * @return Return the hint as a string
     */
    String generateHint(String combinationToFind, String combinationGuess) {
        StringBuilder hintBuilder = new StringBuilder();
        for (int i = 0; i < combinationGuess.length(); i++) {
            int difference = combinationToFind.charAt(i) - combinationGuess.charAt(i);
            if (difference == 0) {
                hintBuilder.append("=");
            } else if (difference < 0) {
                hintBuilder.append("-");
            } else {
                hintBuilder.append("+");
            }
        }
        return hintBuilder.toString();
    }

    /**
     * This method use a combination and a hint and return a new combination based of the hint given<br>
     * The hint is composed of '=','-' or '+' chars<br>
     * = -> the digit will stay the same<br>
     * + -> the digit will increment<br>
     * - -> the digit will decrement<br>
     * <i>If it's the first guess (computerGuess is empty) it return a random combination</i><br>
     * <i>If no hint is given or  </i>
     *
     * @param hint String of the hint to change the combination
     * @return Return the new combination as a String
     * @see #generateCombination()
     * @see #computerGuess
     */
    String computerGuessNewCombinationFromHint(String hint, int nbAttempt) {
        if (computerGuess.equals("")) {
            return generateCombination();
        }

        if (computerGuess.length() != hint.length()) {
            logger.debug("The combination and the hint given doesn't have the same length, the computer can't guess correctly");
            return generateCombination();
        }


        StringBuilder newCombination = new StringBuilder();
        for (int i = 0; i < hint.length(); i++) {
            int step = 5 - nbAttempt;
            if (step <= 0) {
                step = 1;
            }
            int currentNumber = computerGuess.charAt(i) - '0';
            if (hint.charAt(i) == '+') {

                if (nbAttempt == 2) {
                    if (currentNumber == 0) {
                        step += 2;
                    } else if (currentNumber == 1) {
                        step += 1;
                    }
                }
                if (currentNumber + step > 9) {
                    step = 1;
                }
                currentNumber += step;
                if (currentNumber > 9) {
                    currentNumber = 9;
                }

            } else if (hint.charAt(i) == '-') {

                if (nbAttempt == 2) {
                    if (currentNumber == 9) {
                        step += 2;
                    } else if (currentNumber == 8) {
                        step += 1;
                    }
                }
                if (currentNumber - step < 0) {
                    step = 1;
                }
                currentNumber -= step;
                if (currentNumber < 0) {
                    currentNumber = 0;
                }

            }
            newCombination.append(currentNumber);

        }
        return newCombination.toString();
    }


}
