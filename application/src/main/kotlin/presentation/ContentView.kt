package presentation

import business.Model
import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.scene.web.HTMLEditor
import net.codebot.application.ConfigData
import org.controlsfx.control.ToggleSwitch
import org.jsoup.Jsoup


class ContentView(private val model: Model) : IView, VBox() {
    private val toolbar = ToolBar()
    private val saveButton = Button("Save")
    private val toggleSwitch = ToggleSwitch("Lightweight Mode:")

    private val htmlEditor = HTMLEditor()
    private val bottomToolBar = htmlEditor.lookup(".bottom-toolbar")
    private val topToolBar = htmlEditor.lookup(".top-toolbar")

    init {
        this.children.addAll(toolbar, htmlEditor)

        this.isFocusTraversable = false

        htmlEditor.isFocusTraversable = false
        toolbar.isFocusTraversable = false
        toolbar.style = "-fx-spacing: 20px;"

        saveButton.isFocusTraversable = false
        saveButton.graphic = ImageView(Image("save.png", 18.0, 18.0, true, true))

        toggleSwitch.isFocusTraversable = false

        toolbar.items.add(saveButton)
        toolbar.items.add(toggleSwitch)

        saveButton.setOnAction {
            val body = htmlEditor.htmlText
            // use the Jsoup to parse the HTML string into a Document
            val doc = Jsoup.parse(body)
            val title = doc.body().firstElementChild()?.text()
            if (title?.isBlank() == true) {
                WarningAlertView("Empty Title", "The first line can not be blank!")
                    .showAndWait()
            } else {
                // update the lastModified field of the selected note
                model.getCurrSelectedNote()?.updateModified()
                model.changeSelectionContent(
                    title ?: model.getCurrSelectedNote()?.title ?: "",
                    body
                )
            }
        }

        toggleSwitch.selectedProperty().addListener{ observable, oldValue, newValue ->
            changeMode(lightweight = newValue)
        }

        toolbar.prefWidthProperty().bind(this.widthProperty())
        htmlEditor.prefWidthProperty().bind(this.widthProperty())
        htmlEditor.prefHeightProperty().bind(this.heightProperty())
    }

    private fun changeMode(lightweight:Boolean = false){
        // if in lightweight mode: add the foreground color picker to bottomToolBar
        if (lightweight){
            if (bottomToolBar is ToolBar) { // need to add this condition to use bottomToolBar.items
                val nodeToAdd = htmlEditor.lookup(".top-toolbar").lookup(".html-editor-foreground ")
                if (nodeToAdd != null) bottomToolBar.items.add(nodeToAdd)
            }
        }
        // hide or show the topToolBar depending on lightweight
        topToolBar.isVisible = !lightweight
        topToolBar.isManaged = !lightweight

        // toggle display for controls
        ConfigData.invisibleControlsUnderLightweight.forEach {
            toggleControlsDisplay(it, !lightweight)
        }
        // toggle display for separators
        toggleSeparatorDisplay(!lightweight)
    }

    private fun toggleControlsDisplay(target: String, show:Boolean) {
        // check top and bottom toolbars
        val node = htmlEditor.lookup(".bottom-toolbar").lookup(target)
            ?: htmlEditor.lookup(".top-toolbar").lookup(target)
        if (node != null) {
            node.isVisible = show
            node.isManaged = show
        }
    }

    private fun toggleSeparatorDisplay(show: Boolean){
        htmlEditor.lookupAll(".separator").forEach {
            it.isVisible = show
            it.isManaged = show
        }
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