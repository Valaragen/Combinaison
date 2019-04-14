package com.gameplaystudio.rudy;

import java.util.*;

public class CombinationGame {
    private boolean play;
    private Scanner sc = new Scanner(System.in);
    private List<Mode> modes = new ArrayList<>();


    public void init(){
        play = true;
        modes.clear();
        modes.add(new ModeChallenger());
        modes.add(new ModeDefense());
        modes.add(new ModeDuel());
    }

    public void start(){
        init();
        while(play){
            displayMenu();
            chooseMode();
        }
    }

    private void displayMenu() {
        System.out.println("\nBienvenue sur le mode combinaison");
        System.out.println("Veuillez selectionner le mode de jeu souhaité\n");

        int selectionNumber = 1;
        for(Mode mode : modes){
            System.out.println(selectionNumber + ". Mode " + mode.getName());
            selectionNumber++;
        }

        System.out.println("\n" + selectionNumber + ". Quitter l'application");
    }

    private void chooseMode() {
        int choice = sc.nextByte();
        if(choice > 0 && choice < modes.size()){
            System.out.println(modes.get(choice-1).getName());
        }else if(choice == modes.size()){
            quit();
        }else{
            System.out.println("Votre séléction n'est pas valide");
        }
    }

    private void quit() {
        play = false;
    }


}
