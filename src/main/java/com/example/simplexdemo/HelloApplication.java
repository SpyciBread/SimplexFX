package com.example.simplexdemo;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class HelloApplication extends Application {
    InputControll inputControll = new InputControll();
    GridPane clickedGridPane;
    int indexOfRefEl;
    int afterNewTableCount = -1;
    @Override
    public void start(Stage primaryStage) {
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        HBox hBoxVibor = new HBox();
        ComboBox<Integer> rowsComboBox = new ComboBox<>();
        rowsComboBox.getItems().addAll(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        rowsComboBox.setValue(3);

        // Создаем ComboBox для выбора количества столбцов
        ComboBox<Integer> columnsComboBox = new ComboBox<>();
        columnsComboBox.getItems().addAll(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        columnsComboBox.setValue(4);
        int row = rowsComboBox.getValue();
        int col = columnsComboBox.getValue();

        // Создаем ScrollPane для текущей таблицы
        //ScrollPane scrollPane = new ScrollPane();
        GridPane gridPane = new GridPane();
        GridPane gridPaneFunction = new GridPane();

        // Создаем ячейки с названиями столбцов
        int k =0;
        for (int j = 0; j < col - 1; j++) {
            Label columnLabel = new Label("x" + (j + 1));
            gridPane.add(columnLabel, j + 1, 0);
            k = j + 1;
        }

        // Создаем ячейки с названиями строк
        for (int i = 0; i < row ; i++) {
            Label rowLabel = new Label("x" + (k + 1));
            gridPane.add(rowLabel, 0, i + 1);
            k++;
        }

        // Создаем ячейки с данными
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                TextField dataTextField = new TextField(""+j);
                gridPane.add(dataTextField, j + 1, i + 1);
            }
        }
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

        ComboBox<String> numbersComboBox = new ComboBox<>();
        numbersComboBox.getItems().addAll("/",".");
        numbersComboBox.setValue("/");

        gridPaneFunction.add(functionComboBox, col, 1);

        Label rowsLabel = new Label("Количество строк:");
        Label columnsLabel = new Label("Количество столбцов:");
        Label labelBasis = new Label("Базис");

        hBox.getChildren().add(rowsLabel);
        hBox.getChildren().add(rowsComboBox);
        hBox.getChildren().add(columnsLabel);
        hBox.getChildren().add(columnsComboBox);
        hBox.getChildren().add(numbersComboBox);
        hBox.getChildren().add(labelBasis);

        Button calculateButton = new Button("Решить");
        Button createButton = new Button("Создать");
        Button openButton = new Button("Считать из файла");

        Button simplexButton = new Button("Симплекс метод");
        Button iskusstButton = new Button("Искусственный базис");
        iskusstButton.setStyle("-fx-background-color: green;");
        Button poShagamButton = new Button("По шагам");
        Button stepBack = new Button("Шаг назад");
        stepBack.setDisable(true);
        Button pickElButton = new Button("Выбрать опорный элемент");
        pickElButton.setDisable(true);
        openButton.setDisable(true);
        hBoxVibor.getChildren().addAll(simplexButton,iskusstButton,poShagamButton, stepBack ,pickElButton);
        vBox.getChildren().add(hBoxVibor);
        vBox.getChildren().add(hBox);
        vBox.getChildren().add(createButton);
        vBox.getChildren().add(calculateButton);
        vBox.getChildren().add(openButton);
        vBox.getChildren().add(gridPaneFunction);
        vBox.getChildren().add(gridPane);
        clickedGridPane = (GridPane)vBox.getChildren().get(6);
        simplexButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                inputControll.setMethod(2);
                simplexButton.setStyle("-fx-background-color: green;");
                iskusstButton.setStyle("");
                poShagamButton.setStyle("");
                for(int i = 0; i < columnsComboBox.getValue() - 1; i++){
                    ComboBox<Integer> pickedBasis = new ComboBox<>();
                    pickedBasis.getItems().addAll(0,1);
                    pickedBasis.setValue(0);
                    hBox.getChildren().add(pickedBasis);
                }
            }
        });

        stepBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(inputControll.getSteps() > 1){
                    calculateButton.setDisable(true);
                    pickElButton.setDisable(false);
                    int endIndex = vBox.getChildren().size();
                    vBox.getChildren().remove(6 + inputControll.getSteps(), endIndex);
                    inputControll.decSteps();
                    inputControll.simlexMethod.decIterationForStep();

//                    inputControll.simlexMethod.removeTableSteps(inputControll.getSteps());
//                    inputControll.simlexMethod.removeBasisSteps(inputControll.getSteps());
//                    inputControll.simlexMethod.removeNotBasisSteps(inputControll.getSteps());
                    String[][] table = new String[inputControll.simlexMethod.getTableSteps(inputControll.getSteps() - 1).length][];
                    for (int i = 0; i < inputControll.simlexMethod.getTableSteps(inputControll.getSteps() - 1).length; i++) {
                        table[i] = Arrays.copyOf(inputControll.simlexMethod.getTableSteps(inputControll.getSteps() - 1)[i],
                                inputControll.simlexMethod.getTableSteps(inputControll.getSteps() - 1)[i].length);
                    }

                    String[] basis = Arrays.copyOf(inputControll.simlexMethod.getBasisSteps(inputControll.getSteps() - 1), inputControll.simlexMethod.getBasisSteps(inputControll.getSteps() - 1).length);
                    String[] notBasis = Arrays.copyOf(inputControll.simlexMethod.getNotBasisSteps(inputControll.getSteps() - 1),inputControll.simlexMethod.getNotBasisSteps(inputControll.getSteps() - 1).length);

                    inputControll.simlexMethod.setSimplexTable(table);
                    inputControll.simlexMethod.setBasis(basis);
                    inputControll.simlexMethod.setNotBasis(notBasis);
                    if(afterNewTableCount > -1){
                        afterNewTableCount--;
                    }
                    if(afterNewTableCount == -1)
                        inputControll.setOkCount(0);
                }
            }
        });

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                clickedGridPane = (GridPane) vBox.getChildren().get(6 + inputControll.getSteps());
                int endIndex = clickedGridPane.getChildren().size();
                int startIndex = endIndex - inputControll.getSimlexMethod().getSimplexTable()[0].length;

                int needIndex = (clickedGridPane.getChildren().indexOf(e.getSource()) - startIndex) % (columnsComboBox.getValue());
                TextField textField = (TextField) e.getSource();
                clickedGridPane = (GridPane) vBox.getChildren().get(6 + inputControll.getSteps());
                for (Node node : clickedGridPane.getChildren()) {
                    if (node instanceof TextField) {
                        if(clickedGridPane.getChildren().indexOf(node) < (endIndex - 1) && clickedGridPane.getChildren().indexOf(node) >= startIndex){
                            TextField textField2 = (TextField) node;
                            if(textField2.getText().charAt(0) == '-'){
                                textField2.setStyle("-fx-background-color: yellow;");
                            }
                        }
                    }
                }
                textField.setStyle("-fx-background-color: green");
                indexOfRefEl = needIndex;
                System.out.println(indexOfRefEl);
                calculateButton.setDisable(false);
            }
        };

        pickElButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //calculateButton.setDisable(false);
                clickedGridPane = (GridPane) vBox.getChildren().get(6 + inputControll.getSteps());
                int endIndex = clickedGridPane.getChildren().size();
                int startIndex = endIndex - inputControll.getSimlexMethod().getSimplexTable()[0].length;
                if(inputControll.preCheck().equals("ready")){
                    vBox.getChildren().add(inputControll.tableAnswer(inputControll.getSimlexMethod().getSimplexTable().length - 1, inputControll.getSimlexMethod().getSimplexTable()[0].length));
                    inputControll.addSteps();
                    afterNewTableCount = 0;
                }
                if(inputControll.preCheck().equals("bad")){
                    Label badAnswer = new Label(inputControll.getSimlexMethod().getAnswer());
                    vBox.getChildren().add(badAnswer);
                    //inputControll.addSteps();
                    pickElButton.setDisable(true);
                }
                if(inputControll.preCheck().equals("Calculate")){
                    Label badAnswer = new Label(inputControll.getSimlexMethod().getAnswer());
                    vBox.getChildren().add(badAnswer);
                    //inputControll.addSteps();
                    pickElButton.setDisable(true);
                }
                //inputControll.addSteps();
                for (Node node : clickedGridPane.getChildren()) {
                    if (node instanceof TextField) {
                        if(clickedGridPane.getChildren().indexOf(node) < (endIndex - 1) && clickedGridPane.getChildren().indexOf(node) >= startIndex){
                            TextField textField = (TextField) node;
                            if(textField.getText().charAt(0) == '-'){
                                textField.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
                                textField.setStyle("-fx-background-color: yellow;");
                            }
                        }
                    }
                }
            }
        });

        iskusstButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                inputControll.setMethod(1);
                iskusstButton.setStyle("-fx-background-color: green;");
                poShagamButton.setStyle("");
                simplexButton.setStyle("");
            }
        });

        poShagamButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                inputControll.setIsSteps(!inputControll.isSteps());
                if(inputControll.isSteps()){
                    poShagamButton.setStyle("-fx-background-color: green;");
                    pickElButton.setDisable(false);
                    stepBack.setDisable(false);
                }
                else{
                    poShagamButton.setStyle("");
                    pickElButton.setDisable(true);
                    stepBack.setDisable(true);
                }

            }
        });

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(inputControll.getMethod() == 2){
                    while (hBox.getChildren().size() > 6){
                        hBox.getChildren().remove(hBox.getChildren().size() - 1);
                    }
                    for(int i = 0; i < columnsComboBox.getValue() - 1; i++){
                        ComboBox<Integer> pickedBasis = new ComboBox<>();
                        pickedBasis.getItems().addAll(0,1);
                        pickedBasis.setValue(0);
                        hBox.getChildren().add(pickedBasis);
                    }
                }
                afterNewTableCount = -1;
                inputControll.setOkCount(0);
                inputControll.setSteps(0);
                int row = rowsComboBox.getValue();
                int col = columnsComboBox.getValue();
                int endIndex = vBox.getChildren().size() - 1;
                vBox.getChildren().remove(5, endIndex);
                vBox.getChildren().set(5, inputControll.createFunction(col));
                vBox.getChildren().add(6, inputControll.createTable(row, col));
                calculateButton.setDisable(false);
                openButton.setDisable(false);
            }
        });

        calculateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Создаем новую таблицу
                if(!inputControll.isSteps()){
                    int row = rowsComboBox.getValue();
                    int col = columnsComboBox.getValue();
                    String drob = numbersComboBox.getValue();
                    Label answer = new Label("");
                    if(inputControll.getMethod() == 1){
                        inputControll.getArray((GridPane)vBox.getChildren().get(6), (GridPane)vBox.getChildren().get(5), row, col, drob);
                        answer = new Label(inputControll.iskBasis());
                    }
                    if(inputControll.getMethod() == 2){
                        inputControll.getArray((GridPane)vBox.getChildren().get(6), (GridPane)vBox.getChildren().get(5), row, col, drob);
                        int[] pickbasis = new int[columnsComboBox.getValue() - 1];
                        for(int i = 0; i < hBox.getChildren().size() - 6; i++){
                            ComboBox<Integer> textFieldBasis = (ComboBox<Integer>) hBox.getChildren().get(i + 6);
                            pickbasis[i]  = textFieldBasis.getValue();
                        }
                        answer = new Label(inputControll.gauss(pickbasis));
                    }
                    int endIndex = vBox.getChildren().size() - 1;
                    vBox.getChildren().remove(6, endIndex);
                    vBox.getChildren().add(answer);
                    if(!answer.getText().equals("Введено не число") || !answer.getText().equals("Неверно введен базис"))
                        vBox.getChildren().add(inputControll.tableAnswer(row,col));
                    else
                    {
                        calculateButton.setDisable(true);
                        openButton.setDisable(true);
                        return;
                    }
                    calculateButton.setDisable(true);
                    openButton.setDisable(true);
                }
                else {
                    if(inputControll.getMethod() == 1){
                        int step = inputControll.getSteps();
                        int row = rowsComboBox.getValue();
                        int col = columnsComboBox.getValue();
                        String drob = numbersComboBox.getValue();

                        if(step == 0){
                            inputControll.getArray((GridPane)vBox.getChildren().get(6), (GridPane)vBox.getChildren().get(5), row, col, drob);
                            if(!inputControll.poShagam(0,0).equals("Введено не число")){
                                vBox.getChildren().add(inputControll.tableAnswer(row,col));
                                inputControll.addSteps();
                                calculateButton.setDisable(true);
                                indexOfRefEl = -1;
                            }
                            else{
                                vBox.getChildren().add(new Label("Введено не число"));
                                calculateButton.setDisable(true);
                            }
                        }
                        else{
                            clickedGridPane = (GridPane) vBox.getChildren().get(6 + inputControll.getSteps());
                            int endIndex = clickedGridPane.getChildren().size();
                            int startIndex = endIndex - inputControll.getSimlexMethod().getSimplexTable()[0].length;
                            for (Node node : clickedGridPane.getChildren()) {
                                if (node instanceof TextField) {
                                    if(clickedGridPane.getChildren().indexOf(node) < (endIndex - 1) && clickedGridPane.getChildren().indexOf(node) >= startIndex){
                                        TextField textField = (TextField) node;
                                        if(textField.getText().charAt(0) == '-'){
                                            textField.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
                                        }
                                    }
                                }
                            }
                            calculateButton.setDisable(true);
                            String tmpAnswer = inputControll.poShagam(step, indexOfRefEl);
                            if(afterNewTableCount > -1)
                            {
                                afterNewTableCount++;
                            }
                            if(tmpAnswer.equals("Ok")){
                                vBox.getChildren().add(inputControll.tableAnswer(row,col));
                                inputControll.addSteps();
                            }
                            else {
                                if (!tmpAnswer.equals("kk")) {
                                    calculateButton.setDisable(true);
                                    vBox.getChildren().add(inputControll.tableAnswer(row, col));
                                    inputControll.addSteps();
                                } else {
                                    vBox.getChildren().add(new Label(tmpAnswer));
                                    inputControll.addSteps();
                                }
                            }
                        }
                    }
                    else {
                        //гаус по шагам
                    }
                }
            }
        });

        openButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openButton.setDisable(true);
                int row = rowsComboBox.getValue();
                int col = columnsComboBox.getValue();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                File file = fileChooser.showOpenDialog(null);
                if (file != null) {
                    try {
                        Scanner scanner = new Scanner(file);
                        vBox.getChildren().set(6, inputControll.readFile(row, col, file));
                        vBox.getChildren().set(5, inputControll.getGridFunction());
                        scanner.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vBox);

        Scene scene = new Scene(scrollPane, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}