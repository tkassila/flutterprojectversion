package com.metait.flutterprojectversion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirSet {
    private String name;
    private List<File> listBaseDir = new ArrayList<File>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public final List<File> getDirList() {
        return listBaseDir;
    }

    public boolean addDir(File addDir) {
        boolean ret = false;
        if (addDir.isDirectory() && addDir.exists())
        {
            this.listBaseDir.add(addDir);
            ret = true;
        }
        return ret;
    }

    public boolean removeDir(File removeDir) {
        boolean ret = false;
        if (listBaseDir.contains(removeDir))
        {
            this.listBaseDir.remove(removeDir);
            ret = true;
        }
        return ret;
    }

}
