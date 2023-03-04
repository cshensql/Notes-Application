package presentation

import business.Model
import javafx.beans.binding.Bindings
import javafx.scene.control.*

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import java.util.*

class FileListView(model: Model) : IView, TreeView<String>() {
    private val model = model

    private val groupRoot = TreeItem("Groups")

    private val noteRoot = TreeItem("Notes")

    private val root = TreeItem("Categories")

    private val lockNoteView = LockNoteView()

    // list of dateCreated to help locate the correct note in noteList in model
    private val dateCreatedList = mutableListOf<String>()

    private val MAX_CHAR_SHOWN: Int = 15

    init {
        setupCategories()
        setupClickAction()
        setupContextMenuForTreeItem()


    }

    private fun setupContextMenuForTreeItem() {
        val lockNoteItem = MenuItem("Lock Note")
        lockNoteItem.setOnAction {
            val alert = Alert(Alert.AlertType.CONFIRMATION)
            val dialogPane = alert.dialogPane

            dialogPane.content = lockNoteView
            alert.title = "Lock Note"
            alert.isResizable = true
            alert.width = 500.0
            alert.height = 600.0

            while (!checkValidityofPassword(alert.showAndWait())) {
                // repeat this process
            }
        }

        val contextMenu = ContextMenu(lockNoteItem)
        this.contextMenu = contextMenu
        this.setOnContextMenuRequested {
            if (this.selectionModel.selectedIndex >= 0 && this.selectionModel.selectedItem.parent == groupRoot) {
                this.contextMenu.hide()
            }
        }
    }

    private fun checkValidityofPassword(result: Optional<ButtonType>): Boolean {
        if (result.isPresent && result.get() == ButtonType.OK) {
            val password = lockNoteView.getPassword()
            val verifiedPassword = lockNoteView.getVerifiedPassword()
            val passwordHint = lockNoteView.getPasswordHint()
            if (password.isNullOrBlank() || password.isEmpty() || verifiedPassword.isNullOrBlank() || verifiedPassword.isEmpty()) {
                showEmptyPasswordWarning()
                lockNoteView.clearInputPassword()
                return false
            }
        }
        return true
    }
    private fun showEmptyPasswordWarning() {
        val alert = Alert(Alert.AlertType.WARNING)
        alert.title = "Blank Password"
        alert.contentText = "You entered blank / empty string for either 'Password' or 'Verify'. Please enter again"
        alert.showAndWait()
    }


    private fun setupClickAction() {
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

    private fun setupCategories() {
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