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

    public void generateCombination(){
        StringBuilder combinationBuilder = new StringBuilder();

        for (int i = 0; i < this.length; i++) {
            combinationBuilder.append((int)(Math.random() * 10));
        }

        combination = combinationBuilder.toString();
    }

    public String showHint(String toCompare){
        StringBuilder indicatorBuilder= new StringBuilder();
        for (int i = 0; i < combination.length(); i++) {
            int difference = combination.charAt(i) - toCompare.charAt(i);
            if(difference == 0){
                indicatorBuilder.append("=");
            }else if(difference < 0){
                indicatorBuilder.append("-");
            }else{
                indicatorBuilder.append("+");
            }
        }
        return indicatorBuilder.toString();
    }
}
