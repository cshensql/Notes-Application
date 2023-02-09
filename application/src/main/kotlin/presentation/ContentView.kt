package presentation

import javafx.scene.control.TextArea
import javafx.scene.layout.FlowPane

class ContentView(): IView, FlowPane() {
    private val toolbar = ToolBarView()

    init {
        val tempTextArea = TextArea()

        this.children.add(toolbar)
        this.children.add(tempTextArea)

        toolbar.prefWidthProperty().bind(this.widthProperty())
        tempTextArea.prefWidthProperty().bind(this.widthProperty())
        tempTextArea.prefHeightProperty().bind(this.heightProperty())
        tempTextArea.isFocusTraversable = false

    }

    override fun updateView() {
        TODO("Not yet implemented")
    }
}