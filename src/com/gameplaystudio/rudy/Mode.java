package com.gameplaystudio.rudy;

public interface Mode {
    String getName();
    void init();
    void start();
    void logic();
    void stop();

}
