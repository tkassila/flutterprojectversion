package com.metait.flutterprojectversion;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import java.io.InputStreamReader;
import java.util.Optional;

public class FlutterProjectVersionController implements IServiceDone {
    @FXML
    private Label labelMsg;
    @FXML
    private Label labelMsg2;
    @FXML
    private Label labelPath;
    @FXML
    private Label labelPath2;
    @FXML
    private Button buttonBaseDir;
    @FXML
    private Button buttonStartReadDirs;
    @FXML
    private ListView<String> listViewDirs;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ProgressBar progressBar2;
    @FXML
    private Label labelExec;
    @FXML
    private Label labelExec2;
    @FXML
    private RadioButton radiobutton1LeveSubDirs;
    @FXML
    private RadioButton radiobutton1LeveSubDirs2;
    @FXML
    private RadioButton radiobuttonOnlyCurrentDir;
    @FXML
    private RadioButton radiobuttonAllSubDirs;
    @FXML
    private RadioButton radiobuttonAllSubDirs2;
    @FXML
    private TextField textFieldMeld;
    @FXML
    private Button buttonMeldCompare;
    @FXML
    private Button buttonMeldCompare2;
    @FXML
    private TableView<FlutterProjVersionData> tableViewData;
    @FXML
    private TableColumn<FlutterProjVersionData, String> projNameColumn;
    @FXML
    private TableColumn<FlutterProjVersionData, String> dirColumn;
    @FXML
    private TableColumn<FlutterProjVersionData, String> restColumn;
    @FXML
    private TextField textFieldFilter;
    @FXML
    private TextField textFieldFilter2;
    @FXML
    private RadioButton radiobuttonAllBaseDirs;
    @FXML
    private RadioButton radiobuttonAllBaseDirs2;
    @FXML
    private Button buttonBaseDir2;
    @FXML
    private Button buttonStartReadDirs2;
    @FXML
    private TextField textFieldMeld2;

    private final DirectoryChooser flutterBaseDir = new DirectoryChooser();
    private final DirectoryChooser flutterBaseDir2 = new DirectoryChooser();
    private File dirBase = null;
    private File dirBase2 = null;
    private Stage primaryStage;
    private ObservableList<FlutterProjVersionData> dataList = FXCollections.observableArrayList();
    private ObservableList<String> dataList2 = FXCollections.observableArrayList();
    private FlutterVersionService flutterVersionService = new FlutterVersionService();
    private MeldDirService meldDirService = new MeldDirService();
    private String strTerminalFontSize;
    private String strMeldExec = "meld.sh";
    private FilteredList<FlutterProjVersionData> filteredData;
    private SortedList<FlutterProjVersionData> sortedData;
    private FilteredList<String> filteredData2;
    private SortedList<String> sortedData2;

    protected void appIsClosing()
    {
    }

