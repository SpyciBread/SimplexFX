package com.example.simplexdemo;

import java.util.*;

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
    private int iterationForStep;
    private HashMap<Integer,String[][]> tableSteps;
    private HashMap<Integer,String[]> basisSteps;
    private HashMap<Integer,String[]> notBasisSteps;

    public void putBasisSteps(int step, String[] basisSteps) {
        this.basisSteps.put(step,basisSteps);
    }
    public void putNotBasisSteps(int step, String[] notBasisSteps) {
        this.notBasisSteps.put(step,notBasisSteps);
    }

    public String[] getBasisSteps(int step) {
        return basisSteps.get(step);
    }

    public void setBasisSteps(HashMap<Integer, String[]> basisSteps) {
        this.basisSteps = basisSteps;
    }

    public String[] getNotBasisSteps(int step) {
        return notBasisSteps.get(step);
    }

    public void setNotBasisSteps(HashMap<Integer, String[]> notBasisSteps) {
        this.notBasisSteps = notBasisSteps;
    }

    public String[][] getTableSteps(int step) {
        return tableSteps.get(step);
    }

    public void removeTableSteps(int step) {
        tableSteps.remove(step);
    }

    public void removeNotBasisSteps(int step) {
        notBasisSteps.remove(step);
    }
    public void removeBasisSteps(int step) {
        basisSteps.remove(step);
    }

    public void setTableSteps(HashMap<Integer, String[][]> tableSteps) {
        this.tableSteps = tableSteps;
    }

    public void putTableSteps(int step, String[][] tableSteps) {
//        String[][] table = new String[tableSteps.length][];
//        for (int i = 0; i < tableSteps.length; i++) {
//            table[i] = Arrays.copyOf(tableSteps[i], tableSteps[i].length);
//        }
        this.tableSteps.put(step,tableSteps);
    }

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

    public void setFunction(String[] function) {
        this.function = function;
    }

    public int getIterationForStep() {
        return iterationForStep;
    }

    public void setIterationForStep(int iterationForStep) {
        this.iterationForStep = iterationForStep;
    }

    public void addIterationForStep() {
        this.iterationForStep++;
    }
    public void decIterationForStep() {
        this.iterationForStep--;
    }
}
