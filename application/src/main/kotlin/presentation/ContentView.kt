package presentation

import business.Model
import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.scene.web.HTMLEditor
import org.jsoup.Jsoup

class ContentView(private val model: Model) : IView, VBox() {
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
            val body = htmlEditor.htmlText
            // use the Jsoup to parse the HTML string into a Document
            val doc = Jsoup.parse(body)
            val title = doc.body().firstElementChild()?.text()
            // update the lastModified field of the selected note
            model.getCurrSelectedNote()?.updateModified()
            model.changeSelectionContent(
                title ?: model.getCurrSelectedNote()?.title ?: "",
                body
            )
        }


        toolbar.prefWidthProperty().bind(this.widthProperty())
        htmlEditor.prefWidthProperty().bind(this.widthProperty())
        htmlEditor.prefHeightProperty().bind(this.heightProperty())
    }

    override fun updateView() {
        val currSelectedNote = model.getCurrSelectedNote()
        if (currSelectedNote != null) {
            if (currSelectedNote.isLocked) {
                htmlEditor.isDisable = true
                htmlEditor.htmlText = "This note is locked. Please unlock it first by right-clicking this note in the file list or unlocking the note from the menubar."
            } else  {
                htmlEditor.isDisable = false
                htmlEditor.htmlText = model.getCurrSelectedNote()?.body ?: ""
            }
        } else {
            htmlEditor.htmlText = ""
            htmlEditor.isDisable = true
        }
    }
}