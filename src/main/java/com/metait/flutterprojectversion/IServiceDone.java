package com.metait.flutterprojectversion;

import javafx.collections.ObservableList;

public interface IServiceDone {
    public void onServiceSuccess(String msg, ObservableList<FlutterProjVersionData> result);
    public void onServiceSuccess2(String msg, ObservableList<String> result);
}
