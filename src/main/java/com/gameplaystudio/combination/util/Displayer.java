package com.gameplaystudio.combination.util;

import static com.gameplaystudio.combination.CombinationGame.logger;

/**
 * Class used to format and display text in console command
 */
public class Displayer {

    /**
     * Enumeration of special tags used to add generic formatting elements to a String
     */
    public enum TAG {
        LINE_SEPARATOR("------------------------------------------------------------------"),
        EQUAL_SEPARATOR("=================================================================="),
        PLUS_SEPARATOR("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        private String value;

        TAG(String id){
            this.value = id;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * Method used to display text in console command
     * @param text text to display
     */
    public static void display(String text){
        System.out.println(text);
    }

    /**
     * Method used to display text with top and bottom separator
     * @param text text to display
     * @param separator Type of separator choosed from {@link TAG}
     */
    public static void displaySemiBoxed(String text, TAG separator){
        text = separator + "\n" + text + "\n" + separator;
        System.out.println(text);
    }

    /**
     * Overloaded method of {@link #displaySemiBoxed(String, TAG)} used to add line breaks before and/or after the block
     * @param text text to display
     * @param separator Type of separator choosed from {@link TAG}
     * @param nbAppendedLineBreak Number of line break appended to the text semi-boxed
     * @param nbPrependedLineBreak Number of line break prepended to the text semi-boxed block
     */
    public static void displaySemiBoxed(String text, TAG separator, int nbPrependedLineBreak, int nbAppendedLineBreak){
        if(nbAppendedLineBreak <= 0 && nbPrependedLineBreak<= 0){
            logger.info("Incorrect usage of Overloaded method displaySemiBoxed : No line break has been added. You should use displaySemiBoxed(String, TAG)");
        }
        text = "\n".repeat(Math.max(0,nbPrependedLineBreak)) + separator + "\n" + text + "\n" + separator + "\n".repeat(Math.max(0, nbAppendedLineBreak));
        System.out.println(text);
    }

}
