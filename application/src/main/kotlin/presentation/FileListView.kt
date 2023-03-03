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

    private val MAX_CHAR_SHOWN: Int = 15

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
        // position of the current selection relative to noteRoot
        val posToNoteRoot = selectedIndex - getNoteRootIndex()
        // remove all current content
        groupRoot.children.clear()
        noteRoot.children.clear()
        dateCreatedList.clear()

        for (note in model.noteList) {
            var titleShown = note.value.title
            if (titleShown.length > MAX_CHAR_SHOWN) {
                titleShown = titleShown.substring(0, MAX_CHAR_SHOWN)
                titleShown += "..."
            }
            val noteItem = TreeItem(titleShown)
            noteRoot.children.add(noteItem)
            dateCreatedList.add(note.key)
        }

        for (group in model.groupList) {
            var nameShown = group.name
            if (nameShown.length > MAX_CHAR_SHOWN) {
                nameShown = nameShown.substring(0, MAX_CHAR_SHOWN)
                nameShown += "..."
            }
            val groupItem = TreeItem(nameShown)
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
            // Groups/Notes are added under the Groups section
            // fix the selection problem temporarily, need to update this later
            this.selectionModel.select(getNoteRootIndex() + posToNoteRoot)
        }

        this.refresh()
    }
}