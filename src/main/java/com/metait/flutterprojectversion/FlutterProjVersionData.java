package com.metait.flutterprojectversion;

import java.io.File;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FlutterProjVersionData {
    public FlutterProjVersionData(String p_strFlutterVersion, File p_projVersionFile,
                                  String p_fvmVersion, String p_strYamlVersion, String p_projName)
    {
        projVersionFile = p_projVersionFile;
        strFlutterVersion = p_strFlutterVersion;
        if (strFlutterVersion == null)
            strFlutterVersion = "";
        fvmVersion = p_fvmVersion;
        if (fvmVersion == null)
            fvmVersion = "";
        strYamlVersion = p_strYamlVersion;
        if (strYamlVersion == null)
            strYamlVersion = "";
        if (p_projName == null)
            p_projName = "";
        projNameProprety = new SimpleStringProperty(p_projName);
        String fPath = p_projVersionFile.getAbsolutePath();
        if (projVersionFile.isFile())
            fPath = projVersionFile.getParentFile().getAbsolutePath();
        dirProprety = new SimpleStringProperty(fPath);
        restProprety = new SimpleStringProperty(toString());
    }

    public final StringProperty projNameProprety;
    public final StringProperty dirProprety;
    public final StringProperty restProprety;
    private String strYamlVersion = "";
    private File projVersionFile = null;
    private String strFlutterVersion = "";
    private String fvmVersion = "";
    public String getYamlVersion() { return strYamlVersion; }
    public String getStrFlutterVersion() { return strFlutterVersion;}
    public String getFvmVersion() { return fvmVersion;}
    public File getProjVersionFile() { return projVersionFile;}
    public String getProjName() { return projNameProprety.getValue(); }
    public void setProjName(String value) { projNameProprety.setValue(value); }
    public void setDirectory(String value) { dirProprety.setValue(value); }

    public String toString() {
        String ret = "";
        if (projVersionFile != null) {
            String localProjName = projVersionFile.getName();
            if (projVersionFile.isDirectory())
                localProjName = "";
            ret = "" + localProjName + " " + strYamlVersion + " " + strFlutterVersion + " " + fvmVersion;
            ret = ret.trim();
        }
        return ret;
    }
}
