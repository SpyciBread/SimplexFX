package com.example.simplexdemo;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class InputControll {
    private String[][] table;
    private String[] function;
    private String minOrMax = "min";
    private GridPane gridFunction;
    private String isNormalDrob = "/";
    private int method = 1;
    private boolean isSteps;
    private boolean isGauss;
    private int steps = 0;
    private int okCount = 0;
    private int rowForTable = 3, colForTable = 4;
    FractionalNumber fractionalNumber = new FractionalNumber();
    SimlexMethod simlexMethod = new SimlexMethod();

    public String[][] getArray(GridPane gridPane, GridPane gridPaneFunction, int row, int col, String drob){
        simlexMethod = new SimlexMethod();
        String[][] matrix = new String[row][col];
        ComboBox minMax = (ComboBox)gridPaneFunction.getChildren().get(2*col - 1);
        isNormalDrob = drob;
        simlexMethod.setZnakDrobi(drob);
        minOrMax = minMax.getValue().toString();
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
        String[] func = new String[col-1];
        for(int i = 0; i < col - 1; i++){
            TextField textField = (TextField) gridPaneFunction.getChildren().get(i + 1);
            func[i] = textField.getText();
        }
        setTable(matrix);
        setFunction(func);
        return matrix;
    }

    public boolean checkInput(String number){
        return number.matches("^-?[1-9]\\d*(\\/[1-9]\\d*)?$") || number.matches("^0$") || number.matches("-?(0|[1-9]\\d*)(\\.\\d+)?");
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

    public GridPane createFunction(int col){
        GridPane gridPaneFunction = new GridPane();
        Label functionLabel = new Label("Функция");
        gridPaneFunction.add(functionLabel,0,1);
        for (int j = 0; j < col - 1; j++) {
            TextField dataTextField = new TextField("");
            gridPaneFunction.add(dataTextField, j + 1, 1);
        }
        for (int j = 0; j < col - 1; j++) {
            Label columnLabel = new Label("x" + (j + 1));
            gridPaneFunction.add(columnLabel, j + 1, 0);
        }
        ComboBox<String> functionComboBox = new ComboBox<>();
        functionComboBox.getItems().addAll("min","max");
        functionComboBox.setValue("min");

        gridPaneFunction.add(functionComboBox, col, 1);
        return gridPaneFunction;
    }

    public GridPane createFileFunction(int col, File file, String[] function){
        GridPane gridPaneFunction = new GridPane();
        Label functionLabel = new Label("Функция");
        gridPaneFunction.add(functionLabel,0,1);
        for (int j = 0; j < col - 1; j++) {
            TextField dataTextField = new TextField(function[j]);
            gridPaneFunction.add(dataTextField, j + 1, 1);
        }
        for (int j = 0; j < col - 1; j++) {
            Label columnLabel = new Label("x" + (j + 1));
            gridPaneFunction.add(columnLabel, j + 1, 0);
        }
        ComboBox<String> functionComboBox = new ComboBox<>();
        functionComboBox.getItems().addAll("min","max");
        functionComboBox.setValue("min");

        gridPaneFunction.add(functionComboBox, col, 1);
        return gridPaneFunction;
    }

    public GridPane tableAnswer(int row, int col){
        GridPane newGridPane = new GridPane();
        String[] basis = simlexMethod.getBasis();
        String[] notBasis = simlexMethod.getNotBasis();
        String[][] answerTable = simlexMethod.getSimplexTable();
        String[] downFunction = answerTable[answerTable.length - 1];
        if(downFunction == null){
            downFunction =  Arrays.copyOfRange(answerTable[answerTable.length - 1], 0, answerTable.length - 1);
        }
        int indexForDownFunc = 0;
        if(!isSteps)
            if(isNormalDrob.equals(".")){
                //for (int i = 0; i < answerTable.length; i++) {
                    simlexMethod.setZnakDrobi(".");
                    //downFunction[i] = fractionalNumber.toDesDrob(downFunction[i]);
//                    for (int j = 0; j < answerTable[0].length; j++) {
//                        if(!answerTable[i][j].contains("."))
//                            answerTable[i][j] = fractionalNumber.toDesDrob(answerTable[i][j]);
//                    }

               // }
            }
        if(isNormalDrob.equals("/")){
            for (int i = 0; i < answerTable.length; i++) {
                //downFunction[i] = fractionalNumber.toDesDrob(downFunction[i]);
                for (int j = 0; j < answerTable[0].length; j++) {
                    if(answerTable[i][j].contains("."))
                        answerTable[i][j] = fractionalNumber.toStandartDrob(answerTable[i][j]);
                }

            }
        }

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

    public String preCheck(){
        if(steps != 0){
            String[][] limit = getTable();
            String[] func = getFunction();
            CalculateSimlex calculateSimlex = new CalculateSimlex(simlexMethod, func, limit);
            String answer = calculateSimlex.checkAnswerPoShagam(simlexMethod, simlexMethod.getSimplexTable(), simlexMethod.getBasis(), getIsGauss());
            if(answer.equals("Ok")){
                if(!getIsGauss()){
                    if(okCount == 0){
                        calculateSimlex.newSimplexTablePoSagam(simlexMethod.getSimplexTable());
                        putSteps(steps);
                    }
                    else
                        return "Calculate";
                }
                else
                    return "Calculate";
                okCount++;
                return "ready";
            }
            if(answer.equals("Функция неограничена") && simlexMethod.getMinElInfo().equals("Функция неограничена")){
                return "bad";
            }
            if(answer.equals("Система несовместна")){
                return "bad";
            }
        }
        return "not";
    }

    public String poShagam(int step, int index){
        if(step == 0)
            simlexMethod = new SimlexMethod();
        simlexMethod.setZnakDrobi(isNormalDrob);
            String[][] limit = getTable();
            String[] func = getFunction();
        if(step == 0){
            if(minOrMax.equals("max")) {
                for(int i = 0; i < func.length; i++){
                    func[i] = replacingTheSign(func[i]);
                }
                simlexMethod.setMinStatus("max");
            }
            else
                simlexMethod.setMinStatus("min");
        }

            for(int i = 0; i < limit.length; i++){
                for (int j = 0; j < limit[0].length; j++){
                    if(!checkInput(limit[i][j]))
                        return "Введено не число";
                    if(limit[i][j].contains(".")){
                        limit[i][j] = simlexMethod.getFractionalNumber().toStandartDrob(limit[i][j]);
                    }
                }
            }
            for (int i = 0; i < func.length; i++){
                if(!checkInput(func[i]))
                    return "Введено не число";
                if(func[i].contains(".")){
                    func[i] = simlexMethod.getFractionalNumber().toStandartDrob(func[i]);
                }
            }
            for(int i = 0; i < limit.length; i++){
                if(limit[i][limit[0].length - 1].charAt(0) == '-'){
                    for (int j = 0; j < limit[0].length; j++)
                        limit[i][j] = replacingTheSign(limit[i][j]);
                }
            }
        String[][] s;
        CalculateSimlex calculateSimlex = new CalculateSimlex(simlexMethod, func, limit);
        //ArtificialBasisMethod artificialBasisMethod = new ArtificialBasisMethod(true, func, limit, calculateSimlex);
        if(!getIsGauss()){
            if(step == 0){
                HashMap<Integer, String[][]> stepTable = new HashMap<>();
                HashMap<Integer, String[]> basisStep = new HashMap<>();
                HashMap<Integer, String[]> notBasisStep = new HashMap<>();
                simlexMethod.setIterationForStep(0);
                simlexMethod.setBasisSteps(basisStep);
                simlexMethod.setNotBasisSteps(notBasisStep);
                simlexMethod.setTableSteps(stepTable);
                calculateSimlex.calculateSimplexTablePoShagam(limit, calculateSimlex.calcualteDownFunction(limit));
            }
            else{
                if(!simlexMethod.getMinElInfo().equals("no")){
                    if(calculateSimlex.checkAnswerPoShagam(simlexMethod, simlexMethod.getSimplexTable(), simlexMethod.getBasis(), getIsGauss()).equals("Ok")){
                        return "kk";
                    }
                }
                else {
                    calculateSimlex.helpCalculateSimplexTablePoShagam(simlexMethod.getSimplexTable(),index);
                }
            }
            putSteps(step);
            return "Ok";
        }
        else {
            if(step == 1){
                HashMap<Integer, String[][]> stepTable = new HashMap<>();
                HashMap<Integer, String[]> basisStep = new HashMap<>();
                HashMap<Integer, String[]> notBasisStep = new HashMap<>();
                simlexMethod.setIterationForStep(1);
                simlexMethod.setBasisSteps(basisStep);
                simlexMethod.setNotBasisSteps(notBasisStep);
                simlexMethod.setTableSteps(stepTable);
                //calculateSimlex.calculateSimplexTablePoShagam(limit, calculateSimlex.calcualteDownFunction(limit));
                calculateSimlex.getAnswer(simlexMethod.getSimplexTable(), simlexMethod.getBasis(), simlexMethod.getNotBasis());
            }
            //else{
                if(!simlexMethod.getMinElInfo().equals("no")){
                    if(calculateSimlex.checkAnswerPoShagam(simlexMethod, simlexMethod.getSimplexTable(), simlexMethod.getBasis(), getIsGauss()).equals("Ok")){
                        return "kk";
                    }
                }
                else {//тут проблема
                    if(step != 1)
                        calculateSimlex.helpCalculateSimplexTablePoShagam(simlexMethod.getSimplexTable(),index);
                }
            //}
            putSteps(step);
            return "Ok";
        }
    }

    private void putSteps(int step){
        String[][] table = new String[simlexMethod.getSimplexTable().length][];
        for (int i = 0; i < simlexMethod.getSimplexTable().length; i++) {
            table[i] = Arrays.copyOf(simlexMethod.getSimplexTable()[i], simlexMethod.getSimplexTable()[i].length);
        }

        String[] basis = Arrays.copyOf(simlexMethod.getBasis(), simlexMethod.getBasis().length);
        String[] notBasis = Arrays.copyOf( simlexMethod.getNotBasis(), simlexMethod.getNotBasis().length);


        simlexMethod.putBasisSteps(step, basis);
        simlexMethod.putNotBasisSteps(step, notBasis);
        simlexMethod.putTableSteps(step, table);
        simlexMethod.addIterationForStep();
    }

    public String iskBasis(){
        simlexMethod = new SimlexMethod();
        simlexMethod.setZnakDrobi(isNormalDrob);
        String[][] limit = getTable();
        String[] func = getFunction();
        if(minOrMax.equals("max")) {
            for(int i = 0; i < func.length; i++){
               func[i] = replacingTheSign(func[i]);
            }
            simlexMethod.setMinStatus("max");
        }
        else
            simlexMethod.setMinStatus("min");
        for(int i = 0; i < limit.length; i++){
            for (int j = 0; j < limit[0].length; j++){
                if(!checkInput(limit[i][j]))
                    return "Введено не число";
                if(limit[i][j].contains(".")){
                    limit[i][j] = simlexMethod.getFractionalNumber().toStandartDrob(limit[i][j]);
                }
            }
        }
        for (int i = 0; i < func.length; i++){
            if(!checkInput(func[i]))
                return "Введено не число";
            if(func[i].contains(".")){
                func[i] = simlexMethod.getFractionalNumber().toStandartDrob(func[i]);
            }
        }
        for(int i = 0; i < limit.length; i++){
            if(limit[i][limit[0].length - 1].charAt(0) == '-'){
                for (int j = 0; j < limit[0].length; j++)
                limit[i][j] = replacingTheSign(limit[i][j]);
            }
        }
        CalculateSimlex calculateSimlex = new CalculateSimlex(simlexMethod, func, limit);
        simlexMethod.setDownFunction(calculateSimlex.calcualteDownFunction(getTable()));
        ArtificialBasisMethod artificialBasisMethod = new ArtificialBasisMethod(false, function, getTable(), calculateSimlex);

        System.out.println(calculateSimlex.getSimlexMethod().getAnswer());
        return calculateSimlex.getSimlexMethod().getAnswer();
    }

    public GridPane tableAnswerGauss(){
        GridPane newGridPane = new GridPane();
        String[][] answerTable = simlexMethod.getGaussTable();
            if(isNormalDrob.equals(".")){
                simlexMethod.setZnakDrobi(".");
//                for (int i = 0; i < answerTable.length; i++) {
//                    for (int j = 0; j < answerTable[0].length; j++) {
//                        answerTable[i][j] = fractionalNumber.toDesDrob(answerTable[i][j]);
//                    }
//                }
            }

        // Создаем ячейки с данными
        for (int i = 0; i < answerTable.length; i++) {
            for (int j = 0; j < answerTable[0].length; j++) {
                TextField dataTextField = new TextField(answerTable[i][j]);
                dataTextField.setEditable(false);
                newGridPane.add(dataTextField, j + 1, i + 1);
            }
        }

        return newGridPane;
    }

    public String gaussPoshagam(int[] basis){
        simlexMethod = new SimlexMethod();
        simlexMethod.setZnakDrobi(isNormalDrob);
        String[][] limit = getTable();
        String[] func = getFunction();
        int rang = 0;
        for (int i = 0; i < limit[0].length - 1; i++){
            if(basis[i] == 1)
                rang++;
        }
        if(rang < limit.length){
            for (int i = 0; i < limit[0].length - 1; i++){
                if(rang == limit.length)
                    break;
                else
                {
                    if(basis[i] != 1){
                        basis[i] = 1;
                        rang++;
                    }
                }
            }
        }
        if(rang > limit.length){
            return "Неверно введен базис";
        }
        if(minOrMax.equals("max")) {
            for(int i = 0; i < func.length; i++){
                func[i] = replacingTheSign(func[i]);
            }
            simlexMethod.setMinStatus("max");
        }
        else
            simlexMethod.setMinStatus("min");
        for(int i = 0; i < limit.length; i++){
            for (int j = 0; j < limit[0].length; j++){
                if(!checkInput(limit[i][j]))
                    return "Введено не число";
                if(limit[i][j].contains(".")){
                    limit[i][j] = simlexMethod.getFractionalNumber().toStandartDrob(limit[i][j]);
                }
            }
        }
        for (int i = 0; i < func.length; i++){
            if(!checkInput(func[i]))
                return "Введено не число";
            if(func[i].contains(".")){
                func[i] = simlexMethod.getFractionalNumber().toStandartDrob(func[i]);
            }
        }
//        for(int i = 0; i < limit.length; i++){
//            if(limit[i][limit[0].length - 1].charAt(0) == '-'){
//                for (int j = 0; j < limit[0].length; j++)
//                    limit[i][j] = replacingTheSign(limit[i][j]);
//            }
//        }
//        CalculateSimlex calculateSimlex = new CalculateSimlex(simlexMethod, func, limit);
//        simlexMethod.setDownFunction(calculateSimlex.calcualteDownFunction(getTable()));
        //ArtificialBasisMethod artificialBasisMethod = new ArtificialBasisMethod(false, function, getTable(), calculateSimlex);
//        if(!artificialBasisMethod.checkAnswer(simlexMethod, simlexMethod.getSimplexTable(), simlexMethod.getBasis()).equals("Ok")){
//            return "Система несовместна или имеет бесконечно много решений";
//        }
        //simlexMethod = new SimlexMethod();
        CalculateSimlex calculateSimlex2 = new CalculateSimlex(simlexMethod, func, limit);
        Gauss gauss = new Gauss(limit, basis, calculateSimlex2, func, true);
        if(gauss.getSimlexMethod().getAnswer() != null){
            if(gauss.getSimlexMethod().getAnswer().equals("Система несовместна") || gauss.getSimlexMethod().getAnswer().equals("Недопустимый базис")
            || gauss.getSimlexMethod().getAnswer().equals("Невозможно привести к единичной матрице при таком базисе")){
                return gauss.getSimlexMethod().getAnswer();
            }
            else
                gauss.newSimplexTablePoShagam(simlexMethod.getGaussTable());
        }
        else
            gauss.newSimplexTablePoShagam(simlexMethod.getGaussTable());
        return gauss.getSimlexMethod().getAnswer();
    }

    public String gauss(int[] basis){
        simlexMethod = new SimlexMethod();
        simlexMethod.setZnakDrobi(isNormalDrob);
        String[][] limit = getTable();
        String[] func = getFunction();
        int rang = 0;
        for (int i = 0; i < limit[0].length - 1; i++){
            if(basis[i] == 1)
                rang++;
        }
        if(rang < limit.length){
            for (int i = 0; i < limit[0].length - 1; i++){
                if(rang == limit.length)
                    break;
                else
                {
                    if(basis[i] != 1){
                        basis[i] = 1;
                        rang++;
                    }
                }
            }
        }
        if(rang > limit.length){
            return "Неверно введен базис";
        }
        if(minOrMax.equals("max")) {
            for(int i = 0; i < func.length; i++){
                func[i] = replacingTheSign(func[i]);
            }
            simlexMethod.setMinStatus("max");
        }
        else
            simlexMethod.setMinStatus("min");
        for(int i = 0; i < limit.length; i++){
            for (int j = 0; j < limit[0].length; j++){
                if(!checkInput(limit[i][j]))
                    return "Введено не число";
                if(limit[i][j].contains(".")){
                    limit[i][j] = simlexMethod.getFractionalNumber().toStandartDrob(limit[i][j]);
                }
            }
        }
        for (int i = 0; i < func.length; i++){
            if(!checkInput(func[i]))
                return "Введено не число";
            if(func[i].contains(".")){
                func[i] = simlexMethod.getFractionalNumber().toStandartDrob(func[i]);
            }
        }
//        for(int i = 0; i < limit.length; i++){
//            if(limit[i][limit[0].length - 1].charAt(0) == '-'){
//                for (int j = 0; j < limit[0].length; j++)
//                    limit[i][j] = replacingTheSign(limit[i][j]);
//            }
//        }



//        CalculateSimlex calculateSimlex = new CalculateSimlex(simlexMethod, func, limit);
//        simlexMethod.setDownFunction(calculateSimlex.calcualteDownFunction(getTable()));
////        ArtificialBasisMethod artificialBasisMethod = new ArtificialBasisMethod(false, function, getTable(), calculateSimlex);
////        if(!artificialBasisMethod.checkAnswer(simlexMethod, simlexMethod.getSimplexTable(), simlexMethod.getBasis()).equals("Ok")){
////            return "Система несовместна или имеет бесконечно много решений";
////        }
//        simlexMethod = new SimlexMethod();
        CalculateSimlex calculateSimlex2 = new CalculateSimlex(simlexMethod, func, limit);
        Gauss gauss = new Gauss(limit, basis, calculateSimlex2, func, false);
        if(gauss.getSimlexMethod().getAnswer() != null){
            if(gauss.getSimlexMethod().getAnswer().equals("Система несовместна")){
                return gauss.getSimlexMethod().getAnswer();
            }
        }
        return gauss.getSimlexMethod().getAnswer();
    }

    public GridPane readFile(File file){
        GridPane newGridPane = new GridPane();
        int row = 3; int col = 4;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.trim().split("\\s+");
                if(i == 0){
                    i++;
                }
                else {
                   col = values.length;
                    i++;
                }
            }
            row = i - 1;
        }catch (IOException e) {
            e.printStackTrace();
        }
        rowForTable = row;
        colForTable = col;
        String[][] limits = new String[row][col];
        String[] function = new String[col - 1];

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int i = 0;
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.trim().split("\\s+");
                if(i == 0){
                    for (int j = 0; j < values.length; j++) {
                        function[j] = values[j];
                    }
                    i++;
                }
                else {
                    for (int j = 0; j < values.length; j++) {
                        limits[i - 1][j] = values[j];
                    }
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int k = 0;
        for (int j = 0; j < col - 1; j++) {
            Label columnLabel = new Label("x" + (j + 1));
            newGridPane.add(columnLabel, j + 1, 0);
            k = j + 1;
        }
        for (int i = 0; i < row; i++) {
            Label rowLabel = new Label("x" + (k + 1));
            newGridPane.add(rowLabel, 0, i + 1);
            k++;
        }
        // Создаем ячейки с данными
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                TextField dataTextField = new TextField(limits[i][j]);
                newGridPane.add(dataTextField, j + 1, i + 1);
            }
        }
        setGridFunction(createFileFunction(col,file,function));
        return newGridPane;
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

    public void setOkCount(int okCount) {
        this.okCount = okCount;
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

    public GridPane getGridFunction() {
        return gridFunction;
    }

    public void setGridFunction(GridPane gridFunction) {
        this.gridFunction = gridFunction;
    }

    public void setFunction(String[] function) {
        this.function = function;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
    public void decSteps() {
        this.steps--;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public void addSteps() {
        steps++;
    }

    public SimlexMethod getSimlexMethod() {
        return simlexMethod;
    }

    public void setSimlexMethod(SimlexMethod simlexMethod) {
        this.simlexMethod = simlexMethod;
    }

    public boolean isSteps() {
        return isSteps;
    }

    public void setIsSteps(boolean steps) {
        isSteps = steps;
    }

    public boolean getIsGauss() {
        return isGauss;
    }

    public void setGauss(boolean gauss) {
        isGauss = gauss;
    }

    public int getRowForTable() {
        return rowForTable;
    }

    public void setRowForTable(int rowForTable) {
        this.rowForTable = rowForTable;
    }

    public int getColForTable() {
        return colForTable;
    }

    public void setColForTable(int colForTable) {
        this.colForTable = colForTable;
    }
}
