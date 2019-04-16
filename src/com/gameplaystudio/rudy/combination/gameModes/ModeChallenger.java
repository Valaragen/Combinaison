package com.gameplaystudio.rudy.combination.gameModes;

import java.util.regex.Pattern;

public class ModeChallenger extends GameMode {

    @Override
    public String getNameInMenu(){
        return "Mode Challenger";
    }

    @Override
    protected void logic(){
        System.out.println("------------------------------------------------------------------");
        System.out.println("Bienvenue dans le mode challenger");

        while (run){
            combination.generateCombination();
            System.out.println(combination.getCombination());
            boolean play = true;
            String playerCombination;
            int nbTry = 0;
            int nbAllowedTry = 10;

            while (play){
                displayIndication();
                playerCombination = sc.nextLine();
                nbTry++;

                if (Pattern.matches("[0-9]+", playerCombination) && playerCombination.length() == combination.getLength()){
                    if (playerCombination.equals(combination.toString())){
                        play = false;
                    }else{
                        



                    }
                }else{
                    System.out.println("Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + combination.getLength() + " chiffres");
                }

                if ( nbTry >= nbAllowedTry ){
                    play = false;
                }
            }

        }
    }

    private void displayIndication() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Tappez une combinsaison à " + combination.getLength() + " chiffres");
        System.out.println("'=' -> le chiffre est bon");
        System.out.println("'+' -> le chiffre à trouver est plus grand");
        System.out.println("'-' -> le chiffre à trouver est plus petit");
    }

}
