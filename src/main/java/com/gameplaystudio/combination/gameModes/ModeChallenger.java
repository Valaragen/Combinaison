package com.gameplaystudio.combination.gameModes;

import com.gameplaystudio.combination.util.Config;
import com.gameplaystudio.combination.util.Displayer;

import java.util.regex.Pattern;

/**
 * Sub-class of {@link GameMode}<br>
 * In this Game MODE a combination is generated<br>
 * The player has a limited number of tries to find the generated combination<br>
 * After each try the player receive an hint
 * <i>The number of try and the number of digit in the combination are get from a setting file</i>
 *
 * @see #logic()
 * @see #generateHint(String, String)
 * @see #generateCombination()
 * @see Config
 */
public class ModeChallenger extends GameMode {

    private static final String LAST_PROPOSITION = "Dernière proposition : ";
    private static final String PLAYER_WIN_TEXT = "Bravo vous avez trouvé la combinaison !" + Displayer.CARRIAGE_RETURN;
    private static final String PLAYER_LOOSE_TEXT = "Dommage vous avez utilisé les ";
    private static final String HOW_TO_PLAY_INDICATION_PART1 = "L'ordinateur a créé une combinaison secrète que vous devez deviner" + Displayer.CARRIAGE_RETURN
            + "Celui-ci vous donnera des indices pour vous aiguiller" + Displayer.CARRIAGE_RETURN
            + "Tappez une combinsaison à ";
    private static final String HOW_TO_PLAY_INDICATIONS_PART2 = DIGITS + Displayer.CARRIAGE_RETURN
            + "'=' -> le chiffre est bon" + Displayer.CARRIAGE_RETURN
            + "'+' -> le chiffre à trouver est plus grand" + Displayer.CARRIAGE_RETURN
            + "'-' -> le chiffre à trouver est plus petit";

    @Override
    public String getModeName() {
        return MODE.MODE_CHALLENGER.getName();
    }

    @Override
    protected void logic() {
        computerSecretCombination = super.generateCombination();
        logger.debug("(Combinaison secrete : " + computerSecretCombination + ")");


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
                complementaryInfoToDisplay += ANSWER + super.generateHint(computerSecretCombination, playerGuess) + Displayer.CARRIAGE_RETURN;
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
                complementaryInfoToDisplay += CHOOSE_COMBINATION_ERROR_MESSAGE + Config.combinationLength + DIGITS + Displayer.CARRIAGE_RETURN;
            }

            if (!complementaryInfoToDisplay.equals("")) {
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
        String textToDisplay = HOW_TO_PLAY_INDICATION_PART1 + Config.combinationLength + HOW_TO_PLAY_INDICATIONS_PART2;


        Displayer.displaySemiBoxed(textToDisplay, Displayer.TAG.LINE_SEPARATOR, 0, 1);
    }

    /**
     * Display the attempt number and the max attempt number<br>
     * It also display information about the last player guess
     */
    private void displayAttemptInfo(int nbAttempt) {
        String textToDisplay = ATTEMPT + nbAttempt + SLASH_SEPARATOR + Config.maxAttempts + Displayer.CARRIAGE_RETURN;
        if (!playerGuess.equals("")) {
            textToDisplay += LAST_PROPOSITION + playerGuess + RIGHTWARD_ARROW + ANSWER + super.generateHint(computerSecretCombination, playerGuess) + Displayer.CARRIAGE_RETURN;
        }
        textToDisplay += NEW_PROPOSITION;
        Displayer.displayInline(textToDisplay);
    }

    /**
     * Display the game results
     *
     * @param hasWin    boolean set to <code>true</code> if the player wined the game
     * @param nbAttempt number of try the player has used to find the combination
     * @see Displayer
     */
    private void displayGameResult(boolean hasWin, int nbAttempt) {
        String textToDisplay = "";

        if (hasWin) {
            logger.info("Player win");
            textToDisplay += PLAYER_WIN_TEXT
                    + YOU_USED + nbAttempt + (nbAttempt > 1 ? TRIES : TRY) + Displayer.CARRIAGE_RETURN;
        } else {
            logger.info("Player loose");
            textToDisplay += PLAYER_LOOSE_TEXT + Config.maxAttempts + ALLOWED_TRIES;
        }
        textToDisplay += THE_COMBINATION_WAS + SINGLE_PIPE_SEPARATOR + computerSecretCombination + SINGLE_PIPE_SEPARATOR;

        Displayer.displaySemiBoxed(textToDisplay, Displayer.TAG.EQUAL_SEPARATOR, 0, 1);
    }


}
