module application {
    requires kotlin.stdlib;
    requires javafx.controls;
    requires javafx.web;
    requires java.prefs;
    requires kotlinx.coroutines.core.jvm;
    requires shared;
    requires org.jsoup;
    requires kotlinx.serialization.core;
    requires kotlinx.serialization.json;
    requires org.controlsfx.controls;
    exports business;
    exports net.codebot.application;
}