package com.gameplaystudio.combination.gameModes;

import com.gameplaystudio.combination.util.Config;
import com.gameplaystudio.combination.util.Displayer;

import java.util.regex.Pattern;

/**
 * Sub-class of {@link GameMode}<br>
 * In this Game Mode a combination is generated for the computer and the player choose a combination<br>
 * The player and the computer have a limited number of tries to find the combination of the other<br>
 * After each try the player and the computer receive an hint<br>
 * If the player find the combination before the computer he win<br>
 * <i>The number of try and the number of digit in the combination are get from a setting file</i>
 *
 * @see #logic()
 * @see #showHint(String, String)
 * @see #generateCombination()
 * @see Config
 */
public class ModeDuel extends GameMode {

    @Override
    public String getModeName() {
        return Mode.MODE_DUEL.getName();
    }

    @Override
    protected void logic() {
        computerSecretCombination = super.generateCombination();
        logger.debug("(Combinaison secrète de l'ordinateur : " + computerSecretCombination + ")");

        String informationToDisplay = "L'ordinateur devra deviner cette combinaison, ne lui faites pas de cadeau ;)\n";
        informationToDisplay += "De votre coté, vous devrez trouver la combinaison que l'ordinateur a choisi";
        playerSecretCombination = super.chooseCombination(informationToDisplay);


        boolean isPLaying = true;
        boolean playerHasWin = false;
        boolean computerHasWin = false;
        int nbAttempt = 1;

        displayIndication();

        while (isPLaying) { // TODO Cacher la combinaison secrete pour la machine - improve ia
            String complementaryInfoToDisplay = "";

            displayAttemptInfo(nbAttempt);

            String playerGuessToTest = scanner.nextLine();

            if (playerGuessToTest.length() == computerSecretCombination.length() && Pattern.matches("^[0-9]+$", playerGuessToTest)) {
                playerGuess = playerGuessToTest;
                complementaryInfoToDisplay += "Réponse : " + super.showHint(computerSecretCombination, playerGuess) + "\n";
                if (nbAttempt >= Config.maxAttempts) {
                    isPLaying = false;
                }

                if (playerGuess.equals(computerSecretCombination)) {
                    isPLaying = false;
                    playerHasWin = true;
                }

                computerGuess = iaGuessNewCombination(computerGuess, playerSecretCombination);
                complementaryInfoToDisplay += "L'ordinateur propose : " + computerGuess + " -> Réponse : " + super.showHint(playerSecretCombination, computerGuess) + "\n\n";
                if (computerGuess.equals(playerSecretCombination)) {
                    isPLaying = false;
                    computerHasWin = true;
                } else if(isPLaying){
                    nbAttempt++;
                }
            } else {
                complementaryInfoToDisplay += "Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + computerSecretCombination.length() + " chiffres\n";
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
        String textToDisplay = "Devinez la combinaison secrète de l'ordinateur avant qu'il ne trouve la vôtre !\n";

        textToDisplay += "Tappez une combinsaison à " + Config.combinationLength + " chiffres\n";
        textToDisplay += "'=' -> le chiffre est bon\n";
        textToDisplay += "'+' -> le chiffre à trouver est plus grand\n";
        textToDisplay += "'-' -> le chiffre à trouver est plus petit";

        Displayer.displaySemiBoxed(textToDisplay, Displayer.TAG.LINE_SEPARATOR, 1, 1);
    }

    /**
     * Display the attempt number and the max attempt number<br>
     * It also display information about the last player guess
     */
    private void displayAttemptInfo(int nbAttempt) {
        String textToDisplay = "Essai " + nbAttempt + "/" + Config.maxAttempts + "\n";
        if (!playerGuess.equals("")) {
            textToDisplay += " || Votre dernière proposition : " + playerGuess + " -> Réponse : " + super.showHint(computerSecretCombination, playerGuess) + "\n";
            textToDisplay += " || Dernière proposition de l'ordinateur : " + computerGuess + " -> Réponse : " + super.showHint(playerSecretCombination, computerGuess) + "\n";
        }
        textToDisplay += "Nouvelle proposition : ";
        Displayer.displayInline(textToDisplay);
    }

    private void displayGameResult(boolean playerHasWin, boolean computerHasWin, int nbAttempt){
        String textToDisplay = "";
        if (computerHasWin && playerHasWin) {
            textToDisplay += "Égalité ! l'ordinateur et vous, avez trouvé vos combinaisons respectives\n";
            textToDisplay += "Vous avez mis " + nbAttempt + " éssai" + (nbAttempt > 1 ? "s" : "") + "\n";
        } else if (playerHasWin) {
            textToDisplay += "Bravo, vous avez trouvé la combinaison avant l'ordinateur !\n";
            textToDisplay += "Vous avez mis " + nbAttempt + " éssai" + (nbAttempt > 1 ? "s" : "") + "\n";
        } else if (computerHasWin) {
            textToDisplay += "Dommage, l'ordinateur a trouvé la combinaison avant vous...\n";
            textToDisplay += "L'ordinateur a mis " + nbAttempt + " éssai" + (nbAttempt > 1 ? "s" : "") + "\n";
        } else {
            textToDisplay += "Dommage vous avez dépassé les " + Config.maxAttempts + " éssais autorisés !\n";
            textToDisplay += "Ni vous ni l'ordinateur n'avez réussi à trouver la combinaison de l'autre\n";
        }

        textToDisplay += "La combinaison que vous deviez trouver était  | " + computerSecretCombination + " |\n";
        textToDisplay += "La combinaison que l'ordinateur devait trouver était  | " + playerSecretCombination + " |";

        Displayer.displaySemiBoxed(textToDisplay, Displayer.TAG.EQUAL_SEPARATOR, 0, 1);
    }


    /**
     * This method take a combination and return a new combination which closer to the combination to find<br>
     * The hint is composed of '=','-' or '+' chars<br>
     * = -> the digit will stay the same<br>
     * + -> the digit will increment<br>
     * - -> the digit will decrement<br>
     * <i>If it's the first guess (combinationGuess is empty) it return a random combination</i>
     *
     * @param combinationGuess  Combination to change as a String
     * @param combinationToFind Combination to find as a String
     * @return Return the new combination as a String
     * @see super#generateCombination()
     */
    private String iaGuessNewCombination(String combinationGuess, String combinationToFind) { //TODO improve ia
        if (combinationGuess.length() != combinationToFind.length())
            return super.generateCombination();

        StringBuilder newCombination = new StringBuilder();
        for (int i = 0; i < combinationToFind.length(); i++) {
            int combinationDigit = combinationGuess.charAt(i) - '0';
            int combinationToFindDigit = combinationToFind.charAt(i) - '0';

            if (combinationToFindDigit > combinationDigit) {
                combinationDigit++;
            } else if (combinationToFindDigit < combinationDigit) {
                combinationDigit--;
            }
            newCombination.append(combinationDigit);

        }
        return newCombination.toString();
    }
}


