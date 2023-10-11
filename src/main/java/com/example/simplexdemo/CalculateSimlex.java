package com.example.simplexdemo;

import java.util.*;

public class CalculateSimlex {
    private SimlexMethod simlexMethod;


    public CalculateSimlex(SimlexMethod simlexMethod,String[] function, String[][] limitations){
        this.simlexMethod = simlexMethod;
        simlexMethod.setFunction(function);
        simlexMethod.setLimitations(limitations);
        simlexMethod.setDownFunction(calcualteDownFunction(limitations));
    }

    public SimlexMethod getSimlexMethod() {
        return simlexMethod;
    }

    public String[] calcualteDownFunction(String[][] limitations){
        int columnSize = limitations[0].length;
        int rowSize = limitations.length;
        String[] downFunction = new String[columnSize];
        StringBuilder calucateString = new StringBuilder();

        for(int i = 0; i < columnSize; i++){
            for(int j = 0; j < rowSize; j++){
                if(limitations[j][i].charAt(0) != '-' && j != 0)
                    calucateString.append("+").append(limitations[j][i]);
                else
                    calucateString.append(limitations[j][i]);
            }
            downFunction[i] = simlexMethod.getFractionalNumber().convertString(calucateString.toString());
            calucateString = new StringBuilder();
        }


        return replacingTheSign(downFunction);
    }

    public String operationWithTwoNumbers(String first, String second, String operation){
        int a, b, c, d;
        simlexMethod.getFractionalNumber().convertNumber(first);
        a = simlexMethod.getFractionalNumber().getA();
        b = simlexMethod.getFractionalNumber().getB();
        simlexMethod.getFractionalNumber().convertNumber(second);
        c = simlexMethod.getFractionalNumber().getA();
        d = simlexMethod.getFractionalNumber().getB();
        return simlexMethod.getFractionalNumber().calculate(a,b,c,d, operation);
    }

    public String[][] transitionSimplexTable(String[][] simplexTable, int row, int col){
        simplexTable[row][col] = operationWithTwoNumbers("1", simplexTable[row][col], "/");
        String[] oldNumbers = new String[simplexTable.length];
        String[] basic = simlexMethod.getBasis();
        String[] notBasis = simlexMethod.getNotBasis();
        String tmpX = basic[row];
        basic[row] = notBasis[col];
        notBasis[col] = tmpX;
        simlexMethod.setBasis(basic);
        simlexMethod.setNotBasis(notBasis);

        for (int i = 0; i < simplexTable[row].length; i++){
            if(i != col){
                simplexTable[row][i] = operationWithTwoNumbers(simplexTable[row][i], simplexTable[row][col], "*");
            }
        }

        for (int i = 0; i < simplexTable.length; i++){
            oldNumbers[i] = simplexTable[i][col];
            if(i != row){
                simplexTable[i][col] = operationWithTwoNumbers(simplexTable[i][col], simplexTable[row][col], "*");
                simplexTable[i][col] = operationWithTwoNumbers(simplexTable[i][col], "-1", "*");
            }
        }
        String tmp;
        for(int i = 0; i < simplexTable.length; i++){
            if(i != row)
                for (int j = 0; j < simplexTable[row].length; j++){
                    if(j != col){
                        tmp = operationWithTwoNumbers(oldNumbers[i], simplexTable[row][j], "*");
                        simplexTable[i][j] = operationWithTwoNumbers(simplexTable[i][j], tmp, "-");
                    }
                }
        }
        for (int i = 0; i < simplexTable.length; i++) {
            System.out.println(Arrays.toString(simplexTable[i]));
        }
        System.out.println(Arrays.toString(basic));
        System.out.println(Arrays.toString(notBasis));
        simlexMethod.setSimplexTable(simplexTable);

//        simlexMethod.putBasisSteps(simlexMethod.getIterationForStep(), basic);
//        simlexMethod.putNotBasisSteps(simlexMethod.getIterationForStep(), notBasis);
//        simlexMethod.putTableSteps(simlexMethod.getIterationForStep(), simplexTable);
//        simlexMethod.addIterationForStep();
        getAnswer(simplexTable, simlexMethod.getBasis(), simlexMethod.getNotBasis());
        return simplexTable;
    }

