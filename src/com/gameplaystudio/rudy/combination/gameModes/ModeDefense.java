package com.gameplaystudio.rudy.combination.gameModes;

import com.gameplaystudio.rudy.combination.util.Combination;

import java.util.regex.Pattern;

public class ModeDefense extends GameMode {
    private int combinationLength = 4;

    @Override
    public String getNameInMenu(){
        return "Mode Defense";
    }

    @Override
    protected void logic(){
        System.out.println("------------------------------------------------------------------");
        System.out.println("Bienvenue dans le mode défense");

        while (run){
            String playerCombination = chooseCombination();
            System.out.println("------------------------------------------------------------------");
            System.out.println("Très bon choix !");
            System.out.println("Votre combinaison secrète est | " + playerCombination + " |");

            boolean play = true;
            boolean win = false;
            int nbTry = 1;
            int nbAllowedTry = 10;
            String iaCombination = new Combination().generateCombination().getCombination();


            if(iaCombination.equals(playerCombination)){
                play = false;
            }else{
                displayIndication(playerCombination);
            }
            System.out.println("L'ordinateur n'a pas trouvé votre combinaison et attend votre aide");
            System.out.println("Votre combinaison est | " + playerCombination + " |");
            System.out.println("L'ordinateur à proposé | " + iaCombination + " |");

            while (play){//TODO option pour quitter ? Option pour afficher les indications?
                String playerHint = sc.nextLine();

                if (Pattern.matches("[=+-]+", playerHint) && playerHint.length() == combinationLength){
                    nbTry++;
                    if ( nbTry >= nbAllowedTry ){
                        play = false;
                    }

                    iaCombination = iaGuessNewCombination(iaCombination, playerHint);

                    if (playerCombination.equals(iaCombination)){
                        System.out.println("L'ordinateur propose la combinaison : " + iaCombination);
                        play = false;
                        win = true;
                    }else if (play){
                        System.out.println("L'ordinateur n'a pas trouvé votre combinaison et attend votre aide");
                        System.out.println("Votre combinaison est | " + playerCombination + " |");
                        System.out.println("L'ordinateur à proposé | " + iaCombination + " |");
                    }
                }else{
                    System.out.println("Votre indice n'est pas valide");
                    System.out.println("Merci d'entrer un indice constitué de " + combinationLength + " caractères (+ ou - ou =)" );
                }

            }

            if (win){
                System.out.println("------------------------------------------------------------------");
                System.out.println("L'ordinateur à réussi à trouver votre combinaison secrète!");
                System.out.println("Vos indications ont été éfficaces");
                System.out.println("L'ordinateur à mis " + nbTry + " éssais");
                System.out.println("La combinaison était  | " + playerCombination + " |");
                System.out.println("------------------------------------------------------------------");
            }else{
                System.out.println("------------------------------------------------------------------");
                System.out.println("L'ordinateur n'a pas réussi à trouver votre combinaison secrète !");
                System.out.println("L'ordinateur a dépassé les " + nbAllowedTry + " éssais autorisés !");
                System.out.println("La combinaison était | " + playerCombination + " |");
                System.out.println("La dernière proposition de l'ordinateur était | " + iaCombination + " |");
                System.out.println("------------------------------------------------------------------");
            }

            showReplayMenu();

        }
    }

    private String chooseCombination(){
        boolean validChoice = false;
        String choice;
        System.out.println("------------------------------------------------------------------");
        System.out.println("Veuillez définir une combinaison de " + combinationLength + " chiffres");
        System.out.println("------------------------------------------------------------------");
        do {
            choice = sc.nextLine();
            if (Pattern.matches("[0-9]+", choice) && choice.length() == combinationLength){
                validChoice = true;
            }else{
                System.out.println("Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + combinationLength + " chiffres");
            }
        }while (!validChoice);
        return choice;
    }

    private void displayIndication(String combinationToShow) {
        System.out.println("------------------------------------------------------------------");
        System.out.println("L'ordinateur doit trouver votre combinaison : " + combinationToShow );
        System.out.println("Pour l'aider il va falloir lui donner un indice constitué de " + combinationLength + " caractères (+ ou - ou =)" );
        System.out.println("'=' -> le chiffre est bon");
        System.out.println("'+' -> le chiffre à trouver est plus grand");
        System.out.println("'-' -> le chiffre à trouver est plus petit");
        System.out.println("------------------------------------------------------------------");
    }


    private String iaGuessNewCombination(String combination, String hint){
        if (combination.length() != hint.length())
            throw new IllegalArgumentException("combination and hint arguments must have the same length");//TODO handle this exception + add other Exception

        StringBuilder newCombination = new StringBuilder();
        for (int i = 0; i < hint.length(); i++) {
            int currentNumber = combination.charAt(i) - '0';
            if (hint.charAt(i) == '+' && currentNumber < 9){
                currentNumber++;
            }else if (hint.charAt(i) == '-' && currentNumber > 0){
                currentNumber--;
            }
            newCombination.append(currentNumber);

        }
        System.out.println(newCombination);
        return newCombination.toString();
    }

}
