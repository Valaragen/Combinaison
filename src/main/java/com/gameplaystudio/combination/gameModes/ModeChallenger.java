package com.gameplaystudio.combination.gameModes;

import com.gameplaystudio.combination.util.Config;

import java.util.regex.Pattern;

/**
 * Sub-class of {@link GameMode}<br>
 * In this Game Mode a combination is generated<br>
 * The player has a limited number of tries to find the generated combination<br>
 * After each try the player receive an hint
 * <i>The number of try and the number of digit in the combination are get from a setting file</i>
 *
 * @see #logic()
 * @see #showHint(String, String)
 * @see #generateCombination()
 * @see Config
 */
public class ModeChallenger extends GameMode {

    @Override
    public String getModeName() {
        return Mode.MODE_CHALLENGER.getName();
    }

    @Override
    public void start() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Bienvenue dans le " + Mode.MODE_CHALLENGER.getName());
        super.start();
    }

    @Override
    protected void logic() {

        computerSecretCombination = super.generateCombination();
        logger.debug("(Combinaison secrète : " + computerSecretCombination + ")");

        boolean isPlaying = true;
        boolean hasWin = false;
        int nbAttempts = 0;

        displayIndication();

        while (isPlaying) {
            playerGuess = scanner.nextLine();

            if (playerGuess.length() == Config.combinationLength && Pattern.matches("^[0-9]+$", playerGuess)) {
                nbAttempts++;
                System.out.println("Essai " + nbAttempts + "/" + Config.maxAttempts + " | Proposition : " + playerGuess + " -> Réponse : " + super.showHint(computerSecretCombination, playerGuess));
                if (nbAttempts >= Config.maxAttempts) {
                    isPlaying = false;
                }
                if (playerGuess.equals(computerSecretCombination)) {
                    isPlaying = false;
                    hasWin = true;
                }
            } else {
                System.out.println("Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + Config.combinationLength + " chiffres");
            }
        }

        System.out.println("------------------------------------------------------------------");

        if (hasWin) {
            System.out.println("Bravo vous avez trouvé la combinaison !");
            System.out.println("Vous avez mis " + nbAttempts + " éssai" + (nbAttempts > 1 ? "s" : ""));
        } else {
            System.out.println("Dommage vous avez dépassé les " + Config.maxAttempts + " éssais autorisés !");
        }
        System.out.println("La combinaison était | " + computerSecretCombination + " |");
        System.out.println("------------------------------------------------------------------");

        super.showReplayMenu();

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
