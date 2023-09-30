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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class HelloApplication extends Application {
    InputControll inputControll = new InputControll();
    @Override
    public void start(Stage primaryStage) {
        VBox vBox = new VBox();
        HBox hBox = new HBox();
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
        Label rowsLabel = new Label("Количество строк:");
        Label columnsLabel = new Label("Количество столбцов:");

        hBox.getChildren().add(rowsLabel);
        hBox.getChildren().add(rowsComboBox);
        hBox.getChildren().add(columnsLabel);
        hBox.getChildren().add(columnsComboBox);

        Button calculateButton = new Button("Решить");
        Button createButton = new Button("Создать");
        vBox.getChildren().add(hBox);
        vBox.getChildren().add(createButton);
        vBox.getChildren().add(calculateButton);
        vBox.getChildren().add(gridPane);

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int row = rowsComboBox.getValue();
                int col = columnsComboBox.getValue();
                int endIndex = vBox.getChildren().size() - 1;
                vBox.getChildren().remove(3, endIndex);
                vBox.getChildren().set(3, inputControll.createTable(row, col));
                calculateButton.setDisable(false);
            }
        });

        calculateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Создаем новую таблицу
                //newScrollPane.setContent(newGridPane);
                int row = rowsComboBox.getValue();
                int col = columnsComboBox.getValue();
                inputControll.getArray((GridPane)vBox.getChildren().get(3), row, col);
                Label answer = new Label(inputControll.iskBasis());
                int endIndex = vBox.getChildren().size() - 1;
                vBox.getChildren().remove(3, endIndex);
                vBox.getChildren().add(answer);
                vBox.getChildren().add(inputControll.tableAnswer(row,col));
                calculateButton.setDisable(true);
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