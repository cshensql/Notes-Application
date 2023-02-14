module application {
    requires kotlin.stdlib;
    requires javafx.controls;
    requires javafx.web;
    requires java.prefs;
    requires kotlinx.coroutines.core.jvm;
    requires shared;
    requires org.jsoup;
    exports net.codebot.application;
}