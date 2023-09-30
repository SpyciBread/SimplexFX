package com.example.simplexdemo;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;

public class InputControll {
    private String[][] table;
    private String[] function;
    FractionalNumber fractionalNumber = new FractionalNumber();
    SimlexMethod simlexMethod = new SimlexMethod();

    public String[][] getArray(GridPane gridPane, int row, int col){
        String[][] matrix = new String[row][col];
        int startIndex = row + col - 1;
        int endIndex = row * col + startIndex;
        int j = 0;
        int p = 0;
        for (int i = startIndex; i < endIndex; i++) {
            TextField textField = (TextField) gridPane.getChildren().get(i);
            matrix[p][j] = textField.getText();
            if(j < col - 1)
                j++;
            else j = 0;
            if (j == 0)
                p++;
        }
        for (int i = 0; i < row; i++) {
            for (int k = 0; k < col; k++) {
                System.out.print(matrix[i][k] + " ");
            }
            System.out.println();
        }
        setTable(matrix);
        String[] func = {"-1","-4","-1"};
        setFunction(func);
        return matrix;
    }

    public GridPane createTable(int row, int col){
        GridPane gridPane = new GridPane();
        // Создаем ячейки с названиями строк
         int k = 0;
         for (int j = 0; j < col - 1; j++) {
             Label columnLabel = new Label("x" + (j + 1));
             gridPane.add(columnLabel, j + 1, 0);
             k = j + 1;
         }

        for (int i = 0; i < row; i++) {
            Label rowLabel = new Label("x" + (k + 1));
            gridPane.add(rowLabel, 0, i + 1);
            k++;
        }

        // Создаем ячейки с названиями столбцов


        // Создаем ячейки с данными
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                TextField dataTextField = new TextField(""+j);

                gridPane.add(dataTextField, j + 1, i + 1);
            }
        }
        return gridPane;
    }

    public GridPane tableAnswer(int row, int col){
        GridPane newGridPane = new GridPane();
        String[] basis = simlexMethod.getBasis();
        String[] notBasis = simlexMethod.getNotBasis();
        String[][] answerTable = simlexMethod.getSimplexTable();
        String[] downFunction = simlexMethod.getDownFunction();
        if(downFunction == null){
            downFunction =  Arrays.copyOfRange(answerTable[answerTable.length - 1], 0, answerTable.length - 1);
        }
        int indexForDownFunc = 0;

        // Создаем ячейки с названиями строк
        for (int i = 0; i < basis.length; i++) {
            Label rowTextField = new Label(basis[i]);
            newGridPane.add(rowTextField, 0, i + 1);
        }

        // Создаем ячейки с названиями столбцов
        for (int j = 0; j < notBasis.length; j++) {
            Label columnTextField = new Label(notBasis[j]);
            newGridPane.add(columnTextField, j + 1, 0);
        }

        // Создаем ячейки с данными
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < downFunction.length; j++) {
                TextField dataTextField = new TextField(answerTable[i][j]);
                dataTextField.setEditable(false);
                newGridPane.add(dataTextField, j + 1, i + 1);
            }
            indexForDownFunc = i + 1;
        }

        for (int i = 0; i < downFunction.length; i++) {
            TextField dataTextField = new TextField(downFunction[i]);
            newGridPane.add(dataTextField, i + 1, indexForDownFunc + 1);
        }
        return newGridPane;
    }

    public String iskBasis(){
        String[][] limit = getTable();
        String[] func = getFunction();
        CalculateSimlex calculateSimlex = new CalculateSimlex(simlexMethod, func, limit);
        simlexMethod.setDownFunction(calculateSimlex.calcualteDownFunction(getTable()));
        System.out.println(Arrays.toString(simlexMethod.getDownFunction()));
        ArtificialBasisMethod artificialBasisMethod = new ArtificialBasisMethod(function, getTable(), calculateSimlex);

        System.out.println(calculateSimlex.getSimlexMethod().getAnswer());
        return calculateSimlex.getSimlexMethod().getAnswer();
    }

    public String[][] getTable() {
        return table;
    }

    public void setTable(String[][] table) {
        this.table = table;
    }

    public String[] getFunction() {
        return function;
    }

    public void setFunction(String[] function) {
        this.function = function;
    }
}
