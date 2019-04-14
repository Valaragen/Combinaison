package com.gameplaystudio.rudy;

public interface Mode {
    Combination combination = new Combination();
    void init();
    void start();
    void logic();
    void stop();

}
