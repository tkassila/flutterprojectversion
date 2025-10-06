#! /usr/bin/bash
# echo !/bin/bash

export JAVA_HOME=/home/tk/.sdkman/candidates/java/11.0.21.fx-librca
export PATH_TO_FX=$JAVA_HOME/lib
# java -version
export FXSCALE="-Dprism.allowhidpi=true -Dsun.java2d.uiScale.enabled=true -Dsun.java2d.uiScale=2.0"
echo FXSCALE=$FXSCALE
sdk use java 11.0.25.fx-librca
java -version
# exit 0
echo java -Dsun.java2d.uiScale.enabled=true -Dsun.java2d.win.uiScaleX=2.5 -Dsun.java2d.win.uiScaleY=2.5 -cp ./flutterprojectversion.jar:./libs/pdfbox-3.0.0.jar:./libs/spire.pdf.free-9.13.0.jar::$CLASSPATH com.metait.flutterprojectversion.FlutterProjectVersionApplication $*
DISPLAY=:0 java -Dsun.java2d.uiScale.enabled=true -Dsun.java2d.win.uiScaleX=2.5 -Dsun.java2d.win.uiScaleY=2.5 -cp ./flutterprojectversion.jar:$CLASSPATH com.metait.flutterprojectversion.FlutterProjectVersionApplication $*

