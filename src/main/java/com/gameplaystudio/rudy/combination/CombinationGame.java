package com.gameplaystudio.rudy.combination;

import com.gameplaystudio.rudy.Main;
import com.gameplaystudio.rudy.combination.gameModes.GameMode;
import com.gameplaystudio.rudy.combination.gameModes.ModeChallenger;
import com.gameplaystudio.rudy.combination.gameModes.ModeDefense;
import com.gameplaystudio.rudy.combination.gameModes.ModeDuel;
import org.apache.log4j.Logger;


import java.io.*;
import java.util.*;

public class CombinationGame {

    public final static Logger logger = Logger.getLogger(Main.class);

    private boolean run;
    private Scanner sc = new Scanner(System.in);
    private List<GameMode> gameModes = new ArrayList<>();

    static public int nbAllowedTry;
    static public int combinationLength;

    private void init() {
        updateSettingsVariable();
        run = true;
        gameModes.clear();
        gameModes.add(new ModeChallenger());
        gameModes.add(new ModeDefense());
        gameModes.add(new ModeDuel());
    }

    public void start() {
        init();
        while (run) {
            logic();
        }

    }

    private void logic() {
        displayMenu();
        chooseMode();
    }

    private void quit() {
        run = false;
    }

    private void updateSettingsVariable() {
        String path = "config.properties";

        try {
            InputStream input = new FileInputStream(path);
            Properties prop = new Properties();


            // load a properties file
            prop.load(input);

            // get the property value and print it out
            nbAllowedTry = Integer.parseInt(prop.getProperty("nbAllowedTry"));
            combinationLength = Integer.parseInt(prop.getProperty("combinationLength"));

        } catch (NumberFormatException | FileNotFoundException e){
            Properties prop = new Properties();
            if(e instanceof FileNotFoundException){
                System.err.println("Sorry, unable to find " + path);
                System.err.println("Creating " + path + " with default values");
            }

            if(e instanceof NumberFormatException){
                System.err.println("The config file " + path + " contains incorrect information");
                System.err.println("Resetting settings attribute to default values");
            }

            try (OutputStream output = new FileOutputStream(path)) {
                // set the properties value
                prop.setProperty("nbAllowedTry", "5");
                prop.setProperty("combinationLength", "4");

                // save properties to project root folder
                prop.store(output, null);

                InputStream input = new FileInputStream(path);

                prop.load(input);

            } catch (IOException io) {
                io.printStackTrace();
            }

        } catch (IOException io){
            io.printStackTrace();
        }
    }

    private void displayMenu() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("\nBienvenue sur le jeu combinaison");
        System.out.println("Veuillez selectionner le mode de jeu souhaité\n");

        int selectionNumber = 1;
        for (GameMode gameMode : gameModes) {
            System.out.println(selectionNumber + ". " + gameMode.getNameInMenu());
            selectionNumber++;
        }

        System.out.println("\n" + selectionNumber + ". Quitter l'application");
    }

    private void chooseMode() {
        int choice = sc.nextByte();//TODO Handle exception
        if (choice > 0 && choice <= gameModes.size()) {
            run = !gameModes.get(choice - 1).start();
        } else if (choice == gameModes.size() + 1) {
            quit();
        } else {
            System.out.println("Votre séléction n'est pas valide");
        }
    }

}
