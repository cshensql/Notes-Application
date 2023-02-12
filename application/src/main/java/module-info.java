module application {
    requires kotlin.stdlib;
    requires javafx.controls;
    requires javafx.web;
    requires java.prefs;
    requires kotlinx.coroutines.core.jvm;
    requires shared;
    exports net.codebot.application;
}