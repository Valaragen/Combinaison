package com.gameplaystudio.combination.gameModes;

import com.gameplaystudio.combination.util.Config;

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
        return "Mode Duel";
    }

    @Override
    protected void logic() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Bienvenue dans le mode Duel");

        while (run) {
            String playerCombinationToFind = super.generateCombination();
            logger.debug("(Combinaison secrète de l'ordinateur : " + playerCombinationToFind + ")");

            String iaCombinationToFind = chooseCombination();
            System.out.println("------------------------------------------------------------------");
            System.out.println("Très bon choix !");
            System.out.println("Votre combinaison secrète est | " + iaCombinationToFind + " |");

            String playerCombinationGuess;
            String iaCombinationGuess = "";
            boolean play = true;
            boolean playerWin = false;
            boolean computerWin = false;
            int nbTry = 0;

            displayIndication();

            while (play) { // TODO Cacher la combinaison secrete pour la machine
                playerCombinationGuess = sc.nextLine();

                if (playerCombinationGuess.length() == playerCombinationToFind.length() && Pattern.matches("^[0-9]+$", playerCombinationGuess)) {
                    nbTry++;
                    System.out.println("Essai " + nbTry + "/" + Config.nbAllowedTry + " | Votre proposition : " + playerCombinationGuess + " -> Réponse : " + super.showHint(playerCombinationToFind, playerCombinationGuess));
                    if (nbTry >= Config.nbAllowedTry) {
                        play = false;
                    }

                    if (playerCombinationGuess.equals(playerCombinationToFind)) {
                        play = false;
                        playerWin = true;
                    }
                    iaCombinationGuess = iaGuessNewCombination(iaCombinationGuess, iaCombinationToFind);
                    System.out.println("Essai " + nbTry + "/" + Config.nbAllowedTry + " | L'ordinateur propose : " + iaCombinationGuess + " -> Réponse : " + super.showHint(iaCombinationToFind, iaCombinationGuess));
                    if (iaCombinationGuess.equals(iaCombinationToFind)) {
                        play = false;
                        computerWin = true;
                    }
                } else {
                    System.out.println("Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + playerCombinationToFind.length() + " chiffres");
                }

            }

            System.out.println("------------------------------------------------------------------");

            if (computerWin && playerWin){
                System.out.println("Égalité ! l'ordinateur et vous avez trouvé vos combinaisons respectives");
                System.out.println("Vous avez mis " + nbTry + " éssai" + (nbTry > 1 ? "s" : ""));
            } else if (playerWin) {
                System.out.println("Bravo, vous avez trouvé la combinaison avant l'ordinateur !");
                System.out.println("Vous avez mis " + nbTry + " éssai" + (nbTry > 1 ? "s" : ""));
            } else if (computerWin) {
                System.out.println("Dommage, l'ordinateur a trouvé la combinaison avant vous...");
                System.out.println("L'ordinateur a mis " + nbTry + " éssai" + (nbTry > 1 ? "s" : ""));
            } else {
                System.out.println("Dommage vous avez dépassé les " + Config.nbAllowedTry + " éssais autorisés !");
                System.out.println("Ni vous ni l'ordinateur n'avez réussi à trouver la combinaison de l'autre");
            }

            System.out.println("La combinaison que vous deviez trouver était  | " + playerCombinationToFind + " |");
            System.out.println("La combinaison que l'ordinateur devait trouver était  | " + iaCombinationToFind + " |");
            System.out.println("------------------------------------------------------------------");


            super.showReplayMenu();
        }
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
     * This method ask the player to enter a valid combination<br>
     * It return the choice of the player when the combination match the requirements<br>
     *
     * @return Return the player combination as a string
     */
    private String chooseCombination() {
        boolean validChoice = false;
        String choice;
        System.out.println("------------------------------------------------------------------");
        System.out.println("Veuillez définir une combinaison de " + Config.combinationLength + " chiffres");
        System.out.println("L'ordinateur devra deviner cette combinaison ne lui faites pas de cadeau");
        System.out.println("De votre coté, vous devrez trouver la combinaison que l'ordinateur a choisi");
        System.out.println("------------------------------------------------------------------");
        do {
            choice = sc.nextLine();
            if (Pattern.matches("[0-9]+", choice) && choice.length() == Config.combinationLength) {
                validChoice = true;
            } else {
                System.out.println("Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + Config.combinationLength + " chiffres");
            }
        } while (!validChoice);
        return choice;
    }

    /**
     * This method take a combination and return a new combination which closer to the combination to find<br>
     * The hint is composed of '=','-' or '+' chars<br>
     * = -> the digit will stay the same<br>
     * + -> the digit will increment<br>
     * - -> the digit will decrement<br>
     * <i>If it's the first guess (combinationGuess is empty) it return a random combination</i>
     *
     * @see super#generateCombination()
     * @param combinationGuess  Combination to change as a String
     * @param combinationToFind Combination to find as a String
     * @return Return the new combination as a String
     */
    private String iaGuessNewCombination(String combinationGuess, String combinationToFind) {
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


