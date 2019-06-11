package com.gameplaystudio;

import com.gameplaystudio.combination.CombinationGame;
import com.gameplaystudio.combination.util.Config;

public class GameLauncher {

    public static void main(String[] args) {
        //Uncomment the line below to force de devMode through the code
        //Config.forceDevMode = true;

        new CombinationGame().start();
    }

}
