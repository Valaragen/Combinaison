package com.gameplaystudio.combination.gameModes;

import com.gameplaystudio.combination.util.Config;
import com.gameplaystudio.combination.util.Displayer;

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
    protected void logic() {

        computerSecretCombination = super.generateCombination();
        logger.debug("(Combinaison secrète : " + computerSecretCombination + ")");

        boolean isPlaying = true;
        boolean hasWin = false;
        int nbAttempt = 1;

        displayIndication();

        while (isPlaying) {
            displayAttemptNumber(nbAttempt);

            playerGuess = scanner.nextLine();
            String resultToDisplay = "";

            if (playerGuess.length() == Config.combinationLength && Pattern.matches("^[0-9]+$", playerGuess)) {
                resultToDisplay += "Proposition : " + playerGuess + " -> Réponse : " + super.showHint(computerSecretCombination, playerGuess) + "\n";
                if (nbAttempt >= Config.maxAttempts) {
                    isPlaying = false;
                }
                if (playerGuess.equals(computerSecretCombination)) {
                    isPlaying = false;
                    hasWin = true;
                } else {
                    nbAttempt++;
                }
            } else {
                resultToDisplay += "Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + Config.combinationLength + " chiffres" + "\n";
            }

            Displayer.display(resultToDisplay);
        }

        displayGameResult(hasWin, nbAttempt);
        super.showReplayMenu();

    }

    /**
     * Display indications about how the game should be played
     *
     * @see Displayer
     */
    private void displayIndication() {
        String textToDisplay = "";

        textToDisplay += "Tappez une combinsaison à " + Config.combinationLength + " chiffres\n";
        textToDisplay += "'=' -> le chiffre est bon\n";
        textToDisplay += "'+' -> le chiffre à trouver est plus grand\n";
        textToDisplay += "'-' -> le chiffre à trouver est plus petit";


        Displayer.displaySemiBoxed(textToDisplay, Displayer.TAG.LINE_SEPARATOR);
    }

    /**
     * Display the attempt number and the max attempt number
     */
    private void displayAttemptNumber(int nbAttempt) {
        Displayer.display("Essai " + nbAttempt + "/" + Config.maxAttempts);
    }

    /**
     * Display the game results
     *
     * @param hasWin boolean set to <code>true</code> if the player wined the game
     * @param nbAttempts number of try the player has used to find the combination
     * @see Displayer
     */
    private void displayGameResult(boolean hasWin, int nbAttempts){
        String textToDisplay = "";

        if (hasWin) {
            textToDisplay += "Bravo vous avez trouvé la combinaison !\n";
            textToDisplay += "Vous avez mis " + nbAttempts + " éssai" + (nbAttempts > 1 ? "s" : "") + "\n";
        } else {
            textToDisplay += "Dommage vous avez dépassé les " + Config.maxAttempts + " éssais autorisés !\n";
        }
        textToDisplay += "La combinaison était | " + computerSecretCombination + " |";

        Displayer.displaySemiBoxed(textToDisplay, Displayer.TAG.EQUAL_SEPARATOR, 0, 1);

    }


}
