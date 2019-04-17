package com.gameplaystudio.rudy.combination.gameModes;

import com.gameplaystudio.rudy.combination.util.Combination;

import java.util.regex.Pattern;

public class ModeChallenger extends GameMode {
    private Combination combination = new Combination();

    @Override
    public String getNameInMenu() {
        return "Mode Challenger";
    }

    @Override
    protected void logic() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Bienvenue dans le mode challenger");

        while (run) {
            combination.generateCombination();
            System.out.println("(Combinaison secrète : " + combination.getCombination() + ")");
            boolean play = true;
            boolean win = false;
            String playerCombination;
            int nbTry = 0;
            int nbAllowedTry = 10;
            displayIndication();

            while (play) {
                playerCombination = sc.nextLine();

                if (Pattern.matches("[0-9]+", playerCombination) && playerCombination.length() == combination.getLength()) {
                    System.out.println("Proposition : " + playerCombination + " -> Réponse : " + showHint(combination.getCombination(), playerCombination));
                    nbTry++;
                    if (nbTry >= nbAllowedTry) {
                        play = false;
                    }
                    if (playerCombination.equals(combination.getCombination())) {
                        play = false;
                        win = true;
                    }
                } else {
                    System.out.println("Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + combination.getLength() + " chiffres");
                }

            }

            if (win) {
                System.out.println("------------------------------------------------------------------");
                System.out.println("Bravo vous avez trouvé la combinaison !");
                System.out.println("Vous avez mis " + nbTry + " éssais");
                System.out.println("La combinaison était  | " + combination.getCombination() + " |");
                System.out.println("------------------------------------------------------------------");
            } else {
                System.out.println("------------------------------------------------------------------");
                System.out.println("Dommage vous avez dépassé les " + nbAllowedTry + " éssais autorisés !");
                System.out.println("La combinaison était | " + combination.getCombination() + " |");
                System.out.println("------------------------------------------------------------------");
            }

            showReplayMenu();

        }
    }

    private void displayIndication() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Tappez une combinsaison à " + combination.getLength() + " chiffres");
        System.out.println("'=' -> le chiffre est bon");
        System.out.println("'+' -> le chiffre à trouver est plus grand");
        System.out.println("'-' -> le chiffre à trouver est plus petit");
        System.out.println("------------------------------------------------------------------");
    }


    private String showHint(String str, String strToCompare) {
        StringBuilder hintBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            int difference = str.charAt(i) - strToCompare.charAt(i);
            if (difference == 0) {
                hintBuilder.append("=");
            } else if (difference < 0) {
                hintBuilder.append("-");
            } else {
                hintBuilder.append("+");
            }
        }
        return hintBuilder.toString();
    }

}