    public void setX(String[][] limit){
        String[] basis = new String[limit.length];
        String[] startBasis = new String[limit.length];
        String[] notBasis = new String[limit[0].length - 1];
        int i;
        for (i = 0; i < limit[0].length - 1; i++){
            notBasis[i] = "x" + (i + 1);
        }
        for(int j = 0; j < limit.length; j++){
            basis[j] = "x" + (i + 1);
            startBasis[j] = "x" + (i + 1);
            i++;
        }
        simlexMethod.setBasis(basis);
        simlexMethod.setStartBasis(startBasis);
        simlexMethod.setNotBasis(notBasis);
    }
    private String replacingTheSignPoShagam(String replacingNumber){
        if(replacingNumber.charAt(0) == '-'){
            replacingNumber = replacingNumber.substring(1);
        }
        else
        {
            if(replacingNumber.charAt(0) != '0')
                replacingNumber = "-" + replacingNumber;
        }
        return replacingNumber;
    }

    public String checkAnswerPoShagam(SimlexMethod simlexMethod,  String[][] simplexTable, String[] basis, boolean isGauss){
        String[] startBasis = simlexMethod.getStartBasis();
        for (int i = 0; i < simplexTable[0].length - 1; i++){
            if(simplexTable[simplexTable.length - 1][i].charAt(0) == '-'){
                simlexMethod.setAnswer("Функция неограничена");
                return "Функция неограничена";
            }
        }
        if(!isGauss)
            for (int i = 0; i < simlexMethod.getStartBasis().length; i++)
                for(int j = 0; j < simlexMethod.getStartBasis().length; j++)
                    if(basis[i].equals(startBasis[j])){
                        simlexMethod.setAnswer("Система несовместна");
                        return "Система несовместна";
                    }
        simlexMethod.setSimplexTable(simplexTable);
        return "Ok";
    }



    public String[][] newSimplexTablePoSagam(String[][] simplexTable){
        int col = 0;
        for(int i = 0; i < simplexTable[0].length; i++){
            if(simplexTable[simplexTable.length - 1][i].equals("0")){
                col++;
            }
        }
        String[] notBasis = simlexMethod.getNotBasis();
        String[] newNotBasis = new String[col - 1];
        String[] Basis = simlexMethod.getBasis();
        String[] function = simlexMethod.getFunction();
        int j = 0;
        String[][] newSimplexTable = new String[simplexTable.length][col];
        for(int i = 0; i < simplexTable[0].length; i++){
            if(simplexTable[simplexTable.length - 1][i].equals("0")){
                for(int k = 0; k < simplexTable.length; k++)
                    System.arraycopy(simplexTable[k], i, newSimplexTable[k], j, 1);
                j++;
            }
        }
        j = 0;
        for(int i = 0; i < simplexTable[0].length - 1; i++){
            if(simplexTable[simplexTable.length - 1][i].equals("0")){
                newNotBasis[j] = notBasis[i];
                j++;
            }
        }

        String[] downF = newSimplexTable[newSimplexTable.length - 1];
        for(int i = 0; i < newSimplexTable.length - 1; i++){
            for(int k = 0; k < newSimplexTable[0].length; k++){
                int indexX = Integer.parseInt(Basis[i].substring(1))-1;
                String tmpNumber;
                if(k != newSimplexTable[0].length - 1)
                    tmpNumber = operationWithTwoNumbers(replacingTheSignPoShagam(newSimplexTable[i][k]), function[indexX], "*");
                else
                    tmpNumber = operationWithTwoNumbers(newSimplexTable[i][k], function[indexX], "*");
                downF[k] = operationWithTwoNumbers(downF[k], tmpNumber, "+");
            }
        }

        for(int i = 0; i < newNotBasis.length; i++){
            int indexX = Integer.parseInt(newNotBasis[i].substring(1))-1;
            downF[i] = operationWithTwoNumbers(downF[i], function[indexX], "+");
        }
        simlexMethod.setNotBasis(newNotBasis);
        downF[downF.length -1] = replacingTheSignPoShagam(downF[downF.length -1]);
        newSimplexTable = Arrays.copyOfRange(newSimplexTable, 0, newSimplexTable.length - 1);
        simlexMethod.setDownFunction(downF);
        for (int i = 0; i < newSimplexTable.length; i++) {
            System.out.println(Arrays.toString(newSimplexTable[i]));
        }
        System.out.println(Arrays.toString(downF));
        System.out.println((Arrays.toString(simlexMethod.getBasis())));
        System.out.println(Arrays.toString(simlexMethod.getNotBasis()));
        simlexMethod.setLimitations(newSimplexTable);
        String[][] newSimplexTableAnswer = new String[newSimplexTable.length+1][newSimplexTable[0].length];
        for (int i = 0; i < newSimplexTable.length; i++) {
            for (int k = 0; k < newSimplexTable[i].length; k++) {
                newSimplexTableAnswer[i][k] = newSimplexTable[i][k]; // копируем элементы исходного массива в новый массив
            }
        }
        newSimplexTableAnswer[newSimplexTable.length] = downF;
        simlexMethod.setSimplexTable(newSimplexTableAnswer);
        return newSimplexTable;
    }

