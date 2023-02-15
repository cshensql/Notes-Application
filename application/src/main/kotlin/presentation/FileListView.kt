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

    // list of dateCreated to help locate the correct note in noteList in model
    private val dateCreatedList = mutableListOf<String>()

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

        this.setOnMouseClicked {
            val (isUnderNoteRoot, pos) = isUnderNoteRoot()
            if (isUnderNoteRoot) {
                val dateCreated = dateCreatedList[pos - 1]
                model.updateSelection(dateCreated)
            } else { // selection is not under "Notes"
                // TODO
            }
        }
    }

    private fun getNoteRootIndex(): Int{
        var retval =  noteRoot.parent.children.indexOf(noteRoot) + 1
        if (groupRoot.isExpanded) {
            retval += groupRoot.children.size
            for (group in groupRoot.children){
                if (group.isExpanded) retval += group.children.size
            }
        }
        return retval
    }

    private fun isUnderNoteRoot(): Pair<Boolean, Int> {
        val noteRootIndex = getNoteRootIndex()
        val selectedIndex = this.selectionModel.selectedIndex
        return Pair(selectedIndex > noteRootIndex, selectedIndex - noteRootIndex)
    }

    override fun updateView() {
        val selectedIndex = this.selectionModel.selectedIndex
        val numOfNotes = noteRoot.children.size

        // remove all current content
        groupRoot.children.clear()
        noteRoot.children.clear()
        dateCreatedList.clear()


        for (entry in model.noteList) {
            val noteItem = TreeItem(entry.value.title)
            noteRoot.children.add(noteItem)
            dateCreatedList.add(entry.key)
        }

        for (entry in model.groupList) {
            val groupItem = TreeItem(entry.name)
            groupRoot.children.add(groupItem)
        }

        val newNumOfNotes = noteRoot.children.size

        // A note is added or deleted in the Notes section
        if (newNumOfNotes != numOfNotes) {
            val newIndex = dateCreatedList.indexOf(model.getCurrSelected().dateCreated)
            if (newIndex < 0) {
                // not able to find currSelected in Model,
                // then select nothing
                this.selectionModel.select(-1)
            } else {
                this.selectionModel.select(newIndex + 1 + getNoteRootIndex())
            }
        } else {
            // TODO: Update selection if note is added or deleted in the Groups area
            this.selectionModel.select(selectedIndex)
        }

        this.refresh()
    }
}