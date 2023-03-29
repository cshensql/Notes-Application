package presentation

import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType

// Copyright (c) 2023. Andy Yang, Benjamin Du, Charles Shen, Yuying Li

class WarningAlertView(title: String, contentText: String): Alert(AlertType.WARNING) {
    init {
        this.title = title
        this.contentText = contentText
    }

    fun present() {
        this.showAndWait()
    }
}