package com.gameplaystudio.combination.gameModes;

import com.gameplaystudio.combination.CombinationGame;
import com.gameplaystudio.combination.util.Config;
import org.apache.logging.log4j.Logger;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * GameMode class is designed to be the superclass of all Game Modes
 */
public abstract class GameMode {
    /**
     * Set the logger used for all GameMode child
     */
    protected final Logger logger = CombinationGame.logger;
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
    protected boolean run;
    /**
     * Boolean that represent the will to leave the app
     */
    protected boolean leaveApp = false;
    /**
     * Scanner object used to get user inputs
     */
    protected Scanner sc = new Scanner(System.in);

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
     * Getter for the {@link #leaveApp} boolean
     *
     * @return Return <code>true</code> if the player want to leave the application else return <code>false</code>
     * @see CombinationGame
     */
    public boolean getLeaveApp() {
        return leaveApp;
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
     * @see #run
     * @see #start()
     */
    protected void init() {
        Config.updateSettingsFromFile();
        run = true;
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
     * This method contains the core of the Game Mode<br>
     * It is executed while {@link #run} value is true
     * <i>This method must be override in every class that extends GameMode</i>
     *
     * @see #stop()
     */
    protected abstract void logic(); //TODO Display the number of tries that last

    /**
     * Method that set {@link #run} to false in order to leave the current GameMode
     */
    protected void stop() {
        run = false;
    }

    /**
     * Method that set {@link #run} to false and {@link #leaveApp} to true in order to leave the application
     *
     * @see CombinationGame
     */
    protected void leaveApp() {
        run = false;
        leaveApp = true;
    }

    /**
     * This method show a menu to the player and get his choice<br>
     * The player can replay the current Game Mode, go back to menu or leave the application
     *
     * @see #logic()
     * @see #stop()
     * @see #leaveApp()
     */
    protected void showReplayMenu() {
        boolean validChoice = false;
        int choice = 0;
        while (!validChoice) {
            System.out.println("Souhaitez vous rejouer ?");
            System.out.println("1.Rejouer");
            System.out.println("2.Retourner au menu");
            System.out.println("3.Quitter l'application");
            try {
                choice = sc.nextByte();//TODO Optimise handle typing error
            } catch (InputMismatchException e) {
                System.err.println("Votre sélection n'est pas valide");
            }

            switch (choice) {
                case 1:
                    validChoice = true;
                    Config.updateSettingsFromFile();
                    break;
                case 2:
                    validChoice = true;
                    stop();
                    break;
                case 3:
                    validChoice = true;
                    leaveApp();
                    break;
                default:
                    System.out.println("Votre séléction n'est pas valide");
            }
            sc.nextLine();
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
    protected String generateCombination() {
        StringBuilder combinationBuilder = new StringBuilder();

        for (int i = 0; i < Config.combinationLength; i++) {
            combinationBuilder.append((int) (Math.random() * 10));
        }

        return combinationBuilder.toString();
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
    protected String showHint(String str, String strToCompare) {
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
