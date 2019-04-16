package com.gameplaystudio.rudy.combination;

import com.gameplaystudio.rudy.combination.gameModes.GameMode;
import com.gameplaystudio.rudy.combination.gameModes.ModeChallenger;
import com.gameplaystudio.rudy.combination.gameModes.ModeDefense;
import com.gameplaystudio.rudy.combination.gameModes.ModeDuel;

import java.util.*;

public class CombinationGame{

    private boolean run;
    private Scanner sc = new Scanner(System.in);
    private List<GameMode> gameModes = new ArrayList<>();

    private void init(){
        run = true;
        gameModes.clear();
        gameModes.add(new ModeChallenger());
        gameModes.add(new ModeDefense());
        gameModes.add(new ModeDuel());
    }

    public void start(){
        init();
        logic();

    }

    private void logic(){
        while(run) {
            displayMenu();
            chooseMode();
        }
    }

    private void displayMenu() {
        System.out.println("\nBienvenue sur le jeu combinaison");
        System.out.println("Veuillez selectionner le mode de jeu souhaité\n");

        int selectionNumber = 1;
        for(GameMode gameMode : gameModes){
            System.out.println(selectionNumber + ". " + gameMode.getNameInMenu());
            selectionNumber++;
        }

        System.out.println("\n" + selectionNumber + ". Quitter l'application");
    }

    private void chooseMode() {
        int choice = sc.nextByte();
        if(choice > 0 && choice <= gameModes.size()){
            run = !gameModes.get(choice-1).start();
        }else if(choice == gameModes.size()+1){
            quit();
        }else{
            System.out.println("Votre séléction n'est pas valide");
        }
    }

    private void quit() {
        run = false;
    }


}
