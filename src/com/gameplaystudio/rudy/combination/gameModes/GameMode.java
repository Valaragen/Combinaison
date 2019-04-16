package com.gameplaystudio.rudy.combination.gameModes;

public abstract class GameMode {
    protected boolean run;

    public String getNameInMenu(){
        return "Default";
    }

    protected void init(){
        run = true;
    }

    public void start(){
        init();
        logic();
    }

    protected void logic(){

        stop();
    }

    protected void stop(){ run = false; }

}
