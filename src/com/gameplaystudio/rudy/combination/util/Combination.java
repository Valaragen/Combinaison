package com.gameplaystudio.rudy.combination.util;

public class Combination {

    private String combination;
    private int length = 4;

    public String getCombination() {
        return combination;
    }

    public int getLength() {
        return length;
    }

    public Combination generateCombination(){//TODO delete this class and migrate the method in GameMode
        StringBuilder combinationBuilder = new StringBuilder();

        for (int i = 0; i < this.length; i++) {
            combinationBuilder.append((int)(Math.random() * 10));
        }

        combination = combinationBuilder.toString();
        return this;
    }
}
