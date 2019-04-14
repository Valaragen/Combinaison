package com.gameplaystudio.rudy;

import java.util.Scanner;

public class CombinationGame {
    private boolean play = true;
    private int nbTry = 5;
    private Scanner sc = new Scanner(System.in);


    public void start(){
        while(play){
            this.displayMenu();
            int choice = this.sc.nextByte();
            if(choice == 1){
                modeChallenger();
                Combination combinaison = new Combination();
                combinaison.generateCombination();
                System.out.println(combinaison.getCombination());
            }else if(choice == 2){
                modeDefenseur();
                Combination combinaison = new Combination();
                combinaison.generateCombination();
                System.out.println(combinaison.getCombination());
            }else if(choice == 3){
                modeDuel();
                Combination combinaison = new Combination();
                combinaison.generateCombination();
                System.out.println(combinaison.getCombination());
            }else if(choice == 4){
                quit();
            }else{
                System.out.println("Votre séléction n'est pas valide");
            }
        }
    }

    private void quit() {
        this.play = false;
    }

    private void displayMenu() {
        System.out.println("Bienvenue sur le mode combinaison");
        System.out.println("Veuillez selectionner le mode de jeu souhaité");
        System.out.println("1.Mode challenger");
        System.out.println("2.Mode défenseur");
        System.out.println("3.Mode duel");
        System.out.println("4.Quitter l'application");
    }

    private void modeChallenger(){
        System.out.println("mode challenger choisi");
    }

    private void modeDefenseur(){
        System.out.println("mode challenger choisi");
    }
    private void modeDuel(){
        System.out.println("mode défenseur choisi");
    }




}
