package presentation

import business.Model
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

    // list of dateCreated to help locate the correct note in noteList in model
    private val dateCreatedList = mutableListOf<String>()

    private val MAX_CHAR_SHOWN: Int = 15

    init {
        setupCategories()
        setupClickAction()
        setupContextMenuForTreeItem()
    }

    fun unlockNote() {
        val currSelectedNote = model.getCurrSelectedNote()
        if (currSelectedNote == null) return
        val passwordHint = currSelectedNote.passwordHint
        val correctPassword = currSelectedNote.getPwd()

        var shouldStop = false
        while (!shouldStop) {
            val alert = Alert(Alert.AlertType.CONFIRMATION)
            val inputView = VBox()
            val inputField = PasswordField()
            inputView.children.add(inputField)
            inputView.alignment = Pos.CENTER_LEFT
            inputView.spacing = 10.0
            if (passwordHint.isNotBlank() && passwordHint.isNotEmpty()) {
                inputView.children.add(Text("Hint: " + passwordHint))
            }
            alert.headerText = "Please enter the password"
            alert.dialogPane.content = inputView
            shouldStop = confirmPassword(alert.showAndWait(), inputField.text, correctPassword)
        }
    }

    fun lockNote() {
        val possiblePassword = model.getCurrSelectedNote()?.getPwd() ?: ""
        if (model.getCurrSelectedNote() != null && possiblePassword.isNotEmpty()) {
            model.lockNote()
        } else {
            val lockNoteView = LockNoteView()
            var shouldStop = false

            while (!shouldStop) {
                val alert = Alert(Alert.AlertType.CONFIRMATION)
                val dialogPane = alert.dialogPane

                dialogPane.content = lockNoteView
                alert.title = "Lock Note"
                alert.isResizable = true
                alert.width = 500.0
                alert.height = 600.0
                shouldStop = checkValidityofPassword(alert.showAndWait(), lockNoteView)
            }
        }
    }

    private fun setupContextMenuForTreeItem() {
        val lockNoteItem = MenuItem("Lock Note")
        val unlockNoteItem = MenuItem("Unlock Note")
        lockNoteItem.setOnAction {
            lockNote()
        }

        unlockNoteItem.setOnAction {
            unlockNote()
        }

        val contextMenu = ContextMenu(lockNoteItem)
        this.contextMenu = contextMenu
        this.setOnContextMenuRequested {
            if (this.selectionModel.selectedIndex >= 0 && this.selectionModel.selectedItem.parent == groupRoot) {
                this.contextMenu.hide()
            } else if (this.selectionModel.selectedIndex >= 0 && this.selectionModel.selectedItem.parent == noteRoot) {
                val currSelectedNote = model.getCurrSelectedNote()
                val noteIsLocked = currSelectedNote?.isLocked ?: false
                contextMenu.items.clear()
                if (noteIsLocked) {
                    contextMenu.items.add(unlockNoteItem)
                } else {
                    contextMenu.items.add(lockNoteItem)
                }
            }
        }
    }

    private fun confirmPassword(
        result: Optional<ButtonType>,
        passwordEntered: String,
        correctPassword: String
    ): Boolean {
        if (result.isPresent && result.get() == ButtonType.OK) {
            if (passwordEntered != correctPassword) {
                val warningAlert = WarningAlertView(
                    "Incorret Password",
                    "The password entered does not match the one we have. Please enter again."
                )
                warningAlert.present()
                return false
            }
            model.unlockNote()
            return true
        }
        return true
    }

    private fun checkValidityofPassword(result: Optional<ButtonType>, lockNoteView: LockNoteView): Boolean {
        if (result.isPresent && result.get() == ButtonType.OK) {
            val password = lockNoteView.getPassword()
            val verifiedPassword = lockNoteView.getVerifiedPassword()
            val passwordHint = lockNoteView.getPasswordHint()
            if (password.isNullOrBlank() || password.isEmpty() || verifiedPassword.isNullOrBlank() || verifiedPassword.isEmpty()) {
                val warningAlert = WarningAlertView(
                    "Blank or Empty Passwords",
                    "You entered blank or empty string for 'Password' or 'Verify'. Please enter again"
                )
                warningAlert.present()
                lockNoteView.clearInputPassword()
                return false
            } else if (password != verifiedPassword) {
                val warningAlert = WarningAlertView(
                    "Passwords Don't Match",
                    "Your 'Password' and 'Verify' does not match. Please enter again"
                )
                warningAlert.present()
                lockNoteView.clearInputPassword()
                return false
            } else {
                model.lockNote(password, passwordHint)
                return true
            }
        }
        lockNoteView.clearHint()
        lockNoteView.clearInputPassword()
        return true
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
                // TODO: Use if the note has a child and whether it is a group name to decide
                // TODO: We can use the group name to help locate the note and get the date created
                val selectedItem = this.selectionModel.selectedItem
                val parentItem = selectedItem.parent
                // The selectedItem is a group
                if (parentItem == groupRoot) {
                    val groupIndex = parentItem.children.indexOf(selectedItem)
                    model.updateSelection(selectedGroupIndex = groupIndex)
                } else if (parentItem.parent == groupRoot) {
                    // The selectedItem is a note under a group
                    val groupIndex = parentItem.parent.children.indexOf(parentItem)
                    val noteIndex = parentItem.children.indexOf(selectedItem)
                    model.updateSelection(indices = Pair(groupIndex, noteIndex))
                }
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
        root.isExpanded = true
        this.isFocusTraversable = false
    }

    private fun getNoteRootIndex(): Int {
        var retval = noteRoot.parent.children.indexOf(noteRoot) + 1
        if (groupRoot.isExpanded) {
            retval += groupRoot.children.size
            for (group in groupRoot.children) {
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

    // Helper function to return a string that can be put into TreeItem
    private fun getValidName(name: String): String {
        return if (name.length > MAX_CHAR_SHOWN) name.substring(0, MAX_CHAR_SHOWN) + "..."
        else name
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
            val titleShown = getValidName(note.value.title)
            val noteItem = TreeItem(titleShown)
            noteRoot.children.add(noteItem)
            dateCreatedList.add(note.key)
        }

        for (group in model.groupList) {
            val nameShown = getValidName(group.name)
            val groupItem = TreeItem(nameShown)
            groupItem.isExpanded = true
            for (note in group.noteList) {
                val titleShown = getValidName(note.title)
                groupItem.children.add(TreeItem(titleShown))
            }
            groupRoot.children.add(groupItem)
        }

        val newNumOfNotes = noteRoot.children.size
        // A note is added or deleted in the Notes section
        if (newNumOfNotes != numOfNotes) {
            val newIndex = dateCreatedList.indexOf(model.getCurrSelectedNote()?.dateCreated)
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