package com.metait.flutterprojectversion;

import java.util.ArrayList;
import java.util.List;

public class DirSets {
    private List<DirSet> dirSetList = new ArrayList<>();

    public final List<DirSet> getDirSetList() {
        return dirSetList;
    }

    public void setDirSetList(List<DirSet> dirSetList) {
        this.dirSetList = dirSetList;
    }

    public boolean removeDirSet(DirSet dirSet)
    {
        return this.dirSetList.remove(dirSet);
    }

    public boolean addDirSet(DirSet dirSet)
    {
        return this.dirSetList.add(dirSet);
    }

}
