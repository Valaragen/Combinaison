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


        computerGuess = "";
        boolean isPLaying = true;
        boolean playerHasWin = false;
        boolean computerHasWin = false;
        int nbAttempt = 0;

        displayIndication();

        while (isPLaying) { // TODO Cacher la combinaison secrete pour la machine - improve ia
            playerGuess = scanner.nextLine();

            if (playerGuess.length() == computerSecretCombination.length() && Pattern.matches("^[0-9]+$", playerGuess)) {
                nbAttempt++;
                System.out.println("Essai " + nbAttempt + "/" + Config.maxAttempts + " | Votre proposition : " + playerGuess + " -> Réponse : " + super.showHint(computerSecretCombination, playerGuess));
                if (nbAttempt >= Config.maxAttempts) {
                    isPLaying = false;
                }

                if (playerGuess.equals(computerSecretCombination)) {
                    isPLaying = false;
                    playerHasWin = true;
                }
                computerGuess = iaGuessNewCombination(computerGuess, playerSecretCombination);
                System.out.println("Essai " + nbAttempt + "/" + Config.maxAttempts + " | L'ordinateur propose : " + computerGuess + " -> Réponse : " + super.showHint(playerSecretCombination, computerGuess));
                if (computerGuess.equals(playerSecretCombination)) {
                    isPLaying = false;
                    computerHasWin = true;
                }
            } else {
                System.out.println("Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + computerSecretCombination.length() + " chiffres");
            }

        }

        System.out.println("------------------------------------------------------------------");

        if (computerHasWin && playerHasWin) {
            System.out.println("Égalité ! l'ordinateur et vous, avez trouvé vos combinaisons respectives");
            System.out.println("Vous avez mis " + nbAttempt + " éssai" + (nbAttempt > 1 ? "s" : ""));
        } else if (playerHasWin) {
            System.out.println("Bravo, vous avez trouvé la combinaison avant l'ordinateur !");
            System.out.println("Vous avez mis " + nbAttempt + " éssai" + (nbAttempt > 1 ? "s" : ""));
        } else if (computerHasWin) {
            System.out.println("Dommage, l'ordinateur a trouvé la combinaison avant vous...");
            System.out.println("L'ordinateur a mis " + nbAttempt + " éssai" + (nbAttempt > 1 ? "s" : ""));
        } else {
            System.out.println("Dommage vous avez dépassé les " + Config.maxAttempts + " éssais autorisés !");
            System.out.println("Ni vous ni l'ordinateur n'avez réussi à trouver la combinaison de l'autre");
        }

        System.out.println("La combinaison que vous deviez trouver était  | " + computerSecretCombination + " |");
        System.out.println("La combinaison que l'ordinateur devait trouver était  | " + playerSecretCombination + " |");
        System.out.println("------------------------------------------------------------------");

        super.showReplayMenu();
    }

    /**
     * Show indications about how the game should be played
     */
    private void displayIndication() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Devinez la combinaison secrète de l'ordinateur avant qu'il ne trouve la vôtre !");
        System.out.println("Tappez une combinsaison à " + Config.combinationLength + " chiffres");
        System.out.println("'=' -> le chiffre est bon");
        System.out.println("'+' -> le chiffre à trouver est plus grand");
        System.out.println("'-' -> le chiffre à trouver est plus petit");
        System.out.println("------------------------------------------------------------------");
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


