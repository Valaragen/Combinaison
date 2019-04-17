package com.gameplaystudio.rudy.combination.gameModes;

import com.gameplaystudio.rudy.combination.util.Combination;

import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class GameMode {
    protected boolean run;
    protected boolean leaveApp;
    protected Scanner sc = new Scanner(System.in);

    public String getNameInMenu() {
        return "Default";
    }

    protected void init() {
        run = true;
        leaveApp = false;//TODO delete maybe ?
    }

    public boolean start() {
        init();
        while (run) {
            logic();
        }
        return leaveApp;
    }

    protected void logic() {

    }

    protected void stop() {
        run = false;
    }

    protected void leaveApp() {
        run = false;
        leaveApp = true;
    }

    protected void showReplayMenu() {
        boolean validChoice = false;
        int choice = 0;
        while (!validChoice) {
            System.out.println("Souhaitez vous rejouer ?");
            System.out.println("1.Rejouer");
            System.out.println("2.Retourner au menu");
            System.out.println("3.Quitter l'application");
            try{
                choice = sc.nextByte();//TODO Optimise handle typing error
            }catch(InputMismatchException e){
                System.err.println("Votre sélection n'est pas valide");
            }

            switch (choice) {
                case 1:
                    validChoice = true;
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


}
