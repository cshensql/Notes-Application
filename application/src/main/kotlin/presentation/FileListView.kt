package presentation

import business.Model
import javafx.beans.binding.Bindings
import javafx.geometry.Pos
import javafx.scene.control.*

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.scene.text.Text
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
        val unlockNoteItem = MenuItem("Unlock Note")
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

        unlockNoteItem.setOnAction {
            val (isUnderNoteRoot, pos) = isUnderNoteRoot()
            val dateCreated = dateCreatedList[pos - 1]
            val passwordHint = model.noteList[dateCreated]?.passwordHint ?: ""
            val textPrompt = TextInputDialog()
            val inputView = VBox()
            val inputField = PasswordField()
            inputView.children.add(inputField)
            inputView.alignment = Pos.CENTER_LEFT
            inputView.spacing = 10.0
            if (passwordHint.isNotBlank() && passwordHint.isNotEmpty()) {
                inputView.children.add(Text("Hint: " + passwordHint))
            }
            textPrompt.headerText = "Please enter the password"
            textPrompt.dialogPane.content = inputView
            textPrompt.showAndWait()

        }



        val contextMenu = ContextMenu(lockNoteItem)
        this.contextMenu = contextMenu
        this.setOnContextMenuRequested {
            if (this.selectionModel.selectedIndex >= 0 && this.selectionModel.selectedItem.parent == groupRoot) {
                this.contextMenu.hide()
            } else if (this.selectionModel.selectedIndex >= 0 && this.selectionModel.selectedItem.parent == noteRoot) {
                val (isUnderNoteRoot, pos) = isUnderNoteRoot()
                val dateCreated = dateCreatedList[pos - 1]
                val noteIsLocked = model.noteList[dateCreated]?.isLocked ?: false
                contextMenu.items.clear()
                if (noteIsLocked) {
                    contextMenu.items.add(unlockNoteItem)
                } else {
                    contextMenu.items.add(lockNoteItem)
                }
            }
        }
    }

    private fun checkValidityofPassword(result: Optional<ButtonType>): Boolean {
        if (result.isPresent && result.get() == ButtonType.OK) {
            val password = lockNoteView.getPassword()
            val verifiedPassword = lockNoteView.getVerifiedPassword()
            val passwordHint = lockNoteView.getPasswordHint()
            if (password.isNullOrBlank() || password.isEmpty() || verifiedPassword.isNullOrBlank() || verifiedPassword.isEmpty()) {
                showWarningForUnacceptablePassword("Blank or Empty Passwords",
                    "You entered blank or empty string for 'Password' or 'Verify'. Please enter again")
                lockNoteView.clearInputPassword()
                return false
            } else if (password != verifiedPassword) {
                showWarningForUnacceptablePassword("Passwords Don't Match",
                    "Your 'Password' and 'Verify' does not match. Please enter again")
            } else {
                model.lockNote(password, passwordHint)
                return true
            }
        }
        return true
    }
    private fun showWarningForUnacceptablePassword(warningTitle: String, warningContent: String) {
        val alert = Alert(Alert.AlertType.WARNING)
        alert.title = warningTitle
        alert.contentText = warningContent
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
                // Temporary; will add more in later section
                model.updateSelection("")
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
            val newIndex = dateCreatedList.indexOf(model.getCurrSelected()?.dateCreated)
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