package com.example.simplexdemo;

import java.util.ArrayList;
import java.util.List;

public class SimlexMethod {
    private FractionalNumber fractionalNumber = new FractionalNumber();
    private String[][] limitations;
    private String[] notBasis;
    private String[] basis;
    private String[] firstBasis;
    private String[] startNotBasis;
    private String[][] simplexTable;
    private String[][] answerTable;
    private String minElInfo = "no";
    private int iteration;
    private String answer;
    private String[] function;
    private String[] downFunction;
    private List<Integer> negativeElements = new ArrayList<>();
    private int quantityLimit;
    private int negativeElementIndex;
    private int min;

    public String[][] getAnswerTable() {
        return answerTable;
    }

    public void setAnswerTable(String[][] answerTable) {
        this.answerTable = answerTable;
    }

    public String[] getStartNotBasis() {
        return startNotBasis;
    }

    public void setStartNotBasis(String[] startNotBasis) {
        this.startNotBasis = startNotBasis;
    }

    public String getMinElInfo() {
        return minElInfo;
    }

    public void setMinElInfo(String minElInfo) {
        this.minElInfo = minElInfo;
    }

    public String[] getStartBasis() {
        return firstBasis;
    }

    public void setStartBasis(String[] firstBasis) {
        this.firstBasis = firstBasis;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public void addIteration() {
        this.iteration++;
    }

    public String[] getNotBasis() {
        return notBasis;
    }

    public String[][] getSimplexTable() {
        return simplexTable;
    }

    public void setSimplexTable(String[][] simplexTable) {
        this.simplexTable = simplexTable;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setNotBasis(String[] notBasis) {
        this.notBasis = notBasis;
    }

    public String[] getBasis() {
        return basis;
    }

    public void setBasis(String[] basis) {
        this.basis = basis;
    }

    public FractionalNumber getFractionalNumber() {
        return fractionalNumber;
    }

    public int getQuantityLimit() {
        return quantityLimit;
    }

    public void setQuantityLimit(int quantityLimit) {
        this.quantityLimit = quantityLimit;
    }

    public void setDownFunction(String[] downFunction) {
        this.downFunction = downFunction;
    }

    public void setLimitations(String[][] limitations) {
        this.limitations = limitations;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getNegativeElementIndex() {
        return negativeElementIndex;
    }

    public void setNegativeElementIndex(int negativeElementIndex) {
        this.negativeElementIndex = negativeElementIndex;
    }

    public List<Integer> getNegativeElements() {
        return negativeElements;
    }

    public String[][] getLimitations() {
        return limitations;
    }

    public String[] getFunction() {
        return function;
    }

    public String[] getDownFunction() {
        return downFunction;
    }

    public int isMin() {
        return min;
    }

    public void setFunction(String[] function) {
        this.function = function;
    }
}
