package com.gameplaystudio.combination.gameModes;

import com.gameplaystudio.combination.CombinationGame;
import com.gameplaystudio.combination.util.Config;
import com.gameplaystudio.combination.util.Displayer;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * GameMode class is designed to be the superclass of all Game Modes
 */
public abstract class GameMode {

    /**
     * Enum of all available Game modes
     */
    protected enum Mode{
        MODE_DUEL("Mode Duel"),
        MODE_CHALLENGER("Mode Challenger"),
        MODE_DEFENSE("Mode Defense");

        private String name;

        Mode(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }

    /**
     * Set the logger used for all GameMode child
     */
    final Logger logger = CombinationGame.logger;

    /**
     * <p>This attribute represent the running state of the Game Mode<br>
     * It must be initialised to true at the start or the Game Mode will not start properly</p>
     *
     * <p>When it's set to false the Game Mode should stop running</p>
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
     * Getter for the name of the GameMode<br>
     * It return a string that represent the name of the Game Mode<br>
     * <i>This method must be override in every class that extends GameMode</i>
     *
     * @return Return the name of the Game Mode as a String
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
        displayGreeting();
        init();
        while (isRunning) {
            logic();
        }
    }

    /**
     * This method contains the core of the Game Mode<br>
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

    private void displayGreeting(){
        String textToDisplay = "";

        textToDisplay += "Bienvenue dans le " + getModeName();

        Displayer.displaySemiBoxed(textToDisplay, Displayer.TAG.EQUAL_SEPARATOR, 1, 1);
    }

    /**
     * This method show a menu to the player and get his choice<br>
     * The player can replay the current Game Mode, go back to menu or leave the application
     *
     * @see #logic()
     * @see #stop()
     * @see #leaveApp()
     */
    void showReplayMenu() {
        boolean choiceIsValid = false;
        int nbErrorInARow = 0;

        String menuToDisplay = "Souhaitez vous rejouer ?\n";
        menuToDisplay += "1.Rejouer\n";
        menuToDisplay += "2.Retourner au menu\n";
        menuToDisplay += "3.Quitter l'application";

        Displayer.display(menuToDisplay);

        while (!choiceIsValid) {
            String choice = scanner.nextLine();

            //Regex that check if the user choice is an positive int with 1 digit
            if (Pattern.matches("^[0-9]$", choice)) {
                switch (choice) {
                    case "1":
                        choiceIsValid = true;
                        init();
                        break;
                    case "2":
                        choiceIsValid = true;
                        stop();
                        break;
                    case "3":
                        choiceIsValid = true;
                        leaveApp();
                        break;
                    default:
                        break;
                }
            }
            if (!choiceIsValid) {
                nbErrorInARow++;
                String errorMessage = "Votre sélection n'est pas valide\n" +
                        "Veuillez choisir un entier compris entre 1 et 3 inclus\n";
                Displayer.display(errorMessage);
                if (nbErrorInARow%3 == 0) {
                    Displayer.display(menuToDisplay);
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
     * @see Config#combinationLength
     * @return Return the player combination as a string
     */
    String chooseCombination() {
        return chooseCombination("");
    }

    /**
     * This method ask the player to enter a valid combination based of the length in config file and a regex<br>
     * It return the choice of the player when the combination match the requirements<br>
     *
     * @see Config#combinationLength
     * @param informationToDisplay indications you want to display to inform the player the future utility of the asked combination
     * @return Return the player combination as a string
     */
    String chooseCombination(String informationToDisplay) {
        boolean choiceIsValid = false;
        String choice;

        informationToDisplay  = "Veuillez définir une combinaison de " + Config.combinationLength + " chiffres" +(informationToDisplay.isEmpty() ? "" : "\n") + informationToDisplay;
        Displayer.displaySemiBoxed(informationToDisplay, Displayer.TAG.LINE_SEPARATOR);

        do {
            choice = scanner.nextLine();
            String resultToDisplay;
            if (Pattern.matches("^[0-9]+$", choice) && choice.length() == Config.combinationLength) {
                resultToDisplay = "Très bon choix !";
                choiceIsValid = true;
            } else {
                resultToDisplay = "Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + Config.combinationLength + " chiffres\n";
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
     * @param str          The combination to test
     * @param strToCompare The combination to find
     * @return Return the hint as a string
     */
    String showHint(String str, String strToCompare) {
        StringBuilder hintBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            int difference = str.charAt(i) - strToCompare.charAt(i);
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


}
