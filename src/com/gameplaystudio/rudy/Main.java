package com.gameplaystudio.rudy;

import com.gameplaystudio.rudy.combination.CombinationGame;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
/*        CombinationGame combinationGame = new CombinationGame();
        combinationGame.start();*/
        try (OutputStream output = new FileOutputStream("./config.settings")) {
            Properties prop = new Properties();

            // set the properties value
            prop.setProperty("db.url", "localhost");
            prop.setProperty("db.user", "mkyong");
            prop.setProperty("db.password", "password");

            // save properties to project root folder
            prop.store(output, null);

            System.out.println(prop);

        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
