package com.gameplaystudio.rudy.combination.gameModes;

public class ModeDefense extends GameMode {

    @Override
    public String getNameInMenu(){
        return "Mode Defense";
    }

    @Override
    protected void init(){
        run = true;
    }

    @Override
    public void start(){
        init();
        logic();
    }

    @Override
    protected void logic(){

    }

    @Override
    protected void stop(){ run = false; }
}
