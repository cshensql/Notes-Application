package presentation

import javafx.geometry.Insets
import javafx.scene.control.TextArea
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.FlowPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.web.HTMLEditor

class ContentView(): IView, VBox() {
    private val toolbar = ToolBarView()
    val htmlEditor = HTMLEditor()

    init {
        this.children.addAll(toolbar, htmlEditor)

        this.isFocusTraversable = false

        htmlEditor.isFocusTraversable = false
        toolbar.isFocusTraversable = false


        toolbar.prefWidthProperty().bind(this.widthProperty())
        htmlEditor.prefWidthProperty().bind(this.widthProperty())
        htmlEditor.prefHeightProperty().bind(this.heightProperty())

    }

    override fun updateView() {
        TODO("Not yet implemented")
    }
}