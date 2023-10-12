package com.example.simplexdemo;

import java.util.Arrays;

public class Gauss {
    FractionalNumber fractionalNumber = new FractionalNumber();
    private String[][] matrix;
    private boolean isSteps;
    private int[] basis;
    private int[] startBasis;
    private String[] function;
    private int size;
    private SimlexMethod simlexMethod;
    private CalculateSimlex calculateSimlex;
    public Gauss(String[][] matrix, int[] basis, CalculateSimlex calculateSimlex, String[] function, boolean isSteps){
        this.matrix = matrix;
        this.basis = basis;
        this.startBasis = new int[basis.length];
        System.arraycopy(basis, 0, this.startBasis, 0, basis.length);
        this.calculateSimlex = calculateSimlex;
        this.simlexMethod = calculateSimlex.getSimlexMethod();
        this.function = function;
        this.isSteps = isSteps;
        calculateGauss(this.matrix, this.basis);
    }

    public void setX(int[] nubmerBasis){
        int indexBasis = 0;
        for(int i : nubmerBasis){
            if(i != 0)
                indexBasis++;
        }
        String[] basis = new String[indexBasis];
        String[] startBasis = new String[indexBasis];
        String[] notBasis = new String[nubmerBasis.length - indexBasis];
        int i = 0;
        int k = 0;
        for(int j = 0; j < nubmerBasis.length; j++){
            if(nubmerBasis[j] != 0)
            {
                basis[k] = "x" + (j + 1);
                startBasis[k] = "x" + (j + 1);
                k++;
            }
            else {
                notBasis[i] = "x" + (j + 1);
                i++;
            }

        }
        simlexMethod.setBasis(basis);
        simlexMethod.setStartBasis(startBasis);
        simlexMethod.setNotBasis(notBasis);
    }

