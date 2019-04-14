package com.gameplaystudio.rudy;

public class Combination {
    private StringBuilder combination = new StringBuilder();
    private int length = 4;

    public String getCombination() {
        return combination.toString();
    }

    public int getLength() {
        return length;
    }

    public void generateCombination(){
        combination.delete(0, combination.length());
        for (int i = 0; i < this.length; i++) {
            combination.append((int)(Math.random() * 10));
        }
    }
    public void check(){

    }
}
