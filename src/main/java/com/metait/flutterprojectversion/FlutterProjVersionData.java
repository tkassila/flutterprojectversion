package com.metait.flutterprojectversion;

import java.io.File;

public class FlutterProjVersionData {
    public FlutterProjVersionData(String p_strFlutterVersion, File p_projVersionFile, String p_fvmVersion)
    {
        projVersionFile = p_projVersionFile;
        strFlutterVersion = p_strFlutterVersion;
        fvmVersion = p_fvmVersion;
    }
    File projVersionFile = null;
    String strFlutterVersion = "";
    String fvmVersion = "";
    public String getStrFlutterVersion() { return strFlutterVersion;}
    public String getFvmVersion() { return fvmVersion;}
    public File getProjVersionFile() { return projVersionFile;}

    public String toString() {
        String ret = "";
        if (projVersionFile != null)
            ret = "" +projVersionFile.getAbsolutePath().toString() +" " +strFlutterVersion +" " +fvmVersion;
        return ret;
    }
}