    public void onServiceSuccess2(String msg, ObservableList<String> result)
    {
        if (result == null || result.size() == 0)
        {
            setLabelMsg("No data rows!");
            return;
        }
        // static staticFilered = filteredData;
        setLabelMsg2(msg);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                filteredData2 = new FilteredList<>(result, p -> true);
                // 3. Wrap the FilteredList in a SortedList.
                sortedData2 = new SortedList<>(filteredData2);
                // 5. Add sorted (and filtered) data to the table.
                // sortedData2.comparatorProperty().bind(listViewDirs.comparatorProperty());
                listViewDirs.setItems(sortedData2.sorted());
                //  restColumn.resizableProperty().bind(primaryStage.resizableProperty());
                //   GUIUtils.autoFitTable(tableViewData);
                // listViewDirs.sort();
            }
        });
    }


    public void onServiceSuccess(String msg, ObservableList<FlutterProjVersionData> result)
    {
        if (result == null || result.size() == 0)
        {
            setLabelMsg("No data rows!");
            return;
        }
       // static staticFilered = filteredData;
        setLabelMsg(msg);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                filteredData = new FilteredList<>(result, p -> true);
                // 3. Wrap the FilteredList in a SortedList.
               sortedData = new SortedList<>(filteredData);
                // 5. Add sorted (and filtered) data to the table.
                sortedData.comparatorProperty().bind(tableViewData.comparatorProperty());
                tableViewData.setItems(sortedData);
              //  restColumn.resizableProperty().bind(primaryStage.resizableProperty());
             //   GUIUtils.autoFitTable(tableViewData);
                tableViewData.sort();
            }
        });
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

    private void setLabelMsg2(String str)
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                labelMsg2.setText(str);
            }
        });
    }


    @FXML
    protected void buttonMeldComparePressed2()
    {
        int newPosition = listViewDirs.getSelectionModel().getSelectedIndex();
        if (newPosition == -1)
            return;
        System.out.println("New Position is: " + newPosition);

        ObservableList<String> items = listViewDirs.getSelectionModel().getSelectedItems();
        if (items == null) {
            setLabelMsg2("No selected row! Min 2 rows selected at the same time.");
            return;
        }
        if (items.size() < 2) {
            setLabelMsg2("Min 2 rows selected at the same time.");
            return;
        }
        if (items.size() > 3) {
            setLabelMsg2("Max 3 rows can be selected at the same time.");
            return;
        }

        String currentData = null;
        int max = items.size();
        strMeldExec = textFieldMeld2.getText();

        StringBuilder shellCmd = new StringBuilder(strMeldExec + " ");
        String tmp;
        for (int i = 0; i < max; i++)
        {
            currentData = items.get(i);
            if (currentData == null)
                continue;
            shellCmd.append(currentData).append(" ");
        }

        shellCmd = new StringBuilder(shellCmd.toString().trim());
        //  System.out.println("Clicked: " + items);
        // if (desktop != null)
        //   desktop.open(new File(item));
        final String finalShellCmd = shellCmd.toString();

        setLabelMsg2("Opening Meld application in " +max +" directories." );
        Thread taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                double progress = 0;
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder();
                    // -- Linux --
                    // Run a shell command
                    if (File.separatorChar == '/')
                        processBuilder.command("bash", "-c", finalShellCmd);
                    else
                        processBuilder.command("cmd", "/C", finalShellCmd);

                    Process process = processBuilder.start();
                    StringBuilder output = new StringBuilder();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line + "\n");
                    }

                    process.waitFor();
                    int exitVal = process.exitValue();
                    if (exitVal == 0) {
                        System.out.println("Success!");
                        System.out.println(output);
                        setLabelMsg2("Opening meld application: " + max +" dirs.");
                    } else {
                        //abnormal...
                        setLabelMsg2("Cannot open meld!");
                    }
                } catch /* (IOException e) {
                    e.printStackTrace();
                    setLabelMsg("Error: " +e.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    setLabelMsg("Error: " +e.getMessage());
                    */
                (Exception e2) {
                    e2.printStackTrace();
                    setLabelMsg2("Error: " +e2.getMessage());
                }

            }
        });
        taskThread.start();
    }

    @FXML
    protected void buttonMeldComparePressed()
    {
        int newPosition = tableViewData.getSelectionModel().getSelectedIndex();
        if (newPosition == -1)
            return;
        System.out.println("New Position is: " + newPosition);

        ObservableList<FlutterProjVersionData> items = tableViewData.getSelectionModel().getSelectedItems();
        if (items == null) {
            setLabelMsg("No selected row! Min 2 rows selected at the same time.");
            return;
        }
        if (items.size() < 2) {
            setLabelMsg("Min 2 rows selected at the same time.");
            return;
        }
        if (items.size() > 3) {
            setLabelMsg("Max 3 rows can be selected at the same time.");
            return;
        }

        FlutterProjVersionData currentData = null;
        int max = items.size();
        strMeldExec = textFieldMeld.getText();

        StringBuilder shellCmd = new StringBuilder(strMeldExec + " ");
        String tmp;
        for (int i = 0; i < max; i++)
        {
            currentData = items.get(i);
            if (currentData == null)
                continue;
            tmp = getNextDirOf(currentData);
            if (tmp == null)
                continue;
            shellCmd.append(tmp).append(" ");
        }

        shellCmd = new StringBuilder(shellCmd.toString().trim());
        //  System.out.println("Clicked: " + items);
        // if (desktop != null)
        //   desktop.open(new File(item));
        final String finalShellCmd = shellCmd.toString();

        setLabelMsg("Opening Meld application in " +max +" directories." );
        Thread taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                double progress = 0;
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder();
                    // -- Linux --
                    // Run a shell command
                    if (File.separatorChar == '/')
                        processBuilder.command("bash", "-c", finalShellCmd);
                    else
                        processBuilder.command("cmd", "/C", finalShellCmd);

                    Process process = processBuilder.start();
                    StringBuilder output = new StringBuilder();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line + "\n");
                    }

                    process.waitFor();
                    int exitVal = process.exitValue();
                    if (exitVal == 0) {
                        System.out.println("Success!");
                        System.out.println(output);
                        setLabelMsg("Opening meld application: " + max +" dirs.");
                    } else {
                        //abnormal...
                        setLabelMsg("Cannot open meld!");
                    }
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
    }

    @FXML
    public void initialize() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        filteredData = new FilteredList<>(dataList, p -> true);
        filteredData2 = new FilteredList<>(dataList2, p -> true);


        radiobuttonAllBaseDirs.setVisible(false);
        radiobuttonAllBaseDirs2.setVisible(false);

        // 2. Set the filter Predicate whenever the filter changes.
        textFieldFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(data -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (data.projNameProprety.getValue().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (data.dirProprety.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        textFieldFilter2.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData2.setPredicate(data -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (data.toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                }
                return false; // Does not match.
            });
        });

        projNameColumn.setSortable(true);
        dirColumn.setSortable(true);
        projNameColumn.setSortType(TableColumn.SortType.ASCENDING);
        dirColumn.setSortType(TableColumn.SortType.ASCENDING);

        // 3. Wrap the FilteredList in a SortedList.
        sortedData = new SortedList<>(filteredData);
        sortedData2 = new SortedList<>(filteredData2);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tableViewData.comparatorProperty());
        // 0. Initialize the columns.
        projNameColumn.setCellValueFactory(cellData ->
                cellData.getValue().projNameProprety);
        dirColumn.setCellValueFactory(cellData ->
                cellData.getValue().dirProprety);
        restColumn.setCellValueFactory(cellData ->
                cellData.getValue().restProprety);

        tableViewData.getSortOrder().addAll(projNameColumn, dirColumn, restColumn);
        // 5. Add sorted (and filtered) data to the table.
        tableViewData.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableViewData.setItems(sortedData);

        textFieldMeld.setText(strMeldExec);
        textFieldMeld2.setText(strMeldExec);
        flutterBaseDir.setTitle("Open flutter base directory to read");
        flutterBaseDir2.setTitle("Open base directory to read");
        // listViewFlutterProj.setItems(dataList);
        progressBar.progressProperty().bind(flutterVersionService.progressProperty());
        progressBar.visibleProperty().bind(flutterVersionService.runningProperty());
        progressBar2.progressProperty().bind(meldDirService.progressProperty());
        progressBar2.visibleProperty().bind(meldDirService.runningProperty());
      //  listViewFlutterProj.itemsProperty().bind(flutterVersionService.valueProperty());
       // old bind, noew more used: tableViewData.itemsProperty().bind(flutterVersionService.valueProperty());
        labelExec.textProperty().bind(flutterVersionService.messageProperty());
        labelExec2.textProperty().bind(meldDirService.messageProperty());
        // listViewFlutterProj.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableViewData.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listViewDirs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        projNameColumn.prefWidthProperty().bind(tableViewData.widthProperty().multiply(0.1));
        dirColumn.prefWidthProperty().bind(tableViewData.widthProperty().multiply(0.3));
        restColumn.prefWidthProperty().bind(tableViewData.widthProperty().multiply(1.0));

        // listViewFlutterProj.set
        dirColumn.setCellFactory(column -> {
            return new TableCell<FlutterProjVersionData, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle(null);
                    } else {
                        setText(item);
                        FlutterProjVersionData dataRow = getTableRow().getItem();
                        boolean bRedStyle = (dataRow.getStrFlutterVersion().isBlank()
                                || dataRow.getStrFlutterVersion().isEmpty())
                                && (dataRow.getFvmVersion().isEmpty() || dataRow.getFvmVersion().isBlank());
                        String newStyle2 = bRedStyle ? "-fx-background-color: red;" : "-list-cell;";
                        if (!bRedStyle)
                            newStyle2 = dataRow.getYamlVersion().isEmpty() ? "-fx-background-color: orange;" : newStyle2;
                        final String strStyle = newStyle2;
                        setStyle(strStyle);
                    }
                }
            };
        });

        tableViewData.setOnMouseClicked(me -> {
            if (me.getButton() == MouseButton.PRIMARY && me.getClickCount() == 2) {
                int newPosition = tableViewData.getSelectionModel().getSelectedIndex();
                if (newPosition == -1)
                    return;
                System.out.println("New Position is: " + newPosition);

                FlutterProjVersionData item = tableViewData.getSelectionModel().getSelectedItem();
                if (item == null)
                    return;
                System.out.println("Clicked: " + item);
                // if (desktop != null)
                //   desktop.open(new File(item));
                String strCmnFile2 = (item.getProjVersionFile().getAbsolutePath().contains(" ") ?
                        "'" + item.getProjVersionFile().getParentFile().getAbsolutePath() + "'" :
                        item.getProjVersionFile().getParentFile().getAbsolutePath());
                if (item.getFvmVersion().isEmpty() && item.getStrFlutterVersion().isEmpty())
                    strCmnFile2 = (item.getProjVersionFile().getAbsolutePath().contains(" ") ?
                            "'" + item.getProjVersionFile().getAbsolutePath() + "'" :
                            item.getProjVersionFile().getAbsolutePath());
                final String strCmnFile = strCmnFile2;

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
                                processBuilder.command("xterm", "-hold", "-fa", "'Monospace'", "-fs", strTerminalFontSize, "-e", "cd " + strCmnFile + ";/bin/bash");
                            else
                                processBuilder.command("cmd", "/C", "cd " + strCmnFile + ";cmd");

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
                            setLabelMsg("Error: " + e2.getMessage());
                        }

                    }
                });

                taskThread.start();
            } else if (me.getButton() == MouseButton.PRIMARY && me.getClickCount() == 1) {
                int newPosition = tableViewData.getSelectionModel().getSelectedIndex();
                if (newPosition == -1)
                    return;
                System.out.println("New Position is: " + newPosition);

                FlutterProjVersionData item = tableViewData.getSelectionModel().getSelectedItem();
                if (item == null)
                    return;
                System.out.println("Clicked: " + item);
                try {
                    ClipboardContent content = new ClipboardContent();
                    content.putString(item.toString());
                    // content.putHtml("<b>Bold</b> text");
                    Clipboard.getSystemClipboard().setContent(content);
                    //   listViewFlutterProj.refresh();
                    //    setLabelMsg("Into clipboard: " +item.toString());
                    // if (desktop != null)
                    //   desktop.open(new File(item));
                } catch (Exception e) {
                    e.printStackTrace();
                    setLabelMsg("Error: " + e.getMessage());
                }
            }
            /*
            else
            if (me.getButton() == MouseButton.SECONDARY && me.getClickCount() == 1)
            {
            }
            */
        });

        listViewDirs.setOnMouseClicked(me -> {
            if (me.getButton() == MouseButton.PRIMARY && me.getClickCount() == 2) {
                int newPosition = listViewDirs.getSelectionModel().getSelectedIndex();
                if (newPosition == -1)
                    return;
                System.out.println("New Position is: " + newPosition);

                String item = listViewDirs.getSelectionModel().getSelectedItem();
                if (item == null)
                    return;
                System.out.println("Clicked: " + item);
                // if (desktop != null)
                //   desktop.open(new File(item));
                final String strCmnFile = item;

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
                                processBuilder.command("xterm", "-hold", "-fa", "'Monospace'", "-fs", strTerminalFontSize, "-e", "cd " + strCmnFile + ";/bin/bash");
                            else
                                processBuilder.command("cmd", "/C", "cd " + strCmnFile + ";cmd");

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
                            setLabelMsg("Error: " + e2.getMessage());
                        }

                    }
                });

                taskThread.start();
            } else if (me.getButton() == MouseButton.PRIMARY && me.getClickCount() == 1) {
                int newPosition = listViewDirs.getSelectionModel().getSelectedIndex();
                if (newPosition == -1)
                    return;
                System.out.println("New Position is: " + newPosition);

                String item = listViewDirs.getSelectionModel().getSelectedItem();
                if (item == null)
                    return;
                System.out.println("Clicked: " + item);
                try {
                    ClipboardContent content = new ClipboardContent();
                    content.putString(item);
                    // content.putHtml("<b>Bold</b> text");
                    Clipboard.getSystemClipboard().setContent(content);
                    //   listViewFlutterProj.refresh();
                    //    setLabelMsg("Into clipboard: " +item.toString());
                    // if (desktop != null)
                    //   desktop.open(new File(item));
                } catch (Exception e) {
                    e.printStackTrace();
                    setLabelMsg("Error: " + e.getMessage());
                }
            }
            /*
            else
            if (me.getButton() == MouseButton.SECONDARY && me.getClickCount() == 1)
            {
            }
            */
        });
    }

    private String getNextDirOf(FlutterProjVersionData item)
    {
        String strCmnFile2 = (item.getProjVersionFile().getAbsolutePath().contains(" ") ?
                "'" +item.getProjVersionFile().getParentFile().getAbsolutePath() +"'" :
                item.getProjVersionFile().getParentFile().getAbsolutePath());
        if (item.getFvmVersion().isEmpty() && item.getStrFlutterVersion().isEmpty())
            strCmnFile2 = (item.getProjVersionFile().getAbsolutePath().contains(" ") ?
                    "'" +item.getProjVersionFile().getAbsolutePath() +"'" :
                    item.getProjVersionFile().getAbsolutePath());
        final String strCmnFile = strCmnFile2;
        return strCmnFile;
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

    public void setTerminalFontSize(String p_fontSize)
    {
        strTerminalFontSize = p_fontSize;
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
    protected void buttonStartReadDirsPressed2() {
        if (meldDirService.isRunning()) {
            setLabelMsg2("Reading dirs is executing. No new start before it has been stopped.");
            return;
        }
        if (dirBase2 == null || !dirBase2.exists())
        {
            setLabelMsg2("Select at first a base dir where reading files is starting!");
            return;
        }
        double zero = 0.0;
        dataList2.clear();
        meldDirService.setBaseDir(dirBase2);
        // radiobutton1LeveSubDirs is selected: normal executing
        if (radiobutton1LeveSubDirs2.isSelected())
            meldDirService.setExecutetype(MeldDirService.EXECUTETYPE.ONELEVELSUBDIR);
        else
        if (radiobuttonAllSubDirs2.isSelected())
            meldDirService.setExecutetype(MeldDirService.EXECUTETYPE.ALLSUBDIRS);

        meldDirService.setCallbackInstance(this);
        Worker.State serviceState = meldDirService.getState();
        if (serviceState == Worker.State.READY) {
            labelExec2.textProperty().bind(meldDirService.messageProperty());
            meldDirService.start();
        }
        else
        {
            if (serviceState == Worker.State.SUCCEEDED) {
                labelExec2.textProperty().bind(meldDirService.messageProperty());
                meldDirService.restart();
            }
        }
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
        // radiobutton1LeveSubDirs is selected: normal executing
        if (radiobutton1LeveSubDirs.isSelected())
            flutterVersionService.setExecutetype(FlutterVersionService.EXECUTETYPE.ONELEVELSUBDIR);
        else
        if (radiobuttonOnlyCurrentDir.isSelected())
            flutterVersionService.setExecutetype(FlutterVersionService.EXECUTETYPE.ONLYCURRENTSELECTEDDIR);
        else
        if (radiobuttonAllSubDirs.isSelected())
            flutterVersionService.setExecutetype(FlutterVersionService.EXECUTETYPE.ALLSUBDIRS);

        flutterVersionService.setCallbackInstance(this);
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

    @FXML
    protected void buttonBaseDirPressed2() {
        labelMsg2.setText("Opening base dir dialog 2");
        if (dirBase2 != null) {
            flutterBaseDir2.setInitialDirectory(dirBase2);
        }
        File tmp_dirBase = flutterBaseDir2.showDialog(this.primaryStage);
        if (tmp_dirBase != null && tmp_dirBase.exists() && tmp_dirBase.isDirectory()) {
            dirBase2 = tmp_dirBase;
            labelPath2.setText(dirBase2.getAbsolutePath());
            labelMsg2.setText("Selected base directory");
        }
    }
}
