package presentation

import business.Model
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
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
        addNote.accelerator = KeyCodeCombination(KeyCode.A, KeyCodeCombination.CONTROL_DOWN)
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
        // TODO: Add actual actions once model is done
        addNote.setOnAction {
            val isAdded =  model.addNote()
            if (!isAdded) {
                val alert = Alert(AlertType.WARNING,
                    "There is an empty note already, new note not created")
                alert.show()
            }
        }

        deleteNote.setOnAction {
            println("Delete note pressed")
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
            println("Add group pressed")
        }

        deleteGroup.setOnAction {
            println("Delete group pressed")
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


    override fun updateView() {

    }
}