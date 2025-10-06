package com.metait.flutterprojectversion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlutterVersionService extends Service<ObservableList<FlutterProjVersionData>> {

    public enum EXECUTETYPE {
        ONELEVELSUBDIR, ONLYCURRENTSELECTEDDIR, ALLSUBDIRS
    }

    private EXECUTETYPE executetype = EXECUTETYPE.ONELEVELSUBDIR;

    public void setExecutetype(EXECUTETYPE p_executeType){ executetype = p_executeType; }

    File baseDir;
    StringBuffer sbNoFlutterProjDatas = new StringBuffer();
    public void setBaseDir(File p_baseDir){
        baseDir = p_baseDir;
    }
    public String getNoFlutterProjDatas(){ return sbNoFlutterProjDatas.toString(); }

    protected Task createTask() {
        return new Task<ObservableList<FlutterProjVersionData>>() {
            protected ObservableList<FlutterProjVersionData> call() throws Exception {
                updateMessage("Reading flutter directories");
                sbNoFlutterProjDatas = new StringBuffer();
                ObservableList<FlutterProjVersionData> result = FXCollections.observableArrayList();
                List<FlutterProjVersionData> resultSubDirs;

                if (baseDir != null && baseDir.exists() && baseDir.isDirectory())
                {
                    File [] files = baseDir.listFiles();
                    if (files == null || files.length == 0)
                    {
                        updateMessage("No flutter directories");
                        return result;
                    }

                    File current;
                    FlutterProjVersionData data;

                    if (executetype == EXECUTETYPE.ONLYCURRENTSELECTEDDIR) {
                        current = baseDir;
                        if (current == null || !current.exists() || current.isFile() || !current.canRead())
                            return null;
                        data = getFlutterProjVersionNewDataOf(0, current); // new FlutterProjVersionData("" +k, current)
                        if (data == null) {
                            sbNoFlutterProjDatas.append(current.getAbsolutePath() + "\n");
                            updateMessage("Selected directory is no flutter directory!");
                            return null;
                        }
                        result.add(data);
                        return result;
                    }

                    List<File> listDirs = new ArrayList<>();
                    final int max = files.length;
                    for (int i = 0; i < max; i++)
                    {
                        // Thread.sleep(4000);
                        current = files[i];
                        if (current == null || !current.exists() || current.isFile() || !current.canRead())
                            continue;
                        // progressProperty().add(10);
                        listDirs.add(current);
                    }

                    int max2 = listDirs.size();
                    int maxProgress = 100;
                    double dOneItem = maxProgress / max2;
                    double workDone = 0;
                    // progressProperty().divide(max2);
                    for (int k = 0; k < max2; k++)
                    {
                        // Thread.sleep(1000);
                        current = listDirs.get(k);
                        if (current == null || !current.exists() || current.isFile() || !current.canRead())
                            continue;
                      //  result.add(new FlutterProjVersionData("" +k, current));
                        data = getFlutterProjVersionNewDataOf(k, current); // new FlutterProjVersionData("" +k, current)
                        if (executetype == EXECUTETYPE.ALLSUBDIRS)
                        {
                            resultSubDirs = getResultListForFlutterProjVersionOf(k, current);
                            if (resultSubDirs != null && !resultSubDirs.isEmpty())
                                result.addAll(resultSubDirs);
                        }

                        if (data == null) {
                            sbNoFlutterProjDatas.append(current.getAbsolutePath()+"\n");
                            continue;
                        }
                        result.add(data);
                        workDone += dOneItem;
                        updateProgress(workDone, maxProgress);
                        /*
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                updateValue(result);
                            }
                        });
                         */
                    }
                    updateMessage("Read all flutter directories");
                }
                else
                    updateMessage("Flutter base directory not selected");

                return result;
            }

            protected List<FlutterProjVersionData> getResultListForFlutterProjVersionOf(int k, File currentDir)
            {
                List<FlutterProjVersionData> result = null;
                File [] files = currentDir.listFiles();
                File current;
                List<FlutterProjVersionData> resultSubDirs;

                if (files == null || files.length == 0)
                {
                    return result;
                }

                result = new ArrayList<FlutterProjVersionData>();

                int max2 = files.length;
                FlutterProjVersionData data;

                for (int i = 0; i < max2; i++) {
                    // Thread.sleep(1000);
                    current = files[i];
                    if (current == null || !current.exists() || current.isFile() || !current.canRead())
                        continue;
                    //  result.add(new FlutterProjVersionData("" +k, current));
                    data = getFlutterProjVersionNewDataOf(k, current); // new FlutterProjVersionData("" +k, current)
                    if (executetype == EXECUTETYPE.ALLSUBDIRS) {
                        resultSubDirs = getResultListForFlutterProjVersionOf(k, current);
                        if (resultSubDirs != null && !resultSubDirs.isEmpty())
                            result.addAll(resultSubDirs);
                    }

                    if (data == null) {
                        sbNoFlutterProjDatas.append(current.getAbsolutePath() + "\n");
                        continue;
                    }

                    if (!data.getStrFlutterVersion().isEmpty() && !data.getFvmVersion().isEmpty())
                        result.add(data);
                }
                return result;
            }

            protected FlutterProjVersionData getFlutterProjVersionNewDataOf(int k, File flutterProjDir)
            {
                FlutterProjVersionData ret = null;
                FlutterProjVersionData yamData = null;
                if (flutterProjDir == null || flutterProjDir.isFile())
                    return ret;
                try {
                    File [] files = flutterProjDir.listFiles();
                    File file;
                    if (files == null)
                        return ret;

                    final String cnstFlutterProjVersionFileName = "pubspec.lock";
                    final String cnstFlutterProjVersionFileNameYAML = "pubspec.yaml";
                    final String cnstFvmFileName = ".fvm";
                    final String cnstDartToolFileName = ".dart_tool";
                    int max = files.length;
                    String fvmVersion = "", fvmVersion1 = "", fvmVersion2 = "";
                    for (int i = 0; i < max; i++)
                    {
                        file = files[i];
                        if (file == null || file.isFile())
                            continue;
                        if (file.getName().equals(cnstFvmFileName)) {
                            String tmp_fvmVersion = getFVMVersion(k, file);
                            if (tmp_fvmVersion != null && !tmp_fvmVersion.isEmpty())
                                fvmVersion2 = ".fvm: " +tmp_fvmVersion;
                        }
                        else
                        if (fvmVersion.equals("") && file.getName().equals(cnstDartToolFileName)) {
                            String tmp_fvmVersion = getDartToolVersion(k, file);
                            if (tmp_fvmVersion != null && !tmp_fvmVersion.isEmpty())
                                fvmVersion1 = ".dart_tool: " +tmp_fvmVersion;
                        }
                    }

                    fvmVersion = fvmVersion1 +" " +fvmVersion2;

                    for (int ki = 0; ki < max; ki++)
                    {
                        file = files[ki];
                        if (file == null || file.isDirectory())
                            continue;
                        if (file.getName().equals(cnstFlutterProjVersionFileNameYAML)) {
                            yamData = getFlutterVersionDataYAML(k, file, fvmVersion, "");
                            break;
                        }
                    }

                    for (int ki = 0; ki < max; ki++)
                    {
                        file = files[ki];
                        if (file == null || file.isDirectory())
                            continue;
                        if (file.getName().equals(cnstFlutterProjVersionFileName))
                            return getFlutterVersionData(k, file, fvmVersion,
                                    yamData != null ? yamData.getYamlVersion() : "");
                    }

                    if (fvmVersion != null && !fvmVersion.isEmpty())
                    {
                        if (yamData != null)
                            return yamData;

                        for (int ki = 0; ki < max; ki++)
                        {
                            file = files[ki];
                            if (file == null || file.isDirectory())
                                continue;
                            if (file.getName().equals(cnstFlutterProjVersionFileNameYAML))
                                return getFlutterVersionDataYAML(k, file, fvmVersion,
                                        yamData != null ? yamData.getYamlVersion() : "");
                        }

                        return new FlutterProjVersionData("", flutterProjDir, fvmVersion,
                                yamData != null ? yamData.getYamlVersion() : "");
                        // getFlutterVersionData(k, flutterProjDir, fvmVersion);
                    }
                    else
                        return new FlutterProjVersionData("", flutterProjDir, "",
                                yamData != null ? yamData.getYamlVersion() : "");

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

               // return ret;
            }

            protected String getDartToolVersion(int k, File fvmDir)
                    throws IOException
            {
                if (fvmDir == null || fvmDir.isFile())
                    return "";
                File [] files = fvmDir.listFiles();
                if (files == null || files.length == 0)
                    return "";
                File file;
                int max = files.length;
                for (int i = 0; i < max; i++)
                {
                    file = files[i];
                    if (file == null || file.isDirectory())
                        continue;
                    if (file.getName().equals("version"))
                        return Files.readString(Path.of(file.getAbsolutePath()));
                }

                return "";
            }

            protected String getFVMVersion(int k, File fvmDir)
                    throws IOException
            {
                if (fvmDir == null || fvmDir.isFile())
                    return "";
                File [] files = fvmDir.listFiles();
                if (files == null || files.length == 0)
                    return "";
                File file;
                int max = files.length;
                for (int i = 0; i < max; i++)
                {
                    file = files[i];
                    if (file == null || file.isDirectory())
                        continue;
                    if (file.getName().equals("version"))
                        return Files.readString(Path.of(file.getAbsolutePath()));
                }
                return "";
            }

            protected FlutterProjVersionData getFlutterVersionDataYAML(int k, File fileFlutter,
                                                                       String fvmVersion, String yamVersion)
            {
                FlutterProjVersionData ret = null;
                try {
                    String fileContent = Files.readString(Path.of(fileFlutter.getAbsolutePath()));
                    String strYamlVersion = getFlutterVersionStringYAML(fileContent);
                    ret = new FlutterProjVersionData("", fileFlutter, fvmVersion, strYamlVersion);
                } catch (IOException e) {
                    // handle exception in i/o
                    return null;
                }
                return ret;
            }

            protected FlutterProjVersionData getFlutterVersionData(int k, File fileFlutter,
                                                                   String fvmVersion, String yamlVersion)
            {
                FlutterProjVersionData ret = null;
                try {
                    String fileContent = Files.readString(Path.of(fileFlutter.getAbsolutePath()));
                    String strVersion = getFlutterVersionString(fileContent);
                    ret = new FlutterProjVersionData(strVersion, fileFlutter, fvmVersion, yamlVersion);
                } catch (IOException e) {
                    // handle exception in i/o
                    return null;
                }
                return ret;
            }

            protected String getFlutterVersionStringYAML
                    (String fileContent)
            {
                String ret = "";
                String name = "";
                Pattern namePattern = Pattern.compile(
                        "name:\\s*(.*?)\n",
                        Pattern.MULTILINE
                );

                Matcher matcherName = namePattern.matcher(fileContent);
                if (matcherName.find())
                    name = matcherName.group(1);

                Pattern versionPattern = Pattern.compile(
                        "environment:\n\\s*sdk:\\s*'(.*?)'\n",
                        Pattern.MULTILINE
                );
                Matcher matcher = versionPattern.matcher(fileContent);
                if (matcher.find()) {
                    String strVersionFlutter = matcher.group(1);
                    return " project name: " +name +" yaml environment sdk: " +strVersionFlutter ;
                }
                return ret;
            }

            protected String getFlutterVersionString(String fileContent)
            {
                String ret = null;
                Pattern versionPattern = Pattern.compile(
                        "sdks:\n\\s*dart:\\s*\"(.*?)\"\n\\s*flutter:\\s*\"(.*?)\"",
                        Pattern.MULTILINE
                );
                Matcher matcher = versionPattern.matcher(fileContent);
                if (matcher.find()) {
                    String strVersionFlutter = matcher.group(1);
                    String strVersionDart = matcher.group(2);
                    return "flutter: " +strVersionFlutter +" dart: " +strVersionDart;
                }
                return ret;
            }
        };
    }
}
