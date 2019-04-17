package com.gameplaystudio.rudy.combination.gameModes;

import com.gameplaystudio.rudy.combination.util.Combination;

import java.util.Scanner;

public abstract class GameMode {
    protected boolean run;
    protected boolean leaveApp;
    protected Scanner sc = new Scanner(System.in);

    public String getNameInMenu(){
        return "Default";
    }

    protected void init(){
        run = true;
        leaveApp = false;
    }

    public boolean start(){
        init();
        while(run){
            logic();
        }
        return leaveApp;
    }

    protected void logic(){

    }

    protected void stop(){ run = false; }

    protected void leaveApp(){
        run = false;
        leaveApp = true;
    }
    protected void showReplayMenu(){
        boolean validChoice = false;
        while (!validChoice){
            System.out.println("Souhaitez vous rejouer ?");
            System.out.println("1.Rejouer");
            System.out.println("2.Retourner au menu");
            System.out.println("3.Quitter l'application");
            int choice = sc.nextByte();//TODO handle typing error
            switch (choice){
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
        }
        sc.nextLine();
    }


}
