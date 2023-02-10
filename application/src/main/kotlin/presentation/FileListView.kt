package presentation

import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class FileListView() : IView, TreeView<String>() {

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
        val mockGroup1 = TreeItem("Mock Group 1")
        val mockGroup2 = TreeItem("Mock Group 2")
        groupRoot.children.addAll(mockGroup1, mockGroup2)

        root.children.addAll(groupRoot, noteRoot)
        this.setRoot(root)
        this.isFocusTraversable = false
    }

    override fun updateView() {
        TODO("Not yet implemented")
    }
}