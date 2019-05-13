package com.gameplaystudio.combination.gameModes;

import com.gameplaystudio.combination.util.Config;

import java.util.regex.Pattern;

/**
 * Sub-class of {@link GameMode}<br>
 * In this Game Mode the player choose a combination<br>
 * The player help the ia finding the combination by giving hints
 * The player has a limited number of tries to help the ia finding the combination<br>
 * <i>The number of try and the number of digit in the combination are get from a setting file</i>
 *
 * @see #logic()
 * @see #iaGuessNewCombinationFromHint(String, String)
 * @see #chooseCombination()
 * @see Config
 */
public class ModeDefense extends GameMode {

    @Override
    public String getModeName() {
        return "Mode Defense";
    }

    @Override
    public void start() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Bienvenue dans le mode Défense");
        super.start();
    }

    @Override
    protected void logic() {

        playerSecretCombination = chooseCombination();
        System.out.println("------------------------------------------------------------------");
        System.out.println("Très bon choix !");
        System.out.println("Votre combinaison secrète est | " + playerSecretCombination + " |");

        boolean isPlaying = true;
        boolean hasWin = false;
        int nbAttempts = 1;

        computerGuess = super.generateCombination();


        if (computerGuess.equals(playerSecretCombination)) {
            isPlaying = false;
            hasWin = true;
        } else {
            displayIndication(playerSecretCombination);
            System.out.println("Essai " + nbAttempts + "/" + Config.maxAttempts);
            System.out.println("L'ordinateur n'a pas trouvé votre combinaison et attend votre aide");
            System.out.println("Votre combinaison est | " + playerSecretCombination + " |");
            System.out.println("L'ordinateur à proposé | " + computerGuess + " |");
        }

        while (isPlaying) {
            String playerHint = scanner.nextLine();

            if (playerHint.length() == Config.combinationLength && Pattern.matches("[=+-]+", playerHint)) {
                nbAttempts++;
                computerGuess = iaGuessNewCombinationFromHint(computerGuess, playerHint);
                System.out.println("L'ordinateur propose la combinaison : " + computerGuess);

                if (nbAttempts >= Config.maxAttempts) {
                    isPlaying = false;
                }


                if (playerSecretCombination.equals(computerGuess)) {
                    isPlaying = false;
                    hasWin = true;
                } else if (isPlaying) {
                    System.out.println("Essai " + nbAttempts + "/" + Config.maxAttempts);
                    System.out.println("L'ordinateur n'a pas trouvé votre combinaison et attend votre aide");
                    System.out.println("Votre combinaison est | " + playerSecretCombination + " |");
                    System.out.println("L'ordinateur à proposé | " + computerGuess + " |");
                }
            } else {
                System.out.println("Votre indice n'est pas valide");
                System.out.println("Merci d'entrer un indice constitué de " + Config.combinationLength + " caractères (+ ou - ou =)");
            }

        }

        System.out.println("------------------------------------------------------------------");

        if (hasWin) {
            System.out.println("L'ordinateur à réussi à trouver votre combinaison secrète!");
            System.out.println("Vos indications ont été éfficaces");
            System.out.println("L'ordinateur à mis " + nbAttempts + " éssai" + (nbAttempts > 1 ? "s" : ""));
            System.out.println("La combinaison était  | " + playerSecretCombination + " |");
        } else {
            System.out.println("L'ordinateur n'a pas réussi à trouver votre combinaison secrète !");
            System.out.println("L'ordinateur a dépassé les " + Config.maxAttempts + " éssais autorisés !");
            System.out.println("La combinaison était | " + playerSecretCombination + " |");
            System.out.println("La dernière proposition de l'ordinateur était | " + computerGuess + " |");
        }
        System.out.println("------------------------------------------------------------------");

        super.showReplayMenu();

    }

    /**
     * Show indications about how the game should be played
     */
    private void displayIndication(String combinationToShow) {
        System.out.println("------------------------------------------------------------------");
        System.out.println("L'ordinateur doit trouver votre combinaison : " + combinationToShow);
        System.out.println("Pour l'aider il va falloir lui donner un indice constitué de " + Config.combinationLength + " caractères (+ ou - ou =)");
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
        System.out.println("------------------------------------------------------------------");
        do {
            choice = scanner.nextLine();
            if (Pattern.matches("^[0-9]+$", choice) && choice.length() == Config.combinationLength) {
                validChoice = true;
            } else {
                System.out.println("Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + Config.combinationLength + " chiffres");
            }
        } while (!validChoice);
        return choice;
    }

    /**
     * This method use a combination and a hint and return a new combination based of the hint given<br>
     * The hint is composed of '=','-' or '+' chars<br>
     * = -> the digit will stay the same<br>
     * + -> the digit will increment<br>
     * - -> the digit will decrement
     *
     * @param combination Combination to change as a String
     * @param hint        String of the hint to change the combination
     * @return Return the new combination as a String
     */
    private String iaGuessNewCombinationFromHint(String combination, String hint) {
        if (combination.length() != hint.length())
            throw new IllegalArgumentException("combination and hint arguments must have the same length");

        StringBuilder newCombination = new StringBuilder();
        for (int i = 0; i < hint.length(); i++) {
            int currentNumber = combination.charAt(i) - '0';
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