    public void calculateGauss(String[][] matrix, int[] basis){
        setX(basis);
        int row = 0;
        int p = 0;
        for (int i = 0; i < matrix.length; i++){
            p = 0;
            for (int j = 0; j < matrix[0].length; j++){
                if(basis[p] != 0){
                    int col = j;
                    String obrNumber;
                    if(matrix[i][j].charAt(0) != '0'){
//                        if(matrix[i][j].charAt(0) == '-')
//                            obrNumber = operationWithTwoNumbers("-1", matrix[i][j],"/");
//                        else
                            obrNumber = operationWithTwoNumbers("1", matrix[i][j],"/");

                        if(row == i)
                            for (int k = 0; k < matrix[0].length; k++)
                                matrix[i][k] = operationWithTwoNumbers(matrix[i][k], obrNumber, "*");
                        //вычитание
                        for (int k = i; k < matrix.length; k++){
                            if(row != k){
                                String coef = operationWithTwoNumbers(matrix[row][col], matrix[k][col], "*");
                                if(coef.charAt(0) == '-')
                                    coef = replacingTheSign(coef);
                                String needEl = matrix[k][col];
                                for(int c = 0; c < matrix[0].length; c++){
                                    String tmp = operationWithTwoNumbers(matrix[i][c], coef, "*");
                                    if(needEl.charAt(0) == '-'){
                                        matrix[k][c] = operationWithTwoNumbers(matrix[k][c], tmp, "+");
                                    }
                                    else
                                        matrix[k][c] = operationWithTwoNumbers(matrix[k][c], tmp, "-");
                                }
                            }

                        }
                        row++;
                        basis[p] = 0;//в конец
                        break;
                    }
                    else {
                        basis[p] = 0;
                        for (int k = 0; k < startBasis.length; k++){
                            if(startBasis[k] != 1 && !matrix[matrix.length - 1][k].equals("0")){
                                startBasis[p] = 0;
                                startBasis[k] = 1;
                                basis = Arrays.copyOfRange(startBasis, 0, startBasis.length);
                                row = 0;
                                j = 0;
                                i = 0;
                                p = 0;
                                setX(basis);
                                break;
                            }

                        }
                    }
                }
                if(p < matrix[0].length - 2)
                    p++;
            }
        }

        basis = Arrays.copyOfRange(startBasis, 0, startBasis.length);
        row--;

        for (int i = matrix.length - 1; i >= 0; i--){
            p = matrix[0].length - 2;
            for (int j = matrix[0].length - 2; j >= 0; j--){
                if(basis[p] == 1){
                    int col = j;
                    String obrNumber;
                    if(matrix[i][j].charAt(0) != '0'){
//                        if(matrix[i][j].charAt(0) == '-')
//                            obrNumber = operationWithTwoNumbers("-1", matrix[i][j],"/");
//                        else
                            obrNumber = operationWithTwoNumbers("1", matrix[i][j],"/");

                        if(row == i)
                            for (int k = 0; k < matrix[0].length; k++)
                                matrix[i][k] = operationWithTwoNumbers(matrix[i][k], obrNumber, "*");
                        //вычитание
                        for (int k = i; k >= 0; k--){
                            if(row != k){
                                String coef = operationWithTwoNumbers(matrix[row][col], matrix[k][col], "*");
                                if(coef.charAt(0) == '-')
                                    coef = replacingTheSign(coef);
                                String needEl = matrix[k][col];
                                for(int c = matrix[0].length - 1; c >= 0; c--){
                                    String tmp =  operationWithTwoNumbers(matrix[i][c], coef, "*");
                                    if(needEl.charAt(0) == '-'){
                                        matrix[k][c] = operationWithTwoNumbers(matrix[k][c], tmp, "+");
                                    }
                                    else
                                        matrix[k][c] = operationWithTwoNumbers(matrix[k][c], tmp, "-");
                                }
                            }

                        }
                        row--;
                        basis[p] = 0;//в конец
                        break;
                    }
                }
                if(p > 0)
                    p--;
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }
        simlexMethod.setGaussTable(matrix);
        if(checkSystem(matrix).equals("Система несовместна")){
            simlexMethod.setAnswer("Система несовместна");
            return;
        }
        boolean isRebase = false;
        for (int i = 0; i < matrix.length ; i++){
            if(matrix[i][matrix[0].length - 1].charAt(0) == '-'){
                int k = 0;
                for(int j = 0; j < startBasis.length; j++){
                    if(startBasis[j] == 1 && k == i){
                        for(int l = 0; l < startBasis.length; l++){
                            if(startBasis[l] == 0){
                                startBasis[l] = 1;
                                isRebase = true;
                                startBasis[j] = 0;
                                break;
                            }
                        }

                    }
                    if(isRebase){
                        break;
                    }
                    if(startBasis[j] == 1 && k != i)
                        k++;
                }
            }
            if(isRebase){
                basis = Arrays.copyOf(startBasis, startBasis.length);
                calculateGauss(matrix,basis);
            }
        }
        String[][] simplexTable;
        if(!isSteps)
            simplexTable = newSimplexTable(matrix);
//        for (int i = 0; i < simplexTable.length; i++) {
//            System.out.println(Arrays.toString(simplexTable[i]));
//        }
    }

    public String checkSystem(String[][] matrix){
        boolean onlyZeros = true;
        int count = 0;
        for (int i = 0; i <  matrix.length; i++) {
            for (int j = 0; j <  matrix[i].length; j++) {
                if (!matrix[i][j].equals("0")) {
                    onlyZeros = false;
                    count++;
                }
            }
            if(count == 1 || onlyZeros){
                return "Система несовместна";
            }
            else {
                onlyZeros = true;
                count = 0;
            }
        }
        return "ok";
    }

    public void newSimplexTablePoShagam(String[][] simplexTable){
        int indexBasis = 0;
        for(int i : startBasis){
            if(i == 1)
                indexBasis++;
        }
        int[] basis = Arrays.copyOfRange(startBasis,0,startBasis.length);
        for(int i = 0; i < simplexTable.length; i++){
            for (int j = 0; j < simplexTable[0].length - 1; j++)
                if(basis[j] == 1){
                    if(simplexTable[i][j].equals("0")){
                        String[] tmp = simplexTable[i];
                        simplexTable[i] = simplexTable[i+1];
                        simplexTable[i+1] = tmp;
                        basis[j] = 0;
                        break;
                    }
                    else{
                        basis[j] = 0;
                        break;
                    }
                }
        }
        String[][] newSimplexTable = new String[simplexTable.length + 1][simplexTable[0].length - indexBasis];
        for(int i = 0; i < newSimplexTable.length - 1; i++){
            int k = 0;
            for (int j = 0; j < simplexTable[0].length - 1; j++){
                if(startBasis[j] != 1){
                    newSimplexTable[i][k] = simplexTable[i][j];
                    k++;
                }
                newSimplexTable[i][k] = simplexTable[i][simplexTable[0].length - 1];
            }
        }
        String[] downF = new String[newSimplexTable[0].length];
        for(int i = 0; i < downF.length; i++)
            downF[i] = "0";
        for(int i = 0; i < newSimplexTable.length - 1; i++){
            for(int k = 0; k < newSimplexTable[0].length ; k++){
                int indexX = Integer.parseInt(simlexMethod.getBasis()[i].substring(1))-1;
                String tmpNumber;
                if(k != newSimplexTable[0].length - 1)
                    tmpNumber = operationWithTwoNumbers(replacingTheSign(newSimplexTable[i][k]), function[indexX], "*");
                else
                    tmpNumber = operationWithTwoNumbers(newSimplexTable[i][k], function[indexX], "*");
                downF[k] = operationWithTwoNumbers(downF[k], tmpNumber, "+");
            }
        }

        for(int i = 0; i < simlexMethod.getNotBasis().length; i++){
            int indexX = Integer.parseInt(simlexMethod.getNotBasis()[i].substring(1))-1;
            downF[i] = operationWithTwoNumbers(downF[i], function[indexX], "+");
        }
        for (int i = 0; i < newSimplexTable.length; i++) {
            System.out.println(Arrays.toString(newSimplexTable[i]));
        }
        downF[downF.length -1] = replacingTheSign(downF[downF.length -1]);
        newSimplexTable = Arrays.copyOfRange(newSimplexTable, 0, newSimplexTable.length - 1);
//        downFunction = downF;

        for (int i = 0; i < newSimplexTable.length; i++) {
            System.out.println(Arrays.toString(newSimplexTable[i]));
        }
        System.out.println(Arrays.toString(downF));
        calculateSimlex.calculateSimplexTablePoShagam(newSimplexTable,downF);
    }

    public String[][] newSimplexTable(String[][] simplexTable){
        int indexBasis = 0;
        for(int i : startBasis){
            if(i == 1)
                indexBasis++;
        }
        int[] basis = Arrays.copyOfRange(startBasis,0,startBasis.length);
        for(int i = 0; i < simplexTable.length; i++){
            for (int j = 0; j < simplexTable[0].length - 1; j++)
            if(basis[j] == 1){
                if(simplexTable[i][j].equals("0")){
                    String[] tmp = simplexTable[i];
                    simplexTable[i] = simplexTable[i+1];
                    simplexTable[i+1] = tmp;
                    basis[j] = 0;
                    break;
                }
                else{
                    basis[j] = 0;
                    break;
                }
            }
        }
        String[][] newSimplexTable = new String[simplexTable.length + 1][simplexTable[0].length - indexBasis];
        for(int i = 0; i < newSimplexTable.length - 1; i++){
            int k = 0;
            for (int j = 0; j < simplexTable[0].length - 1; j++){
                if(startBasis[j] != 1){
                    newSimplexTable[i][k] = simplexTable[i][j];
                    k++;
                }
                newSimplexTable[i][k] = simplexTable[i][simplexTable[0].length - 1];
            }
        }
        String[] downF = new String[newSimplexTable[0].length];
        for(int i = 0; i < downF.length; i++)
            downF[i] = "0";
        for(int i = 0; i < newSimplexTable.length - 1; i++){
            for(int k = 0; k < newSimplexTable[0].length ; k++){
                int indexX = Integer.parseInt(simlexMethod.getBasis()[i].substring(1))-1;
                String tmpNumber;
                if(k != newSimplexTable[0].length - 1)
                    tmpNumber = operationWithTwoNumbers(replacingTheSign(newSimplexTable[i][k]), function[indexX], "*");
                else
                    tmpNumber = operationWithTwoNumbers(newSimplexTable[i][k], function[indexX], "*");
                downF[k] = operationWithTwoNumbers(downF[k], tmpNumber, "+");
            }
        }

        for(int i = 0; i < simlexMethod.getNotBasis().length; i++){
            int indexX = Integer.parseInt(simlexMethod.getNotBasis()[i].substring(1))-1;
            downF[i] = operationWithTwoNumbers(downF[i], function[indexX], "+");
        }
        for (int i = 0; i < newSimplexTable.length; i++) {
            System.out.println(Arrays.toString(newSimplexTable[i]));
        }
        downF[downF.length -1] = replacingTheSign(downF[downF.length -1]);
        newSimplexTable = Arrays.copyOfRange(newSimplexTable, 0, newSimplexTable.length - 1);
//        downFunction = downF;

        for (int i = 0; i < newSimplexTable.length; i++) {
            System.out.println(Arrays.toString(newSimplexTable[i]));
        }
        System.out.println(Arrays.toString(downF));
        return this.calculateSimlex.calculateSimplexTable(newSimplexTable, downF);
    }

    private String replacingTheSign(String replacingNumber){
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

    public SimlexMethod getSimlexMethod() {
        return simlexMethod;
    }

    public CalculateSimlex getCalculateSimlex() {
        return calculateSimlex;
    }
}
