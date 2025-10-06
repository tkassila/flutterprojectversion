package com.metait.flutterprojectversion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ResourceBundle;

public class FlutterProjectVersionApplication extends Application {
    private FlutterProjectVersionController controller = null;
    public FlutterProjectVersionController getController()
    {
        return controller;
    }

    @Override
    public void start(Stage stage) throws IOException {
        // FXMLLoader fxmlLoader = new FXMLLoader(FlutterProjectVersionApplication.class.getResource("flutterproj-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("flutterproj-view.fxml"));
        // remove xml block from .fxml file: fx:controller="com.metait.javafxplayer.PlayerController"
        controller = new FlutterProjectVersionController();
        fxmlLoader.setController(controller);
        controller.setPrimaryStage(stage);
        Scene scene = new Scene(fxmlLoader.load(), 920, 540);
        stage.setTitle("Read flutter project directories and search flutter versions");
        stage.setScene(scene);
       // stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }
}
