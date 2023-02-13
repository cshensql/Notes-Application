package presentation

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.ToolBar
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.FlowPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.web.HTMLEditor

class ContentView(): IView, VBox() {
    private val toolbar = ToolBar()
    private val saveButton = Button("Save")
    private val htmlEditor = HTMLEditor()

    init {
        this.children.addAll(toolbar, htmlEditor)

        this.isFocusTraversable = false

        htmlEditor.isFocusTraversable = false
        toolbar.isFocusTraversable = false

        saveButton.isFocusTraversable = false
        saveButton.graphic = ImageView(Image("save.png", 18.0, 18.0, true, true))

        toolbar.items.add(saveButton)

        saveButton.setOnAction {
            // Need to write code to get and update note content
            // notify model to update related properties
            println("Clicked")
        }


        toolbar.prefWidthProperty().bind(this.widthProperty())
        htmlEditor.prefWidthProperty().bind(this.widthProperty())
        htmlEditor.prefHeightProperty().bind(this.heightProperty())

    }

    override fun updateView() {
        TODO("Not yet implemented")
    }
}