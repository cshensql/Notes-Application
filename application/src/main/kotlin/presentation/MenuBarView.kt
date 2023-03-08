package presentation

import business.Model
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination

class MenuBarView(model: Model) : IView, MenuBar() {
    // Model
    private val model = model

    // top: menubar
    private val noteMenu = Menu("Note")
    private val groupMenu = Menu("Group")
    private val searchMenu = Menu("Search")

    // Note Menu Sub options
    private val addNote = MenuItem("Add Note")
    private val deleteNote = MenuItem("Delete Note")
    private val lockOrUnlockNote = MenuItem("Lock Note")
    private val groupNotes = MenuItem("Group Notes")
    private val moveNotes = MenuItem("Move Notes")
    private val recoverNote = MenuItem("Recover Notes")

    // Group Menu Sub options
    private val addGroup = MenuItem("Add Group")
    private val deleteGroup = MenuItem("Delete Group")
    private val renameGroup = MenuItem("Rename Group")

    // Search Menu Sub options
    private val searchByTitle = MenuItem("Search By Note Title")
    private val searchByContent = MenuItem("Search By Note Content")

    init {
        // Set hotkeys for each feature
        addNote.accelerator = KeyCodeCombination(KeyCode.N, KeyCodeCombination.CONTROL_DOWN)
        deleteNote.accelerator = KeyCodeCombination(KeyCode.D, KeyCodeCombination.CONTROL_DOWN)
        lockOrUnlockNote.accelerator = KeyCodeCombination(KeyCode.L, KeyCodeCombination.CONTROL_DOWN)
        groupNotes.accelerator = KeyCodeCombination(KeyCode.G, KeyCodeCombination.CONTROL_DOWN)
        moveNotes.accelerator = KeyCodeCombination(KeyCode.M, KeyCodeCombination.CONTROL_DOWN)
        recoverNote.accelerator = KeyCodeCombination(KeyCode.R, KeyCodeCombination.CONTROL_DOWN)
        addGroup.accelerator = KeyCodeCombination(KeyCode.A, KeyCodeCombination.ALT_DOWN)
        deleteGroup.accelerator = KeyCodeCombination(KeyCode.D, KeyCodeCombination.ALT_DOWN)
        renameGroup.accelerator = KeyCodeCombination(KeyCode.R, KeyCodeCombination.ALT_DOWN)
        searchByContent.accelerator = KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_DOWN)
        searchByTitle.accelerator = KeyCodeCombination(KeyCode.T, KeyCodeCombination.CONTROL_DOWN)

        // check for duplicate group name

        fun isAllowedGroupName(inputGroupName: String): Boolean {
            if (inputGroupName == "") {
                showErrorMessage("Empty Group names are not allowed")
                return false
            }
            for (group in this.model.groupList) {
                if (group.name == inputGroupName) {
                    showErrorMessage("Duplicate Group names are not allowed")
                    return false
                }
            }
            return true
        }

        // Set actions for each submenu item

        addNote.setOnAction {
            val isAdded  =
                if (model.getCurrSelectedGroupIndex() >= 0) model.addNoteUnderGroup()
                else model.addNote()
            if (!isAdded) {
                val alert = WarningAlertView("Duplicate New Note",
                    "There is an empty note already, new note not created")
                alert.present()
            }
        }

        deleteNote.setOnAction {
            val alert = Alert(AlertType.CONFIRMATION)
            val dialogPane = alert.dialogPane
            val deleteNoteView = DeleteNoteView(model)

            dialogPane.content = deleteNoteView
            alert.title = "Delete"
            alert.isResizable = true
            alert.width = 300.0
            alert.height = 400.0

            val result = alert.showAndWait()

            if (!result.isPresent) {
                // alert is exited, no button has been pressed.
            } else if (result.get() == ButtonType.OK) {
                val selectedItems = deleteNoteView.getDateCreatedList()
                model.deleteNote(selectedItems)
            } else if (result.get() == ButtonType.CANCEL){
                // cancel button is pressed
            }
        }

        groupNotes.setOnAction {
            println("Group notes pressed")
        }

        moveNotes.setOnAction {
            println("Move notes pressed")
        }

