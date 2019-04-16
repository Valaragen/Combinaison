package com.gameplaystudio.rudy.combination.gameModes;

import java.util.regex.Pattern;

public class ModeChallenger extends GameMode {

    @Override
    public String getNameInMenu(){
        return "Mode Challenger";
    }

    @Override
    protected void logic(){
        System.out.println("------------------------------------------------------------------");
        System.out.println("Bienvenue dans le mode challenger");

        while (run){
            combination.generateCombination();
            System.out.println("(Combinaison secrète : " + combination.getCombination() + ")");
            boolean play = true;
            boolean win = false;
            String playerCombination;
            int nbTry = 0;
            int nbAllowedTry = 10;

            while (play){
                displayIndication();
                playerCombination = sc.nextLine();

                if (Pattern.matches("[0-9]+", playerCombination) && playerCombination.length() == combination.getLength()){
                    nbTry++;
                    if (playerCombination.equals(combination.getCombination())){
                        play = false;
                        win = true;
                    }else{
                        System.out.println("Proposition : " + playerCombination + " -> Réponse : " + combination.showHint(playerCombination));



                    }
                }else{
                    System.out.println("Votre combinaison n'est pas valide, merci d'entrer une combinaison de " + combination.getLength() + " chiffres");
                }

                if ( nbTry >= nbAllowedTry ){
                    play = false;
                }
            }

            if (win){
                System.out.println("------------------------------------------------------------------");
                System.out.println("Bravo vous avez trouvé la combinaison !");
                System.out.println("Vous avez mis " + nbTry + " éssais");
                System.out.println("La combinaison était  | " + combination.getCombination() + " |");
                System.out.println("------------------------------------------------------------------");
            }else{
                System.out.println("------------------------------------------------------------------");
                System.out.println("Dommage vous avez dépassé les " + nbAllowedTry + " éssais autorisés !");
                System.out.println("La combinaison était | " + combination.getCombination() + " |");
                System.out.println("------------------------------------------------------------------");
            }

            showReplayMenu();

        }
    }

    private void displayIndication() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Tappez une combinsaison à " + combination.getLength() + " chiffres");
        System.out.println("'=' -> le chiffre est bon");
        System.out.println("'+' -> le chiffre à trouver est plus grand");
        System.out.println("'-' -> le chiffre à trouver est plus petit");
        System.out.println("------------------------------------------------------------------");
    }

    private void showReplayMenu(){
        boolean validChoice = false;
        while (!validChoice){
            System.out.println("Souhaitez vous rejouer ?");
            System.out.println("1.Rejouer");
            System.out.println("2.Retourner au menu");
            System.out.println("3.Quitter l'application");
            int choice = sc.nextByte();//TODO handle typing error
            switch (choice){
                case 1:
                    validChoice = true;
                    break;
                case 2:
                    validChoice = true;
                    stop();
                    break;
                case 3:
                    validChoice = true;
                    leaveApp();
                    break;
                    default:
                        System.out.println("Votre séléction n'est pas valide");
            }
        }
        sc.nextLine();

    }

}
