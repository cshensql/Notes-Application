package presentation

import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType

class WarningAlertView(title: String, contentText: String): Alert(AlertType.WARNING) {
    init {
        this.title = title
        this.contentText = contentText
    }

    fun present() {
        this.showAndWait()
    }
}