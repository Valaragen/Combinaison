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
 * @see GameMode#computerGuessNewCombinationFromHint(String, int)
 * @see Config
 */
public class ModeDefense extends GameMode {

    @Override
    public String getModeName() {
        return Mode.MODE_DEFENSE.getName();
    }

    @Override
    protected void logic() {

        String informationToDisplay = "L'ordinateur devra deviner cette combinaison";
        playerSecretCombination = chooseCombination(informationToDisplay);

        String hintForComputer = "";
        boolean computerCanGuess = true;

        boolean isPlaying = true;
        boolean hasWin = false;
        int nbAttempt = 0;

        displayIndication(playerSecretCombination);

        while (isPlaying) {
            String complementaryInfoToDisplay = "";

            if(computerCanGuess){
                computerGuess = super.computerGuessNewCombinationFromHint(hintForComputer, nbAttempt);
                nbAttempt++;
                computerCanGuess= false;
            }

            displayAttemptInfo(nbAttempt);

            if (nbAttempt >= Config.maxAttempts) {
                isPlaying = false;
                complementaryInfoToDisplay = super.generateHint(playerSecretCombination, computerGuess);
            }

            if (playerSecretCombination.equals(computerGuess)) {
                complementaryInfoToDisplay = "==== Bingo !";
                isPlaying = false;
                hasWin = true;
            } else if (isPlaying) {
                hintForComputer = scanner.nextLine();
                if (hintForComputer.length() == Config.combinationLength && Pattern.matches("[=+-]+", hintForComputer)) {
                    computerCanGuess = true;
                } else {
                    complementaryInfoToDisplay += "Votre indice n'est pas valide\n";
                    complementaryInfoToDisplay += "Merci d'entrer un indice constitué de " + Config.combinationLength + " caractères (+ ou - ou =)\n";
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
        indicationToDisplay += "L'ordinateur doit trouver votre combinaison : " + combinationToShow + "\n";
        indicationToDisplay += "Pour l'aider, il va falloir lui donner un indice constitué de " + Config.combinationLength + " caractères (+ ou - ou =)\n";
        indicationToDisplay += "'=' -> le chiffre est bon\n";
        indicationToDisplay += "'+' -> le chiffre à trouver est plus grand\n";
        indicationToDisplay += "'-' -> le chiffre à trouver est plus petit";
        Displayer.displaySemiBoxed(indicationToDisplay, Displayer.TAG.LINE_SEPARATOR, 1, 1);
    }

    /**
     * Display the attempt number and the max attempt number and some indications
     */
    private void displayAttemptInfo(int nbAttempt) {
        String textToDisplay = "Essai " + nbAttempt + "/" + Config.maxAttempts + " -> votre combinaison | " + playerSecretCombination + " |\n";
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


        Displayer.displaySemiBoxed(textToDisplay, Displayer.TAG.EQUAL_SEPARATOR, 1, 1);
    }


}
