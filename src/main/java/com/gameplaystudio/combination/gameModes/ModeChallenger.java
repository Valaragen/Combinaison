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
            String complementaryInfoToDisplay = "";

            displayAttemptInfo(nbAttempt);

            String playerGuessToVerify = scanner.nextLine();

            if (playerGuessToVerify.length() == Config.combinationLength && Pattern.matches("^[0-9]+$", playerGuessToVerify)) {
                playerGuess = playerGuessToVerify;
                complementaryInfoToDisplay += "Réponse : " + super.showHint(computerSecretCombination, playerGuess) + "\n";
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
                complementaryInfoToDisplay += "Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + Config.combinationLength + " chiffres" + "\n";
            }

            if(!complementaryInfoToDisplay.equals("")) {
                Displayer.display(complementaryInfoToDisplay);
            }
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

        textToDisplay += "L'ordinateur a créé une combinaison secrète que vous devez deviner\n";
        textToDisplay += "Celui-ci vous donnera des indices pour vous aiguiller\n";
        textToDisplay += "Tappez une combinsaison à " + Config.combinationLength + " chiffres\n";
        textToDisplay += "'=' -> le chiffre est bon\n";
        textToDisplay += "'+' -> le chiffre à trouver est plus grand\n";
        textToDisplay += "'-' -> le chiffre à trouver est plus petit";


        Displayer.displaySemiBoxed(textToDisplay, Displayer.TAG.LINE_SEPARATOR, 0, 1);
    }

    /**
     * Display the attempt number and the max attempt number<br>
     * It also display information about the last player guess
     */
    private void displayAttemptInfo(int nbAttempt) {
        String textToDisplay = "Essai " + nbAttempt + "/" + Config.maxAttempts + "\n";
        if (!playerGuess.equals("")) {
            textToDisplay += "Dernière proposition : " + playerGuess + " -> Réponse : " + super.showHint(computerSecretCombination, playerGuess) + "\n";
        }
        textToDisplay += "Nouvelle proposition : ";
        Displayer.displayInline(textToDisplay);
    }

    /**
     * Display the game results
     *
     * @param hasWin boolean set to <code>true</code> if the player wined the game
     * @param nbAttempt number of try the player has used to find the combination
     * @see Displayer
     */
    private void displayGameResult(boolean hasWin, int nbAttempt){
        String textToDisplay = "";

        if (hasWin) {
            textToDisplay += "Bravo vous avez trouvé la combinaison !\n";
            textToDisplay += "Vous avez mis " + nbAttempt + " éssai" + (nbAttempt > 1 ? "s" : "") + "\n";
        } else {
            textToDisplay += "Dommage vous avez utilisé les " + Config.maxAttempts + " éssais autorisés !\n";
        }
        textToDisplay += "La combinaison était | " + computerSecretCombination + " |";

        Displayer.displaySemiBoxed(textToDisplay, Displayer.TAG.EQUAL_SEPARATOR, 0, 1);
    }


}