        recoverNote.setOnAction {
            val alert = Alert(AlertType.CONFIRMATION)
            val dialogPane = alert.dialogPane
            val recoverNoteView = RecoverNoteView(model)

            dialogPane.content = recoverNoteView
            alert.title = "Recover"
            alert.isResizable = true
            alert.width = 300.0
            alert.height = 400.0

            val result = alert.showAndWait()

            if (!result.isPresent) {
                // alert is exited, no button has been pressed.
            } else if (result.get() == ButtonType.OK) {
                val selectedItems = recoverNoteView.getRecentlyDeletedNotesSelected()
                model.recoverNote(selectedItems)
            } else if (result.get() == ButtonType.CANCEL){
                // cancel button is pressed
            }
        }

        addGroup.setOnAction {
            val renamePrompt = TextInputDialog()
            renamePrompt.title = "Create Group"
            renamePrompt.headerText = "Enter the New Name for the Group"
            val result = renamePrompt.showAndWait()
            var newGroupName: String = ""
            if (result.isPresent) {
                newGroupName = renamePrompt.editor.text
                if (isAllowedGroupName(newGroupName)) {
                    model.addGroup(newGroupName)
                }
            }
        }

        deleteGroup.setOnAction {
            val alert = Alert(AlertType.CONFIRMATION)
            val dialogPane = alert.dialogPane
            val deleteGroupView = DeleteGroupView(model)

            dialogPane.content = deleteGroupView
            alert.title = "Delete"
            alert.isResizable = true
            alert.width = 300.0
            alert.height = 400.0

            val result = alert.showAndWait()

            if (!result.isPresent) {
                // alert is exited, no button has been pressed.
            } else if (result.get() == ButtonType.OK) {
                val selectedItems = deleteGroupView.getGroupSelectedList()
                model.deleteGroup(selectedItems)
            } else if (result.get() == ButtonType.CANCEL){
                // cancel button is pressed
            }
        }

        renameGroup.setOnAction {
            val alert = Alert(AlertType.CONFIRMATION)
            val dialogPane = alert.dialogPane
            val renameGroupView = RenameGroupView(model)

            dialogPane.content = renameGroupView
            alert.title = "Rename"
            alert.isResizable = true
            alert.width = 300.0
            alert.height = 400.0

            val result = alert.showAndWait()

            if (result.isPresent && result.get() == ButtonType.OK) {
                val selectedGroup = renameGroupView.getGroupSelected()
                // rename dialog pop up below
                val renamePrompt = TextInputDialog()
                renamePrompt.title = "Rename Group"
                renamePrompt.headerText = "Enter the New Name for the Group"
                val renamePromptResult = renamePrompt.showAndWait()
                var newGroupName: String = ""
                if (renamePromptResult.isPresent) {
                    newGroupName = renamePrompt.editor.text
                    if (isAllowedGroupName(newGroupName)) {
                        model.renameGroup(newGroupName, selectedGroup)
                    }
                }
            }
        }

        searchByContent.setOnAction {
            println("Search by content pressed")
        }

        searchByTitle.setOnAction {
            println("Search by title pressed")
        }


        // Add menu options to menubar
        this.menus.add(noteMenu)
        this.menus.add(groupMenu)
        this.menus.add(searchMenu)

        // Add submenu to their corresponding menu
        noteMenu.items.add(addNote)
        noteMenu.items.add(deleteNote)
        noteMenu.items.add(lockOrUnlockNote)
        noteMenu.items.add(groupNotes)
        noteMenu.items.add(moveNotes)
        noteMenu.items.add(recoverNote)
        groupMenu.items.add(addGroup)
        groupMenu.items.add(deleteGroup)
        groupMenu.items.add(renameGroup)
        searchMenu.items.add(searchByTitle)
        searchMenu.items.add(searchByContent)
    }

    // display error message function
    private fun showErrorMessage(message: String) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = "Warning"
        alert.headerText = message
        alert.showAndWait()
    }

    override fun updateView() {
        val currSelectedNote = model.getCurrSelectedNote()
        if (currSelectedNote != null) {
            val isLocked = currSelectedNote?.isLocked ?: false
            if (isLocked) {
                lockOrUnlockNote.text = "Unlock Note"
                lockOrUnlockNote.setOnAction {
                    FileListView(model).unlockNote()
                }
            } else {
                lockOrUnlockNote.text = "Lock Note"
                lockOrUnlockNote.setOnAction {
                    FileListView(model).lockNote()
                }
            }
        } else {
            lockOrUnlockNote.text = "Lock Note"
            lockOrUnlockNote.setOnAction {
                val warningAlert = WarningAlertView("No Note Selected", "\"Please select a note first\"")
                warningAlert.present()
            }
        }
    }
}