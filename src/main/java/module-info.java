module com.metait.flutterprojectversion {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.desktop;


    opens com.metait.flutterprojectversion to javafx.fxml;
    exports com.metait.flutterprojectversion;
}