package com.gameplaystudio.rudy.gameModes;

public class ModeDuel extends GameMode {
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
