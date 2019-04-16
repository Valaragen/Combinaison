package com.gameplaystudio.rudy.gameModes;

public abstract class GameMode {
    protected boolean run;
    protected String name;

    public String getName(){
        return name;
    }

    protected void init(){
        run = true;
    }
    public void start(){

    }
    public void logic(){

    }
    public void stop(){
        run = false;
    }

}
