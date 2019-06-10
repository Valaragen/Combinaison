package com.gameplaystudio.combination.gameModes;

import com.gameplaystudio.combination.util.Config;
import com.gameplaystudio.combination.util.Displayer;

import java.util.regex.Pattern;

/**
 * Sub-class of {@link GameMode}<br>
 * In this Game MODE a combination is generated for the computer and the player choose a combination<br>
 * The player and the computer have a limited number of tries to find the combination of the other<br>
 * After each try the player and the computer receive an hint<br>
 * If the player find the combination before the computer he win<br>
 * <i>The number of try and the number of digit in the combination are get from a setting file</i>
 *
 * @see #logic()
 * @see #generateHint(String, String)
 * @see #generateCombination()
 * @see Config
 */
public class ModeDuel extends GameMode {

    private static final String COMPLEMENTARY_INFORMATION_ABOUT_THE_COMBINATION_TO_CHOOSE = "L'ordinateur devra deviner cette combinaison, ne lui faites pas de cadeau ;)" + Displayer.CARRIAGE_RETURN
            + "De votre coté, vous devrez trouver la combinaison que l'ordinateur a choisi";
    private static final String HOW_TO_PLAY_INDICATION = "Devinez la combinaison secrète de l'ordinateur avant qu'il ne trouve la vôtre !" + Displayer.CARRIAGE_RETURN
            + "Tappez une combinsaison à " + Config.combinationLength + " chiffres" + Displayer.CARRIAGE_RETURN
            + "'=' -> le chiffre est bon" + Displayer.CARRIAGE_RETURN
            + "'+' -> le chiffre à trouver est plus grand" + Displayer.CARRIAGE_RETURN
            + "'-' -> le chiffre à trouver est plus petit";
    private static final String THE_COMPUTER_GUESS = "L'ordinateur propose : ";
    private static final String YOUR_LAST_PROPOSITION = "Votre dernière proposition : ";
    private static final String COMPUTER_LAST_PROPOSITION = "Dernière proposition de l'ordinateur : ";
    private static final String THE_COMPUTER_USED = "L'ordinateur a mis ";
    private static final String THE_COMBINATION_YOU_HAD_TO_FIND_WAS = "La combinaison que vous deviez trouver était";
    private static final String THE_COMBINATION_THE_COMPUTER_HAD_TO_FIND_WAS = "La combinaison que l'ordinateur devait trouver était";
    private static final String COMPUTER_WIN_TEXT = "Dommage, l'ordinateur a trouvé la combinaison avant vous..." + Displayer.CARRIAGE_RETURN;
    private static final String PLAYER_WIN_TEXT = "Bravo, vous avez trouvé la combinaison avant l'ordinateur !" + Displayer.CARRIAGE_RETURN;
    private static final String DRAW_TEXT = "Égalité ! l'ordinateur et vous, avez trouvé vos combinaisons respectives" + Displayer.CARRIAGE_RETURN;

    @Override
    public String getModeName() {
        return MODE.MODE_DUEL.getName();
    }

