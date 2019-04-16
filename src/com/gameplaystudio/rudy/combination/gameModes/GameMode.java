package com.gameplaystudio.rudy.combination.gameModes;

import com.gameplaystudio.rudy.combination.util.Combination;

import java.util.Scanner;

public abstract class GameMode {
    protected boolean run;
    protected int nbTry;
    protected boolean leaveApp;
    protected Combination combination = new Combination();
    protected Scanner sc = new Scanner(System.in);

    public String getNameInMenu(){
        return "Default";
    }

    protected void init(){
        run = true;
        nbTry = 4;
        leaveApp = false;
    }

    public void start(){
        init();
        logic();
    }

    protected void logic(){

    }

    protected void stop(){ run = false; }

    protected void leaveApp(){
        run = false;
        leaveApp = true;
    }

}
