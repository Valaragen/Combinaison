package com.gameplaystudio.combination.util;

import static com.gameplaystudio.combination.CombinationGame.logger;

/**
 * Class used to format and display text in console command
 */
public class Displayer {
    public static final String CARRIAGE_RETURN = "\n";
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
     * Method used to display text in console command without carriage return at the end
     * @param text text to display
     */
    public static void displayInline(String text) {
        System.out.print(text);
    }

    /**
     * Method used to display text with top and bottom separator
     * @param text text to display
     * @param separator Type of separator choosed from {@link TAG}
     */
    public static void displaySemiBoxed(String text, TAG separator){
        text = separator + CARRIAGE_RETURN + text + CARRIAGE_RETURN + separator;
        System.out.println(text);
    }

    /**
     * Overloaded method of {@link #displaySemiBoxed(String, TAG)} used to add line breaks before and/or after the block
     * @param text text to display
     * @param separator Type of separator choosed from {@link TAG}
     * @param nbAppendedLineBreak Number of line break appended to the block
     * @param nbPrependedLineBreak Number of line break prepended to the block
     */
    public static void displaySemiBoxed(String text, TAG separator, int nbPrependedLineBreak, int nbAppendedLineBreak){
        if(nbAppendedLineBreak <= 0 && nbPrependedLineBreak<= 0){
            logger.debug("Incorrect usage of method : No line break has been added. You should use displaySemiBoxed(String, TAG)");
        }

        text = separator + CARRIAGE_RETURN + text + CARRIAGE_RETURN + separator;

        StringBuilder textBuilder = new StringBuilder(text);
        for (int i = 0; i < nbPrependedLineBreak; i++) {
            textBuilder.insert(0, CARRIAGE_RETURN);
        }
        for (int i = 0; i < nbAppendedLineBreak; i++) {
            textBuilder.append(CARRIAGE_RETURN);
        }

        System.out.println(textBuilder.toString());
    }

}
