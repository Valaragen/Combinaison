package com.gameplaystudio.combination.gameModes;

import com.gameplaystudio.combination.util.Config;
import com.gameplaystudio.combination.util.Displayer;

import java.util.regex.Pattern;

/**
 * Sub-class of {@link GameMode}<br>
 * In this Game Mode the player choose a combination<br>
 * The player help the ia finding the combination by giving hints
 * The player has a limited number of tries to help the ia finding the combination<br>
 * <i>The number of try and the number of digit in the combination are get from a setting file</i>
 *
 * @see #logic()
 * @see #computerGuessNewCombinationFromHint(String, String)
 * @see #chooseCombination()
 * @see Config
 */
public class ModeDefense extends GameMode {

    @Override
    public String getModeName() {
        return Mode.MODE_DEFENSE.getName();
    }

    @Override
    protected void logic() {

        playerSecretCombination = chooseCombination();
        computerGuess = "";

        String hintForComputer = "";
        boolean computerCanGuess = true;

        boolean isPlaying = true;
        boolean hasWin = false;
        int nbAttempt = 0;

        if (computerGuess.equals(playerSecretCombination)) {
            displayAttemptInfo(nbAttempt, playerSecretCombination, computerGuess);
            isPlaying = false;
            hasWin = true;
        } else {
            displayIndication(playerSecretCombination);
        }

        while (isPlaying) {
            if(computerCanGuess){
                computerGuess = computerGuessNewCombinationFromHint(computerGuess, hintForComputer);
                nbAttempt++;
                computerCanGuess= false;
            }

            displayAttemptInfo(nbAttempt, playerSecretCombination, computerGuess);

            if (nbAttempt >= Config.maxAttempts) {
                isPlaying = false;
            }

            if (playerSecretCombination.equals(computerGuess)) {
                isPlaying = false;
                hasWin = true;
            } else if (isPlaying) {
                hintForComputer = scanner.nextLine();
                if (hintForComputer.length() == Config.combinationLength && Pattern.matches("[=+-]+", hintForComputer)) {
                    computerCanGuess = true;
                } else {
                    String errorToDisplay = "Votre indice n'est pas valide\n";
                    errorToDisplay += "Merci d'entrer un indice constitué de " + Config.combinationLength + " caractères (+ ou - ou =)\n";
                    Displayer.display(errorToDisplay);
                }
            }

        }

        displayGameResult(hasWin, nbAttempt);

        super.showReplayMenu();

    }

    /**
     * This method ask the player to enter a valid combination<br>
     * It return the choice of the player when the combination match the requirements<br>
     *
     * @return Return the player combination as a string
     */
    private String chooseCombination() {
        boolean choiceIsValid = false;
        String choice;

        String indicationToDisplay = "Veuillez définir une combinaison de " + Config.combinationLength + " chiffres";

        Displayer.displaySemiBoxed(indicationToDisplay, Displayer.TAG.LINE_SEPARATOR);

        do {
            choice = scanner.nextLine();
            String resultToDisplay;
            if (Pattern.matches("^[0-9]+$", choice) && choice.length() == Config.combinationLength) {
                resultToDisplay = "Très bon choix !";
                choiceIsValid = true;
            } else {
                resultToDisplay = "Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + Config.combinationLength + " chiffres";
            }
            Displayer.display(resultToDisplay);
        } while (!choiceIsValid);
        return choice;
    }

    /**
     * Show indications about how the game should be played
     */
    private void displayIndication(String combinationToShow) {
        String indicationToDisplay = "";
        indicationToDisplay += "L'ordinateur doit trouver votre combinaison : " + combinationToShow + "\n";
        indicationToDisplay += "Pour l'aider il va falloir lui donner un indice constitué de " + Config.combinationLength + " caractères (+ ou - ou =)\n";
        indicationToDisplay += "'=' -> le chiffre est bon\n";
        indicationToDisplay += "'+' -> le chiffre à trouver est plus grand\n";
        indicationToDisplay += "'-' -> le chiffre à trouver est plus petit";
        Displayer.displaySemiBoxed(indicationToDisplay, Displayer.TAG.LINE_SEPARATOR, 1, 1);
    }

    /**
     * Display the attempt number and the max attempt number and some indications
     */
    private void displayAttemptInfo(int nbAttempt, String playerSecretCombination, String computerGuess) {
        String textToDisplay = "";
        textToDisplay += "Essai " + nbAttempt + "/" + Config.maxAttempts + " -> votre combinaison | " + playerSecretCombination + " |\n";
        textToDisplay += "L'ordinateur à proposé | " + computerGuess + " |";
        Displayer.display(textToDisplay);
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
            textToDisplay += "L'ordinateur à réussi à trouver votre combinaison secrète!\n";
            textToDisplay += "Vos indications ont été éfficaces\n";
            textToDisplay += "L'ordinateur à mis " + nbAttempt + " éssai" + (nbAttempt > 1 ? "s" : "") +"\n";
            textToDisplay += "La combinaison était  | " + playerSecretCombination + " |";
        } else {
            textToDisplay += "L'ordinateur n'a pas réussi à trouver votre combinaison secrète !\n";
            textToDisplay += "L'ordinateur a utilisé les " + Config.maxAttempts + " éssais autorisés !\n";
            textToDisplay += "La combinaison était | " + playerSecretCombination + " |\n";
            textToDisplay += "La dernière proposition de l'ordinateur était | " + computerGuess + " |";
        }


        Displayer.displaySemiBoxed(textToDisplay, Displayer.TAG.EQUAL_SEPARATOR, 0, 1);
    }

    /**
     * This method use a combination and a hint and return a new combination based of the hint given<br>
     * The hint is composed of '=','-' or '+' chars<br>
     * = -> the digit will stay the same<br>
     * + -> the digit will increment<br>
     * - -> the digit will decrement
     *
     * @param currentCombination Combination to change as a String
     * @param hint        String of the hint to change the combination
     * @return Return the new combination as a String
     */
    private String computerGuessNewCombinationFromHint(String currentCombination, String hint) { //TODO improve ia
        if (currentCombination.isEmpty()){
            return super.generateCombination();
        }
        if (currentCombination.length() != hint.length()){
            logger.debug("The combination and the hint given doesn't have the same length, the can't guess correctly");
            return super.generateCombination();
        }

        StringBuilder newCombination = new StringBuilder();
        for (int i = 0; i < hint.length(); i++) {
            int currentNumber = currentCombination.charAt(i) - '0';
            if (hint.charAt(i) == '+' && currentNumber < 9) {
                currentNumber++;
            } else if (hint.charAt(i) == '-' && currentNumber > 0) {
                currentNumber--;
            }
            newCombination.append(currentNumber);

        }
        return newCombination.toString();
    }

}
