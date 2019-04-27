package com.gameplaystudio.combination.gameModes;

import com.gameplaystudio.combination.CombinationGame;
import com.gameplaystudio.combination.util.Config;

import java.util.regex.Pattern;

/**
 * Sub-class of {@link GameMode}<br>
 * In this Game Mode a combination is generated<br>
 * The player has a limited number of tries to find the generated combination<br>
 * After each try the player receive an hint
 * <i>The number of try and the number of digit in the combination are get from a setting file</i>
 * @see #logic()
 * @see #showHint(String, String)
 * @see #generateCombination()
 * @see Config
 */
public class ModeChallenger extends GameMode {

    @Override
    public String getModeName() {
        return "Mode Challenger";
    }

    @Override
    protected void logic() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Bienvenue dans le mode challenger");

        while (run) {
            String combinationToFind = super.generateCombination();
            CombinationGame.logger.debug("(Combinaison secrète : " + combinationToFind + ")");

            boolean play = true;
            boolean win = false;
            int nbTry = 0;

            displayIndication();

            while (play) {
                String combinationGuess = sc.nextLine();

                if (Pattern.matches("^[0-9]+$", combinationGuess) && combinationGuess.length() == Config.combinationLength) {
                    System.out.println("Proposition : " + combinationGuess + " -> Réponse : " + super.showHint(combinationToFind, combinationGuess));
                    nbTry++;
                    if (nbTry >= Config.nbAllowedTry) {
                        play = false;
                    }
                    if (combinationGuess.equals(combinationToFind)) {
                        play = false;
                        win = true;
                    }
                } else {
                    System.out.println("Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + Config.combinationLength + " chiffres");
                }
            }

            if (win) {
                System.out.println("------------------------------------------------------------------");
                System.out.println("Bravo vous avez trouvé la combinaison !");
                System.out.println("Vous avez mis " + nbTry + " éssai" + (nbTry>1?"s":""));
                System.out.println("La combinaison était  | " + combinationToFind + " |");
                System.out.println("------------------------------------------------------------------");
            } else {
                System.out.println("------------------------------------------------------------------");
                System.out.println("Dommage vous avez dépassé les " + Config.nbAllowedTry + " éssais autorisés !");
                System.out.println("La combinaison était | " + combinationToFind + " |");
                System.out.println("------------------------------------------------------------------");
            }

            super.showReplayMenu();

        }
    }

    /**
     * Show indications about how the game should be played
     */
    private void displayIndication() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Tappez une combinsaison à " + Config.combinationLength + " chiffres");
        System.out.println("'=' -> le chiffre est bon");
        System.out.println("'+' -> le chiffre à trouver est plus grand");
        System.out.println("'-' -> le chiffre à trouver est plus petit");
        System.out.println("------------------------------------------------------------------");
    }



}
