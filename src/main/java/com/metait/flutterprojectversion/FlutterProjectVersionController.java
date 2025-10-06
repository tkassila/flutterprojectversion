package com.metait.flutterprojectversion;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Provider;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class FlutterProjectVersionController {
    @FXML
    private Label labelMsg;
    @FXML
    private Label labelPath;
    @FXML
    private Button buttonBaseDir;
    @FXML
    private Button buttonStartReadDirs;
    @FXML
    private ListView<FlutterProjVersionData> listViewFlutterProj;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label labelExec;

    private final DirectoryChooser flutterBaseDir = new DirectoryChooser();
    private File dirBase = null;
    private Stage primaryStage;
    private ObservableList<FlutterProjVersionData> dataList = FXCollections.observableArrayList();
    private FlutterVersionService flutterVersionService = new FlutterVersionService();

    protected void appIsClosing()
    {
    }


    private void setLabelMsg(String str)
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                labelMsg.setText(str);
            }
        });
    }

    @FXML
    public void initialize() {
        flutterBaseDir.setTitle("Open flutter base directory to read");
       // listViewFlutterProj.setItems(dataList);
        progressBar.progressProperty().bind(flutterVersionService.progressProperty());
        progressBar.visibleProperty().bind(flutterVersionService.runningProperty());
        listViewFlutterProj.itemsProperty().bind(flutterVersionService.valueProperty());
        labelExec.textProperty().bind(flutterVersionService.messageProperty());

        listViewFlutterProj.setOnMouseClicked(me -> {
            if (me.getButton() == MouseButton.PRIMARY && me.getClickCount() == 2) {
                int newPosition = listViewFlutterProj.getSelectionModel().getSelectedIndex();
                if (newPosition == -1)
                    return;
                System.out.println("New Position is: " + newPosition);

                FlutterProjVersionData item = listViewFlutterProj.getSelectionModel().getSelectedItem();
                if (item == null)
                    return;
                System.out.println("Clicked: " + item);
                    // if (desktop != null)
                    //   desktop.open(new File(item));
                String strCmnFile = (item.getProjVersionFile().getAbsolutePath().contains(" ") ?
                        "'" +item.getProjVersionFile().getParentFile().getAbsolutePath() +"'" :
                        item.getProjVersionFile().getParentFile().getAbsolutePath());

                setLabelMsg("An open terminal has been in dir:: " + strCmnFile);
                    Thread taskThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            double progress = 0;
                            try {
                                ProcessBuilder processBuilder = new ProcessBuilder();
                            // -- Linux --
                            // Run a shell command
                                if (File.separatorChar == '/')
                                    processBuilder.command("xterm", "-hold", "-fa", "'Monospace'", "-fs", "14", "-e", "cd " + strCmnFile +";/bin/bash");
                                else
                                    processBuilder.command("cmd", "/C", "cd " + strCmnFile +";cmd");

                                Process process = processBuilder.start();
                                StringBuilder output = new StringBuilder();
                                BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(process.getInputStream()));

                                String line;
                                while ((line = reader.readLine()) != null) {
                                    output.append(line + "\n");
                                }

                            // process.waitFor(500, TimeUnit.MILLISECONDS);
                    /*
                    int exitVal = process.exitValue();
                    if (exitVal == 0) {
                        System.out.println("Success!");
                        System.out.println(output);
                        setLabelMsg("Opened file: " + item);
                    } else {
                        //abnormal...
                        setLabelMsg("Cannot open: " + item);
                    }
                     */
                        } catch /* (IOException e) {
                    e.printStackTrace();
                    setLabelMsg("Error: " +e.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    setLabelMsg("Error: " +e.getMessage());
                    */
                                (Exception e2) {
                            e2.printStackTrace();
                            setLabelMsg("Error: " +e2.getMessage());
                        }

                    }
                    });

                    taskThread.start();
            } else {
                int newPosition = listViewFlutterProj.getSelectionModel().getSelectedIndex();
                if (newPosition == -1)
                    return;
                System.out.println("New Position is: " + newPosition);

                FlutterProjVersionData item = listViewFlutterProj.getSelectionModel().getSelectedItem();
                if (item == null)
                    return;
                System.out.println("Clicked: " + item);
                try {
                    ClipboardContent content = new ClipboardContent();
                    content.putString(item.toString());
                    // content.putHtml("<b>Bold</b> text");
                    Clipboard.getSystemClipboard().setContent(content);
                    setLabelMsg("Into clipboard: " +item.toString());
                    // if (desktop != null)
                    //   desktop.open(new File(item));
                } catch (Exception e) {
                    e.printStackTrace();
                    setLabelMsg("Error: " +e.getMessage());
                }
            }
        });
    }

    private boolean yesNoDialog(String strYes, String strNo, String strMs, String strTitle,
                                Alert.AlertType alertType)
    {
        boolean ret = false;
        ButtonType yesButton = new ButtonType(strYes, ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType(strNo, ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(alertType, strMs,
                noButton,
                yesButton);
        Button noButtonAlert = (Button) alert.getDialogPane().lookupButton( noButton );
        noButtonAlert.setDefaultButton( true );
        Button yesButtonAlert = (Button) alert.getDialogPane().lookupButton( yesButton );
        yesButtonAlert.setDefaultButton( false );

        alert.setTitle(strTitle);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.orElse(noButton) == yesButton) {
            ret = true;
        }
        return ret;
    }

    public void setPrimaryStage(Stage stage)
    {
        primaryStage = stage;
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                if (flutterVersionService.isRunning())
                {
                    boolean bCloseApp = yesNoDialog("Yes","No",
                            "Should stop executing and close application?",
                            "Reading dirs are in progress",
                            Alert.AlertType.CONFIRMATION);
                    if (!bCloseApp) {
                        t.consume();
                        return;
                    }
                }
                appIsClosing();
                Platform.exit();
                System.exit(0);
            }
        });
    }

    @FXML
    protected void buttonStartReadDirsPressed()
    {
        if (flutterVersionService.isRunning()) {
            labelMsg.setText("Reading dirs is executing. No new start before it has been stopped.");
            return;
        }
        if (dirBase == null || !dirBase.exists())
        {
            labelMsg.setText("Select at first an flutter base dir where reading flutter project files is starting!");
            return;
        }
        double zero = 0.0;
        dataList.clear();
        flutterVersionService.setBaseDir(dirBase);
        Worker.State serviceState = flutterVersionService.getState();
        if (serviceState == Worker.State.READY) {
            labelExec.textProperty().bind(flutterVersionService.messageProperty());
            flutterVersionService.start();
        }
        else
        {
            if (serviceState == Worker.State.SUCCEEDED) {
                labelExec.textProperty().bind(flutterVersionService.messageProperty());
                flutterVersionService.restart();
            }
        }
    }

    @FXML
    protected void buttonBaseDirPressed() {
        labelMsg.setText("Opening base dir dialog");
        if (dirBase != null) {
            flutterBaseDir.setInitialDirectory(dirBase);
        }
        File tmp_dirBase = flutterBaseDir.showDialog(this.primaryStage);
        if (tmp_dirBase != null && tmp_dirBase.exists() && tmp_dirBase.isDirectory()) {
            dirBase = tmp_dirBase;
            labelPath.setText(dirBase.getAbsolutePath());
            labelMsg.setText("Selected base directory");
        }
    }
}
