module com.example.simplexdemo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.simplexdemo to javafx.fxml;
    exports com.example.simplexdemo;
}