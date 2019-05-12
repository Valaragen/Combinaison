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
    protected void logic() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Bienvenue dans le mode Défense");

        while (run) {
            String playerCombination = chooseCombination();
            System.out.println("------------------------------------------------------------------");
            System.out.println("Très bon choix !");
            System.out.println("Votre combinaison secrète est | " + playerCombination + " |");

            boolean play = true;
            boolean win = false;
            int nbTry = 1;

            String iaCombinationGuess = super.generateCombination();


            if (iaCombinationGuess.equals(playerCombination)) {
                play = false;
                win = true;
            } else {
                displayIndication(playerCombination);
                System.out.println("L'ordinateur n'a pas trouvé votre combinaison et attend votre aide");
                System.out.println("Votre combinaison est | " + playerCombination + " |");
                System.out.println("L'ordinateur à proposé | " + iaCombinationGuess + " |");
            }

            while (play) {
                String playerHint = sc.nextLine();

                if (Pattern.matches("[=+-]+", playerHint) && playerHint.length() == Config.combinationLength) {
                    iaCombinationGuess = iaGuessNewCombinationFromHint(iaCombinationGuess, playerHint);
                    System.out.println("L'ordinateur propose la combinaison : " + iaCombinationGuess);

                    nbTry++;
                    if (nbTry >= Config.nbAllowedTry) {
                        play = false;
                    }


                    if (playerCombination.equals(iaCombinationGuess)) {
                        play = false;
                        win = true;
                    } else if (play) {
                        System.out.println("L'ordinateur n'a pas trouvé votre combinaison et attend votre aide");
                        System.out.println("Votre combinaison est | " + playerCombination + " |");
                        System.out.println("L'ordinateur à proposé | " + iaCombinationGuess + " |");
                    }
                } else {
                    System.out.println("Votre indice n'est pas valide");
                    System.out.println("Merci d'entrer un indice constitué de " + Config.combinationLength + " caractères (+ ou - ou =)");
                }

            }

            if (win) {
                System.out.println("------------------------------------------------------------------");
                System.out.println("L'ordinateur à réussi à trouver votre combinaison secrète!");
                System.out.println("Vos indications ont été éfficaces");
                System.out.println("L'ordinateur à mis " + nbTry + " éssai" + (nbTry > 1 ? "s" : ""));
                System.out.println("La combinaison était  | " + playerCombination + " |");
                System.out.println("------------------------------------------------------------------");
            } else {
                System.out.println("------------------------------------------------------------------");
                System.out.println("L'ordinateur n'a pas réussi à trouver votre combinaison secrète !");
                System.out.println("L'ordinateur a dépassé les " + Config.nbAllowedTry + " éssais autorisés !");
                System.out.println("La combinaison était | " + playerCombination + " |");
                System.out.println("La dernière proposition de l'ordinateur était | " + iaCombinationGuess + " |");
                System.out.println("------------------------------------------------------------------");
            }

            super.showReplayMenu();

        }
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
            choice = sc.nextLine();
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
    private String iaGuessNewCombinationFromHint(String combination, String hint) { //TODO enhance the ia
        if (combination.length() != hint.length())
            throw new IllegalArgumentException("combination and hint arguments must have the same length");//TODO handle this exceptions + add other Exception

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
