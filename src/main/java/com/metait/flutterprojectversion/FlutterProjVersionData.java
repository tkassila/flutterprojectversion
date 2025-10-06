package com.metait.flutterprojectversion;

import java.io.File;

public class FlutterProjVersionData {
    public FlutterProjVersionData(String p_strFlutterVersion, File p_projVersionFile,
                                  String p_fvmVersion, String p_strYamlVersion)
    {
        projVersionFile = p_projVersionFile;
        strFlutterVersion = p_strFlutterVersion;
        if (strFlutterVersion == null)
            strFlutterVersion = "";
        fvmVersion = p_fvmVersion;
        if (fvmVersion == null)
            fvmVersion = "";
        strYamlVersion = p_strYamlVersion;
    }

    private String strYamlVersion = "";
    private File projVersionFile = null;
    private String strFlutterVersion = "";
    private String fvmVersion = "";
    public String getYamlVersion() { return strYamlVersion; }
    public String getStrFlutterVersion() { return strFlutterVersion;}
    public String getFvmVersion() { return fvmVersion;}
    public File getProjVersionFile() { return projVersionFile;}

    public String toString() {
        String ret = "";
        if (projVersionFile != null)
            ret = "" +projVersionFile.getAbsolutePath().toString() +" " +strYamlVersion +" " +strFlutterVersion +" " +fvmVersion;
        return ret;
    }
}
