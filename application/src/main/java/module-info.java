module application {
    requires kotlin.stdlib;
    requires javafx.controls;
    requires javafx.web;
    requires java.prefs;
    requires kotlinx.coroutines.core.jvm;
    requires shared;
    requires org.jsoup;
    requires org.controlsfx.controls;
    exports net.codebot.application;
}