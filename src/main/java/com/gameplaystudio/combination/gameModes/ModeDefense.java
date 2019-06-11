package com.gameplaystudio.combination.gameModes;

import com.gameplaystudio.combination.util.Config;
import com.gameplaystudio.combination.util.Displayer;

import java.util.regex.Pattern;

/**
 * Sub-class of {@link GameMode}<br>
 * In this Game MODE the player choose a combination<br>
 * The player help the ia finding the combination by giving hints
 * The player has a limited number of tries to help the ia finding the combination<br>
 * <i>The number of try and the number of digit in the combination are get from a setting file</i>
 *
 * @see #logic()
 * @see GameMode#computerGuessNewCombinationFromHint(String, int)
 * @see Config
 */
public class ModeDefense extends GameMode {
    private static final String COMPLEMENTARY_INFORMATION_ABOUT_THE_COMBINATION_TO_CHOOSE = "L'ordinateur devra deviner cette combinaison";
    private static final String BINGO = "==== Bingo !";
    private static final String CHOOSE_HINT_ERROR_MESSAGE = "Votre indice n'est pas valide" + Displayer.CARRIAGE_RETURN
            + "Merci d'entrer un indice constitué de ";
    private static final String CHARACTERS_INDICATION = " caractères (+ ou - ou =)";
    private static final String HOW_TO_PLAY_INDICATION_PART1 = "L'ordinateur doit trouver votre combinaison : ";
    private static final String HOW_TO_PLAY_INDICATION_PART2 = "Pour l'aider, il va falloir lui donner un indice constitué de ";
    private static final String HOW_TO_PLAY_INDICATION_PART3 = CHARACTERS_INDICATION + Displayer.CARRIAGE_RETURN
            + "'=' -> le chiffre est bon" + Displayer.CARRIAGE_RETURN
            + "'+' -> le chiffre à trouver est plus grand" + Displayer.CARRIAGE_RETURN
            + "'-' -> le chiffre à trouver est plus petit";
    private static final String YOUR_COMBINATION = "Votre combinaison";
    private static final String YOUR_HINT = "Votre indice : ";
    private static final String COMPUTER_GESSED = "L'ordinateur à proposé";
    private static final String COMPUTER_WIN_TEXT = "L'ordinateur à réussi à trouver votre combinaison secrète!" + Displayer.CARRIAGE_RETURN
            + "Vos indications ont été éfficaces" + Displayer.CARRIAGE_RETURN;
    private static final String COMPUTER_LOOSE_TEXT = "L'ordinateur n'a pas réussi à trouver votre combinaison secrète !" + Displayer.CARRIAGE_RETURN;
    private static final String COMPUTER_USED = "L'ordinateur à mis ";
    private static final String COMPUTER_USED_THE = "L'ordinateur a utilisé les ";
    private static final String THE_LAST_COMPUTER_GUESS_WAS = "La dernière proposition de l'ordinateur était";
    private static final String PLAYER_IS_CHEATING_TEXT = "Votre indice est valide cependant il ne donne pas les bonnes indications à l'ordinateur, veuillez réesayer" + Displayer.CARRIAGE_RETURN;
    private static final String PLAYER_IS_CHEATING_HELP_TEXT = "AIDE : L'indice attendu est";


    @Override
    public String getModeName() {
        return MODE.MODE_DEFENSE.getName();
    }