    public void calculateSimplexTablePoShagam(String[][] limit, String[] downFunction){
        int numRows = limit.length;
        int numCols = limit[0].length;
        if(simlexMethod.getIteration() == 0 && simlexMethod.getBasis() == null)
            setX(limit);

        simlexMethod.addIteration();
        String[][] simlexTable = new String[numRows + 1][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                simlexTable[i][j] = limit[i][j];
            }
        }

//        for (int i = 0; i < numRows; i++){
//            if(simlexTable[i][numCols - 1].charAt(0) == '-'){
//                for (int j = 0; j < numRows; j++){
//                    simlexTable[i][j] = operationWithTwoNumbers("-1", simlexTable[i][j], "*");
//                }
//            }
//
//        }
        simlexTable[numRows] = downFunction;
        simlexMethod.setSimplexTable(simlexTable);
    }



    public String[][] helpCalculateSimplexTablePoShagam(String[][] simlexTable, int indexOfMinEl){
        String minEl = findMinEl(Arrays.copyOfRange(simlexTable[simlexTable.length - 1], 0, simlexTable[0].length - 1));
        if(minEl.equals("Calculated")){
            simlexMethod.setMinElInfo("Calculated");
            simlexMethod.setSimplexTable(simlexTable);
            return simlexTable;
        }

        if(minEl.equals("Функция неограничена")){
            simlexMethod.setMinElInfo("Функция неограничена");
            simlexMethod.setSimplexTable(simlexTable);
            return new String[][]{{"n"}, {"n"}};
        }

        return findReferenceE(simlexTable, indexOfMinEl);
    }

    public String[][] calculateSimplexTable(String[][] limit, String[] downFunction){
        int numRows = limit.length;
        int numCols = limit[0].length;
        if(simlexMethod.getIteration() == 0 && simlexMethod.getBasis() == null)
            setX(limit);

        simlexMethod.addIteration();
        String[][] simlexTable = new String[numRows + 1][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                simlexTable[i][j] = limit[i][j];
            }
        }

