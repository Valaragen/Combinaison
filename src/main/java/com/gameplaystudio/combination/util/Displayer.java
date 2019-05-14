package com.gameplaystudio.combination.util;

public class Displayer {

    public enum TAG {
        LINE_SEPARATOR("------------------------------------------------------------------\n");

        private String value;

        TAG(String id){
            this.value = id;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static void display(String text){
        System.out.println(text);
    }
}