    @Override
    protected void logic() {

        playerSecretCombination = chooseCombination(COMPLEMENTARY_INFORMATION_ABOUT_THE_COMBINATION_TO_CHOOSE);

        String hintForComputer = "";
        boolean computerCanGuess = true;

        boolean isPlaying = true;
        boolean hasWin = false;
        int nbAttempt = 0;
        int nbCheatInARow = 0;

        displayIndication(playerSecretCombination);

        while (isPlaying) {
            String complementaryInfoToDisplay = "";

            if (computerCanGuess) {
                computerGuess = super.computerGuessNewCombinationFromHint(hintForComputer, nbAttempt);
                nbAttempt++;
                computerCanGuess = false;
            }

            logger.debug("(Solution : " + super.generateHint(playerSecretCombination, computerGuess) + ")");
            displayAttemptInfo(nbAttempt);

            if (nbAttempt >= Config.maxAttempts) {
                isPlaying = false;
                complementaryInfoToDisplay = super.generateHint(playerSecretCombination, computerGuess);
            }

            if (playerSecretCombination.equals(computerGuess)) {
                complementaryInfoToDisplay = BINGO;
                isPlaying = false;
                hasWin = true;
            } else if (isPlaying) {
                hintForComputer = scanner.nextLine();
                if (hintForComputer.length() == Config.combinationLength && Pattern.matches("[=+-]+", hintForComputer)) {
                    if(!isPlayerCheating(hintForComputer)) {
                        computerCanGuess = true;
                        nbCheatInARow = 0;
                    } else {
                        complementaryInfoToDisplay += PLAYER_IS_CHEATING_TEXT;
                        nbCheatInARow++;
                        if (nbCheatInARow % 3 == 0) {
                            complementaryInfoToDisplay += PLAYER_IS_CHEATING_HELP_TEXT + DOUBLE_PIPE_SEPARATOR + super.generateHint(playerSecretCombination, computerGuess) + DOUBLE_PIPE_SEPARATOR + Displayer.CARRIAGE_RETURN;
                            nbCheatInARow = 0;
                        }
                    }
                } else {
                    complementaryInfoToDisplay += CHOOSE_HINT_ERROR_MESSAGE + Config.combinationLength + CHARACTERS_INDICATION + Displayer.CARRIAGE_RETURN;
                }
            }

            Displayer.display(complementaryInfoToDisplay);

        }

        displayGameResult(hasWin, nbAttempt);

        super.showReplayMenu();

    }

    /**
     * Show indications about how the game should be played
     */
    private void displayIndication(String combinationToShow) {
        String indicationToDisplay = "";
        indicationToDisplay += HOW_TO_PLAY_INDICATION_PART1 + combinationToShow + Displayer.CARRIAGE_RETURN
                + HOW_TO_PLAY_INDICATION_PART2 + Config.combinationLength + HOW_TO_PLAY_INDICATION_PART3;
        Displayer.displaySemiBoxed(indicationToDisplay, Displayer.TAG.LINE_SEPARATOR, 1, 1);
    }

    /**
     * Display the attempt number and the max attempt number and some indications
     */
    private void displayAttemptInfo(int nbAttempt) {
        String textToDisplay = ATTEMPT + nbAttempt + SLASH_SEPARATOR + Config.maxAttempts + RIGHTWARD_ARROW + YOUR_COMBINATION + SINGLE_PIPE_SEPARATOR + playerSecretCombination + SINGLE_PIPE_SEPARATOR + Displayer.CARRIAGE_RETURN
                + COMPUTER_GESSED + SINGLE_PIPE_SEPARATOR + computerGuess + SINGLE_PIPE_SEPARATOR + Displayer.CARRIAGE_RETURN
                + YOUR_HINT;
        Displayer.displayInline(textToDisplay);
    }

    private boolean isPlayerCheating(String hintForComputer) {
        boolean isPlayerCheating = false;
        if (!hintForComputer.equals(super.generateHint(playerSecretCombination, computerGuess))){
            isPlayerCheating = true;
        }
        return isPlayerCheating;
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
            logger.info("Computer win");
            textToDisplay += COMPUTER_WIN_TEXT
                    + COMPUTER_USED + nbAttempt + (nbAttempt > 1 ? TRIES : TRY) + Displayer.CARRIAGE_RETURN
                    + THE_COMBINATION_WAS + SINGLE_PIPE_SEPARATOR + playerSecretCombination + SINGLE_PIPE_SEPARATOR;
        } else {
            logger.info("Computer loose");
            textToDisplay += COMPUTER_LOOSE_TEXT
                    + COMPUTER_USED_THE + Config.maxAttempts + ALLOWED_TRIES
                    + THE_COMBINATION_WAS + SINGLE_PIPE_SEPARATOR + playerSecretCombination + SINGLE_PIPE_SEPARATOR + Displayer.CARRIAGE_RETURN
                    + THE_LAST_COMPUTER_GUESS_WAS + SINGLE_PIPE_SEPARATOR + computerGuess + SINGLE_PIPE_SEPARATOR;
        }


        Displayer.displaySemiBoxed(textToDisplay, Displayer.TAG.EQUAL_SEPARATOR, 1, 1);
    }


}