//        for (int i = 0; i < numRows; i++){
//            if(simlexTable[i][numCols - 1].charAt(0) == '-'){
//                for (int j = 0; j < numRows; j++){
//                    simlexTable[i][j] = operationWithTwoNumbers("-1", simlexTable[i][j], "*");
//                }
//            }
//
//        }
        simlexTable[numRows] = downFunction;

        String[][] noSolution = {{"n"},{"n"}};
        simlexMethod.setSimplexTable(simlexTable);
        while (true){
            if(Arrays.deepEquals(helpCalculateSimplexTable(simlexMethod.getSimplexTable(), downFunction), noSolution)){
                break;
                }
            else{
                if(simlexMethod.getMinElInfo().equals("Calculated"))
                    break;
                if(simlexMethod.getMinElInfo().equals("Функция неограничена"))
                    break;
                helpCalculateSimplexTable(simlexMethod.getSimplexTable(), downFunction);
            }
        }
        getAnswer(helpCalculateSimplexTable(simlexMethod.getSimplexTable(), downFunction), simlexMethod.getBasis(), simlexMethod.getNotBasis());
        return helpCalculateSimplexTable(simlexMethod.getSimplexTable(), downFunction);
    }
    public String[][] helpCalculateSimplexTable(String[][] simlexTable, String[] downFunction){
        //simlexMethod.addIteration();
        String minEl = findMinEl(Arrays.copyOfRange(downFunction, 0, downFunction.length - 1));

        int indexOfMinEl = 0;
        if(minEl.equals("Calculated")){
            simlexMethod.setMinElInfo("Calculated");
            simlexMethod.setSimplexTable(simlexTable);
            return simlexTable;
        }

        if(minEl.equals("Функция неограничена")){
            simlexMethod.setMinElInfo("Функция неограничена");
            simlexMethod.setSimplexTable(simlexTable);
            return new String[][]{{"n"}, {"n"}};
        }

        for(int i = 0; i < downFunction.length - 1; i++) {
            if(minEl.equals(downFunction[i]) && simlexMethod.getNegativeElementIndex() == i){
                indexOfMinEl = i;
                break;
            }
        }
        return findReferenceE(simlexTable, indexOfMinEl);
    }
    public String[][] findReferenceE(String[][] simlexTable, int indexOfMinEl) {
        int numRows = simlexTable.length;
        List<String> potentialReferenceElList = new ArrayList<>();
        for(int i = 0; i < numRows - 1; i++){
            String potentialReference;
            if(simlexTable[i][indexOfMinEl].charAt(0) != '-' && simlexTable[i][indexOfMinEl].charAt(0) != '0'){
                potentialReference = operationWithTwoNumbers(simlexTable[i][simlexTable[0].length - 1], simlexTable[i][indexOfMinEl], "/");
                potentialReferenceElList.add(potentialReference);
            }
        }
        if(potentialReferenceElList.size() == 0){
            simlexMethod.setSimplexTable(simlexTable);
            return new String[][]{{"n"}, {"n"}};
        }

        String[] potentialReferenceArray = potentialReferenceElList.toArray(new String[potentialReferenceElList.size()]);
        String referenceEl = findReferenceElementInRow(potentialReferenceArray);
        System.out.println(referenceEl);


        for(int i = 0; i < numRows - 1; i++) {
            if(!simlexTable[i][indexOfMinEl].equals("0")){
                String el = operationWithTwoNumbers(simlexTable[i][simlexTable[0].length - 1], simlexTable[i][indexOfMinEl], "/");
                if(referenceEl.equals(el)){
                    System.out.println(simlexTable[i][indexOfMinEl]);
                    return transitionSimplexTable(simlexTable, i, indexOfMinEl);
                }
            }
        }
        simlexMethod.setSimplexTable(simlexTable);
        return simlexTable;
    }

    private String[] replacingTheSign(String[] replacingArray){
        for (int i = 0; i < replacingArray.length; i++){
            if(replacingArray[i].charAt(0) == '-'){
                replacingArray[i] = replacingArray[i].substring(1);
            }
            else
            {
                if(replacingArray[i].charAt(0) != '0')
                    replacingArray[i] = "-" + replacingArray[i];
            }
        }
        return replacingArray;
    }
    public String findReferenceElementInRow(String[] list){
        if(list.length == 1){
            return list[0];
        }
        int a, b, c, d, minNumber, znamenatel;
        simlexMethod.getFractionalNumber().convertNumber(list[0]);
        a = simlexMethod.getFractionalNumber().getA();
        b = simlexMethod.getFractionalNumber().getB();
        simlexMethod.getFractionalNumber().convertNumber(list[1]);
        c = simlexMethod.getFractionalNumber().getA();
        d = simlexMethod.getFractionalNumber().getB();
        znamenatel = simlexMethod.getFractionalNumber().findLCM(b, d);
        a = a * (simlexMethod.getFractionalNumber().findLCM(b,d)/ b);
        c = c * (simlexMethod.getFractionalNumber().findLCM(b,d) / d);
        minNumber = Math.min(a,c);
        if(list.length > 2){
            for(int i = 2; i < list.length; i++){
                String min = simlexMethod.getFractionalNumber().toNormalNumber(minNumber, znamenatel);

                simlexMethod.getFractionalNumber().convertNumber(min);
                a = simlexMethod.getFractionalNumber().getA();
                b = simlexMethod.getFractionalNumber().getB();

                simlexMethod.getFractionalNumber().convertNumber(list[i]);
                c = simlexMethod.getFractionalNumber().getA();
                d = simlexMethod.getFractionalNumber().getB();

                znamenatel = simlexMethod.getFractionalNumber().findLCM(b, d);
                a = a * (simlexMethod.getFractionalNumber().findLCM(b,d)/ b);
                c = c * (simlexMethod.getFractionalNumber().findLCM(b,d) / d);
                minNumber = Math.min(a,c);
            }
        }

        return  simlexMethod.getFractionalNumber().toNormalNumber(minNumber, znamenatel);
    }

    public String getAnswer(String[][] simlexTable, String[] basis, String[] notBasis){
        String[][] noSolution = {{"n"},{"n"}};
        if(Arrays.deepEquals(noSolution, simlexTable)){
            simlexMethod.setAnswer("Функция неограничена");
            return "Функция неограничена";
        }

        int sizeAnswer = basis.length + notBasis.length;
        String[] answer = new String[sizeAnswer];
        for(int i = 0; i < answer.length; i++){
            for (int j = 0; j < basis.length; j++)
                if(i == Integer.parseInt(basis[j].substring(1)) - 1){
                    answer[i] = simlexTable[j][simlexTable[j].length - 1];
                    break;
                }
                else
                    answer[i] = "0";
        }
        String finalAnswer = Arrays.toString(answer) + " f(x) = " +
                Arrays.toString(replacingTheSign(new String[]{simlexTable[simlexTable.length - 1][simlexTable[0].length - 1]}));
        simlexMethod.setAnswer(finalAnswer);
        return finalAnswer;
    }
    public String findMinEl(String[] list){
        List<String> negativeEl = new ArrayList<>();
        List<Integer> negativeElIndex = new ArrayList<>();
        int kolvoMinEl = 0;
        String minNegativEl;
        for (int i = 0; i < list.length; i++) {
            if (list[i].charAt(0) == '-'){
                kolvoMinEl++;
                negativeEl.add(list[i]);
                negativeElIndex.add(i);

            }
        }
        String[][] simplexTable = simlexMethod.getSimplexTable();
        int negativElinCol = 0;
        for(int i = 0; i < simlexMethod.getSimplexTable()[0].length - 1; i++)
            if(simplexTable[simlexMethod.getSimplexTable().length - 1][i].charAt(0) == '-')
                for (int j = 0; j < simlexMethod.getSimplexTable().length - 1; j++)
                {
                    if(simplexTable[j][i].charAt(0) == '-' || simplexTable[j][i].charAt(0) == '0')
                        negativElinCol++;
                    else{
                        negativElinCol = 0;
                        break;
                    }
                }

        if(negativElinCol == simplexTable.length - 1){
            simlexMethod.setMinElInfo("Функция неограничена");
            return "Функция неограничена";
        }


        if(negativeEl.size() == 0){
            simlexMethod.setMinElInfo("Calculated");
            return "Calculated";
        }

        //simlexMethod.setNegativeElements(negativeEl.toArray(new Integer[negativeEl.size()]));

        if(negativeEl.size() == 1){
            simlexMethod.setNegativeElementIndex(negativeElIndex.get(0));
            return negativeEl.get(0);
        }

        int a, b, c, d, maxNegativNumber, znamenatel;

        simlexMethod.getFractionalNumber().convertNumber(negativeEl.get(0));
        a = simlexMethod.getFractionalNumber().getA();
        b = simlexMethod.getFractionalNumber().getB();
        simlexMethod.getFractionalNumber().convertNumber(negativeEl.get(1));
        c = simlexMethod.getFractionalNumber().getA();
        d = simlexMethod.getFractionalNumber().getB();
        znamenatel = simlexMethod.getFractionalNumber().findLCM(b, d);
        a = a * (simlexMethod.getFractionalNumber().findLCM(b,d)/ b);
        c = c * (simlexMethod.getFractionalNumber().findLCM(b,d) / d);
        maxNegativNumber = Math.min(a,c);
        if(maxNegativNumber == a)
            simlexMethod.setNegativeElementIndex(negativeElIndex.get(0));
        else
            simlexMethod.setNegativeElementIndex(negativeElIndex.get(1));

        if(negativeEl.size() > 2){
            for(int i = 2; i < negativeEl.size(); i++){
                minNegativEl = simlexMethod.getFractionalNumber().toNormalNumber(maxNegativNumber, znamenatel);

                simlexMethod.getFractionalNumber().convertNumber(minNegativEl);
                a = simlexMethod.getFractionalNumber().getA();
                b = simlexMethod.getFractionalNumber().getB();

                simlexMethod.getFractionalNumber().convertNumber(negativeEl.get(i));
                c = simlexMethod.getFractionalNumber().getA();
                d = simlexMethod.getFractionalNumber().getB();

                znamenatel = simlexMethod.getFractionalNumber().findLCM(b, d);
                a = a * (simlexMethod.getFractionalNumber().findLCM(b,d)/ b);
                c = c * (simlexMethod.getFractionalNumber().findLCM(b,d) / d);

                maxNegativNumber = Math.min(a,c);
                if(maxNegativNumber == a)
                    simlexMethod.setNegativeElementIndex(negativeElIndex.get(i-1));
                else
                    simlexMethod.setNegativeElementIndex(negativeElIndex.get(i));
            }
        }

        return simlexMethod.getFractionalNumber().toNormalNumber(maxNegativNumber, znamenatel);
    }
}
