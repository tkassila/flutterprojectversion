# javafxSeekPdfFiles
JavaFx application for search and reading flutter project version files under selected base directory. 
It working with java 11. After selected dir and pressing start button this gui app is showing all sub dirs
of flutter projects and founded flutter project versions. By 1 mouse click selected is copyed into clibborad.
By 2 mouse click on selected list row, it will opening new terminal in current selected directory. This freature
requires that xterm is installed on a linux (apt install xterm).

Added 3 execute type, whcih 2 of are working well, but the third is not working yet.

Default for terminal font size is 14, but it can changed by given app parameter: -fs <number> . That is: -f 10
