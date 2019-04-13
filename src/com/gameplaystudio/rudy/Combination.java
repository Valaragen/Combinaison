package com.gameplaystudio.rudy;

import java.util.Scanner;

public class Combination {
    private boolean play;
    private int length;
    private int nbTry;
    private StringBuilder combinaison = new StringBuilder(); //TODO choix le plus optmisé pourquoi?
    private Scanner sc = new Scanner(System.in);

    Combination(){
        this.play = true;
        setLength(4);
        setNbTry(5);
    }

    private void setLength(int length) {
        this.length = length;
    }

    private void setNbTry(int nbTry) {
        this.nbTry = nbTry;
    }

    private void setCombinaison() {
        combinaison.delete(0, combinaison.length());
        for (int i = 0; i < this.length; i++) {
            combinaison.append((int)(Math.random() * 10));
        }
    }

    public int getLength() {
        return length;
    }

    public String getCombinaison() {
        return combinaison.toString();
    }

    public void start(){
        while(play){
            this.displayMenu();
            int choice = this.sc.nextByte();
            if(choice == 1){
                modeChallenger();
                setCombinaison();
                System.out.println(getCombinaison());
            }else if(choice == 2){
                modeDefenseur();
                setCombinaison();
                System.out.println(getCombinaison());
            }else if(choice == 3){
                modeDuel();
                setCombinaison();
                System.out.println(getCombinaison());
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