    @Override
    protected void logic() {
        computerSecretCombination = super.generateCombination();
        logger.debug("(Combinaison secrete de l'ordinateur : " + computerSecretCombination + ")");


        playerSecretCombination = super.chooseCombination(COMPLEMENTARY_INFORMATION_ABOUT_THE_COMBINATION_TO_CHOOSE);


        boolean isPLaying = true;
        boolean playerHasWin = false;
        boolean computerHasWin = false;
        int nbAttempt = 1;

        displayIndication();

        while (isPLaying) {
            String complementaryInfoToDisplay = "";

            displayAttemptInfo(nbAttempt);

            String playerGuessToTest = scanner.nextLine();

            if (playerGuessToTest.length() == computerSecretCombination.length() && Pattern.matches("^[0-9]+$", playerGuessToTest)) {
                playerGuess = playerGuessToTest;
                complementaryInfoToDisplay += ANSWER + super.generateHint(computerSecretCombination, playerGuess) + Displayer.CARRIAGE_RETURN;

                if (playerGuess.equals(computerSecretCombination)) {
                    isPLaying = false;
                    playerHasWin = true;
                }

                computerGuess = super.computerGuessNewCombinationFromHint(super.generateHint(playerSecretCombination, computerGuess), nbAttempt);
                complementaryInfoToDisplay += THE_COMPUTER_GUESS + computerGuess + RIGHTWARD_ARROW + ANSWER + super.generateHint(playerSecretCombination, computerGuess) + Displayer.CARRIAGE_RETURN + Displayer.CARRIAGE_RETURN;
                if (computerGuess.equals(playerSecretCombination)) {
                    isPLaying = false;
                    computerHasWin = true;
                } else if (isPLaying) {
                    nbAttempt++;
                }
            } else {
                complementaryInfoToDisplay += CHOOSE_COMBINATION_ERROR_MESSAGE + computerSecretCombination.length() + DIGITS +Displayer.CARRIAGE_RETURN;
            }

            Displayer.display(complementaryInfoToDisplay);

        }

        displayGameResult(playerHasWin, computerHasWin, nbAttempt);

        super.showReplayMenu();
    }

    /**
     * Show indications about how the game should be played
     */
    private void displayIndication() {
        Displayer.displaySemiBoxed(HOW_TO_PLAY_INDICATION, Displayer.TAG.LINE_SEPARATOR, 1, 1);
    }

    /**
     * Display the attempt number and the max attempt number<br>
     * It also display information about the last player guess
     */
    private void displayAttemptInfo(int nbAttempt) {
        String textToDisplay = ATTEMPT + nbAttempt + Displayer.CARRIAGE_RETURN;
        if (!playerGuess.equals("")) {
            textToDisplay += DOUBLE_PIPE_SEPARATOR + YOUR_LAST_PROPOSITION + playerGuess + RIGHTWARD_ARROW + ANSWER + super.generateHint(computerSecretCombination, playerGuess) + Displayer.CARRIAGE_RETURN
                    + DOUBLE_PIPE_SEPARATOR + COMPUTER_LAST_PROPOSITION + computerGuess + RIGHTWARD_ARROW + ANSWER + super.generateHint(playerSecretCombination, computerGuess) + Displayer.CARRIAGE_RETURN;
        }
        textToDisplay += NEW_PROPOSITION;
        Displayer.displayInline(textToDisplay);
    }

    private void displayGameResult(boolean playerHasWin, boolean computerHasWin, int nbAttempt) {
        String textToDisplay = "";
        if (computerHasWin && playerHasWin) {
            logger.info("Draw");
            textToDisplay += DRAW_TEXT
                    + YOU_USED + nbAttempt + (nbAttempt > 1 ? TRIES : TRY) + Displayer.CARRIAGE_RETURN;
        } else if (playerHasWin) {
            logger.info("Player win");
            textToDisplay += PLAYER_WIN_TEXT
                    + YOU_USED + nbAttempt + (nbAttempt > 1 ? TRIES : TRY) + Displayer.CARRIAGE_RETURN;
        } else if (computerHasWin) {
            logger.info("Machine win");
            textToDisplay += COMPUTER_WIN_TEXT
                    + THE_COMPUTER_USED + nbAttempt + (nbAttempt > 1 ? TRIES : TRY) + Displayer.CARRIAGE_RETURN;
        }

        textToDisplay += THE_COMBINATION_YOU_HAD_TO_FIND_WAS + SINGLE_PIPE_SEPARATOR + computerSecretCombination + SINGLE_PIPE_SEPARATOR + Displayer.CARRIAGE_RETURN
                + THE_COMBINATION_THE_COMPUTER_HAD_TO_FIND_WAS + SINGLE_PIPE_SEPARATOR + playerSecretCombination + SINGLE_PIPE_SEPARATOR;

        Displayer.displaySemiBoxed(textToDisplay, Displayer.TAG.EQUAL_SEPARATOR, 0, 1);
    }
}


