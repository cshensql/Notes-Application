package presentation

import business.Group
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
    private val lockNote = MenuItem("Lock Note")
    private val groupNotes = MenuItem("Group Notes")
    private val moveNotes = MenuItem("Move Notes")


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
        lockNote.accelerator = KeyCodeCombination(KeyCode.L, KeyCodeCombination.CONTROL_DOWN)
        groupNotes.accelerator = KeyCodeCombination(KeyCode.G, KeyCodeCombination.CONTROL_DOWN)
        moveNotes.accelerator = KeyCodeCombination(KeyCode.M, KeyCodeCombination.CONTROL_DOWN)
        addGroup.accelerator = KeyCodeCombination(KeyCode.A, KeyCodeCombination.ALT_DOWN)
        deleteGroup.accelerator = KeyCodeCombination(KeyCode.D, KeyCodeCombination.ALT_DOWN)
        renameGroup.accelerator = KeyCodeCombination(KeyCode.R, KeyCodeCombination.ALT_DOWN)
        searchByContent.accelerator = KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_DOWN)
        searchByTitle.accelerator = KeyCodeCombination(KeyCode.T, KeyCodeCombination.CONTROL_DOWN)


        // Set actions for each submenu item

        addNote.setOnAction {
            val isAdded =  model.addNote()
            if (!isAdded) {
                val alert = Alert(AlertType.WARNING,
                    "There is an empty note already, new note not created")
                alert.show()
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

        lockNote.setOnAction {
            println("Lock note pressed")
        }

        groupNotes.setOnAction {
            println("Group notes pressed")
        }

        moveNotes.setOnAction {
            println("Move notes pressed")
        }

        addGroup.setOnAction {
            val renamePrompt = TextInputDialog()
            renamePrompt.title = "Create Group"
            renamePrompt.headerText = "Enter the New Name for the Group"
            val result = renamePrompt.showAndWait()
            var newGroupName: String = ""
            if (result.isPresent) {
                newGroupName = renamePrompt.editor.text
                if (newGroupName == "") {
                    showErrorMessage("Empty Group names are not allowed")
                } else {
                    var isDuplicateGroupName = false
                    for (group in this.model.groupList) {
                        if (group.name == newGroupName) {
                            showErrorMessage("Duplicate Group names are not allowed")
                            isDuplicateGroupName = true
                            break
                        }
                    }
                    if (!isDuplicateGroupName) {
                        model.addGroup(newGroupName)
                    }
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
            println("Rename group pressed")
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
        noteMenu.items.add(lockNote)
        noteMenu.items.add(groupNotes)
        noteMenu.items.add(moveNotes)
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

    }
}