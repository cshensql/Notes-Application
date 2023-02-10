package presentation
import business.Model

import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class FileListView(model: Model) : IView, TreeView<String>() {
    private val model = model

    private val groupRoot = TreeItem("Groups")

    private val noteRoot = TreeItem("Notes")

    private val root = TreeItem("Categories")


    init {
        val groupIcon = ImageView(Image("groupIcon.png", 18.0, 18.0, true, true))

        groupRoot.graphic = groupIcon

        val noteIcon = ImageView(Image("noteIcon.png", 18.0, 18.0, true, true))
        noteRoot.graphic = noteIcon

        groupRoot.isExpanded = true
        noteRoot.isExpanded = true

        root.children.addAll(groupRoot, noteRoot)
        this.setRoot(root)
        this.isFocusTraversable = false
    }

    override fun updateView() {
        groupRoot.children.clear()
        noteRoot.children.clear()

        for (entry in model.noteList) {
            val noteItem = TreeItem(entry.value.title)
            noteRoot.children.add(noteItem)
        }

        this.refresh()
    }
}