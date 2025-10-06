package com.metait.flutterprojectversion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class FlutterProjectVersionApplication extends Application {
    private FlutterProjectVersionController controller = null;
    private String strTerminalFontSize = "14";
    public FlutterProjectVersionController getController()
    {
        return controller;
    }

    @Override
    public void init()
    {
       // super.init();;
        Parameters parameters = getParameters ();

        Map<String, String> namedParameters = parameters.getNamed ();
        List<String> rawArguments = parameters.getRaw ();
        List<String> unnamedParameters = parameters.getUnnamed ();

        /*
        System.out.println ("\nnamedParameters -");
        for (Map.Entry<String, String> entry : namedParameters.entrySet ())
            System.out.println (entry.getKey () + " : " + entry.getValue ());
         */

       // System.out.println ("\nrawArguments -");
        boolean bTerminalFontSizeArg = false;
        for (String raw : rawArguments) {
         //   System.out.println(raw);
            if (raw == null || raw.isEmpty() || raw.isBlank())
                continue;
            if (raw != null && !raw.isEmpty() && raw.equals("-fs"))
                bTerminalFontSizeArg = true;
            if (bTerminalFontSizeArg)
                strTerminalFontSize = raw;
        }

        /*
        System.out.println ("\nunnamedParameters -");
        for (String unnamed : unnamedParameters)
            System.out.println (unnamed);
         */
    }

    @Override
    public void start(Stage stage) throws IOException {
        // FXMLLoader fxmlLoader = new FXMLLoader(FlutterProjectVersionApplication.class.getResource("flutterproj-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("flutterproj-view.fxml"));
        // remove xml block from .fxml file: fx:controller="com.metait.javafxplayer.PlayerController"
        controller = new FlutterProjectVersionController();
        fxmlLoader.setController(controller);
        controller.setTerminalFontSize(strTerminalFontSize);
        controller.setPrimaryStage(stage);
        Scene scene = new Scene(fxmlLoader.load(), 920, 540);
        stage.setTitle("Read flutter project directories and search flutter versions");
        stage.setScene(scene);
       // stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }
}
