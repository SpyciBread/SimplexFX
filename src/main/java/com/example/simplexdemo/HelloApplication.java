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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class HelloApplication extends Application {
    InputControll inputControll = new InputControll();
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

        hBox.getChildren().add(rowsLabel);
        hBox.getChildren().add(rowsComboBox);
        hBox.getChildren().add(columnsLabel);
        hBox.getChildren().add(columnsComboBox);
        hBox.getChildren().add(numbersComboBox);

        Button calculateButton = new Button("Решить");
        Button createButton = new Button("Создать");
        Button openButton = new Button("Считать из файла");

        Button simplexButton = new Button("Симплекс метод");
        Button iskusstButton = new Button("Искусственный базис");
        iskusstButton.setStyle("-fx-background-color: green;");
        Button poShagamButton = new Button("По шагам");
        openButton.setDisable(true);
        hBoxVibor.getChildren().addAll(simplexButton,iskusstButton,poShagamButton);
        vBox.getChildren().add(hBoxVibor);
        vBox.getChildren().add(hBox);
        vBox.getChildren().add(createButton);
        vBox.getChildren().add(calculateButton);
        vBox.getChildren().add(openButton);
        vBox.getChildren().add(gridPaneFunction);

        vBox.getChildren().add(gridPane);

        simplexButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                inputControll.setMethod(2);
                simplexButton.setStyle("-fx-background-color: green;");
                iskusstButton.setStyle("");
                poShagamButton.setStyle("");
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
                inputControll.setMethod(3);
                poShagamButton.setStyle("-fx-background-color: green;");
                iskusstButton.setStyle("");
                simplexButton.setStyle("");
            }
        });

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
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
                    answer = new Label(inputControll.gauss());
                }
                int endIndex = vBox.getChildren().size() - 1;
                vBox.getChildren().remove(6, endIndex);
                vBox.getChildren().add(answer);
                if(!answer.getText().equals("Введено не число"))
                    vBox.getChildren().add(inputControll.tableAnswer(row,col));
                calculateButton.setDisable(true);
                openButton.setDisable(true);
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




        Scene scene = new Scene(scrollPane, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}