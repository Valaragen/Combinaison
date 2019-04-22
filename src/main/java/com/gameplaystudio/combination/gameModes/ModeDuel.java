package com.gameplaystudio.combination.gameModes;

import com.gameplaystudio.combination.CombinationGame;
import com.gameplaystudio.combination.util.Combination;

import java.util.regex.Pattern;

public class ModeDuel extends GameMode {
    private Combination combination = new Combination();

    @Override
    public String getNameInMenu() {
        return "Mode Duel";
    }

    @Override
    protected void logic() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Bienvenue dans le mode Duel");
        while (run) {
            String playerCombinationToFind = combination.generateCombination().getCombination();
            CombinationGame.logger.debug("(Combinaison secrète de l'ordinateur : " + playerCombinationToFind + ")");//TODO add a dev condition

            String iaCombinationToFind = chooseCombination();
            System.out.println("------------------------------------------------------------------");
            System.out.println("Très bon choix !");
            System.out.println("Votre combinaison secrète est | " + iaCombinationToFind + " |");

            String playerCombinationGuess;
            String iaCombinationGuess = "";
            boolean play = true;
            boolean win = false;
            int nbTry = 0;
            int nbAllowedTry = 10;

            displayIndication();

            while (play) {
                playerCombinationGuess = sc.nextLine();

                if (Pattern.matches("[0-9]+", playerCombinationGuess) && playerCombinationGuess.length() == combination.getLength()) {
                    System.out.println("Votre proposition : " + playerCombinationGuess + " -> Réponse : " + showHint(playerCombinationToFind, playerCombinationGuess));
                    nbTry++;
                    if (nbTry >= nbAllowedTry) {
                        play = false;
                    }

                    if (playerCombinationGuess.equals(playerCombinationToFind)) {
                        play = false;
                        win = true;
                    } else {
                        iaCombinationGuess = iaGuessNewCombination(iaCombinationGuess, iaCombinationToFind);
                        System.out.println("L'ordinateur propose : " + iaCombinationGuess + " -> Réponse : " + showHint(iaCombinationToFind, iaCombinationGuess));
                        if (iaCombinationGuess.equals(iaCombinationToFind)) {
                            play = false;
                        }
                    }
                } else {
                    System.out.println("Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + combination.getLength() + " chiffres");
                }

            }

            if (win) {
                System.out.println("------------------------------------------------------------------");
                System.out.println("Bravo vous avez trouvé la combinaison avant l'ordinateur !");
                System.out.println("Vous avez mis " + nbTry + " éssai" + (nbTry>1?"s":""));
                System.out.println("La combinaison que vous deviez trouver était  | " + playerCombinationToFind + " |");
                System.out.println("La combinaison que l'ordinateur devait trouver était  | " + iaCombinationToFind + " |");
                System.out.println("------------------------------------------------------------------");
            } else if (iaCombinationGuess.equals(iaCombinationToFind)) {
                System.out.println("------------------------------------------------------------------");
                System.out.println("Dommage l'ordinateur a trouvé la combinaison avant vous...");
                System.out.println("L'ordinateur a mis " + nbTry + " éssai" + (nbTry>1?"s":""));
                System.out.println("La combinaison que vous deviez trouver était  | " + playerCombinationToFind + " |");
                System.out.println("La combinaison que l'ordinateur devait trouver était  | " + iaCombinationToFind + " |");
                System.out.println("------------------------------------------------------------------");

            } else {
                System.out.println("------------------------------------------------------------------");
                System.out.println("Dommage vous avez dépassé les " + nbAllowedTry + " éssais autorisés !");
                System.out.println("Ni vous ni l'ordinateur n'avez réussi à trouver la combinaison de l'autre");
                System.out.println("La combinaison que vous deviez trouver était  | " + playerCombinationToFind + " |");
                System.out.println("La combinaison que l'ordinateur devait trouver était  | " + iaCombinationToFind + " |");
                System.out.println("------------------------------------------------------------------");
            }

            showReplayMenu();
        }
    }

    private String chooseCombination() {
        boolean validChoice = false;
        String choice;
        System.out.println("------------------------------------------------------------------");
        System.out.println("Veuillez définir une combinaison de " + combination.getLength() + " chiffres");
        System.out.println("L'ordinateur devra deviner cette combinaison ne lui faites pas de cadeau");
        System.out.println("De votre coté, vous devrez trouver la combinaison que l'ordinateur a choisi");
        System.out.println("------------------------------------------------------------------");
        do {
            choice = sc.nextLine();
            if (Pattern.matches("[0-9]+", choice) && choice.length() == combination.getLength()) {
                validChoice = true;
            } else {
                System.out.println("Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + combination.getLength() + " chiffres");
            }
        } while (!validChoice);
        return choice;
    }

    private void displayIndication() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Devinez la combinaison secrète de l'ordinateur avant qu'il ne trouve la vôtre !");
        System.out.println("Tappez une combinsaison à " + combination.getLength() + " chiffres");
        System.out.println("'=' -> le chiffre est bon");
        System.out.println("'+' -> le chiffre à trouver est plus grand");
        System.out.println("'-' -> le chiffre à trouver est plus petit");
        System.out.println("------------------------------------------------------------------");
    }


    private String showHint(String str, String strToCompare) {
        StringBuilder hintBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            int difference = str.charAt(i) - strToCompare.charAt(i);
            if (difference == 0) {
                hintBuilder.append("=");
            } else if (difference < 0) {
                hintBuilder.append("-");
            } else {
                hintBuilder.append("+");
            }
        }
        return hintBuilder.toString();
    }

    private String iaGuessNewCombination(String combination, String combinationToFind) {
        if (combination.length() != combinationToFind.length())
            return this.combination.generateCombination().getCombination();

        StringBuilder newCombination = new StringBuilder();
        for (int i = 0; i < combinationToFind.length(); i++) {
            int combinationDigit = combination.charAt(i) - '0';
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


