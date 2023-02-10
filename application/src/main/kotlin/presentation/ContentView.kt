package presentation

import javafx.scene.control.TextArea
import javafx.scene.layout.FlowPane
import javafx.scene.web.HTMLEditor

class ContentView(): IView, FlowPane() {
    private val toolbar = ToolBarView()
    private val htmlEditor = HTMLEditor()

    init {
        this.children.add(toolbar)
        this.children.add(htmlEditor)


        htmlEditor.isFocusTraversable = false
        toolbar.isFocusTraversable = false

        toolbar.prefWidthProperty().bind(this.widthProperty())

    }

    override fun updateView() {
        TODO("Not yet implemented")
    }
}