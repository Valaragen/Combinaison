package com.gameplaystudio.rudy.combination.gameModes;

public class ModeChallenger extends GameMode {

    @Override
    public String getNameInMenu(){
        return "Mode Challenger";
    }

    @Override
    protected void logic(){
        while(run){
            combination.generateCombination();
            boolean play = true;
            int playerCombination = 0;
            while(run && play){
                displayIndication();
                playerCombination = sc.nextByte();

            }
        }
    }

    private void displayIndication() {
        System.out.println("Tappez une combinsaison à " + combination.getLength() + " chiffres");
        System.out.println("'=' -> le chiffre est bon");
        System.out.println("'+' -> le chiffre à trouver est plus grand");
        System.out.println("'-' -> le chiffre à trouver est plus petit");
    }

}
