package business

import javafx.scene.control.Alert
import javafx.scene.control.TextInputDialog
import presentation.IView

class Model {

    private val views = ArrayList<IView>()
    val noteList = mutableMapOf<String, Note>()
    val groupList = mutableListOf<Group>()
    private var currSelected = Note("", "")   // represent empty selection

    // note specific functions
    fun addNote() : Boolean {
        val newNote = Note()
        var containsNewNote = false
        var isAdded = false
        noteList.forEach {
            if (it.value.title == "New Note" && it.value.body == "") {
                // a new note has already been created
                containsNewNote = true
            }
        }
        if (!containsNewNote) {
            noteList[newNote.dateCreated] = newNote
            currSelected = newNote
            isAdded = true
            notifyViews()
        }
        return isAdded
    }

    fun addNoteUnderGroup() {
        // TODO
    }

    fun deleteNote(dateCreatedList: MutableList<String>) {
        val selectedNote = currSelected.dateCreated
        dateCreatedList.forEach {
            if (noteList.containsKey(it)) {
                // make currSelected field points to an empty note if removed
                if (it == selectedNote) currSelected = Note("", "")
                noteList.remove(it)
            } else {
                var flag = false
                for (group in groupList) {
                    val noteList = group.noteList
                    for (i in 0 until noteList.size) {
                        val note = noteList[i]
                        if (note.dateCreated == it) {
                            // make currSelected field points to an empty note if removed
                            if (it == selectedNote) currSelected = Note("", "")
                            noteList.removeAt(i)
                            flag = true
                            break
                        }
                    }
                    if (flag) break
                }
            }
        }
        notifyViews()
    }

    fun getCurrSelected() = currSelected

    fun updateSelection(dateCreated: String) {
        val newSelection = noteList[dateCreated]
        if (newSelection != null) {
            currSelected = newSelection
            notifyViews()
        }
    }

    fun changeSelectionContent(title: String, body: String) {
        currSelected.title = title
        currSelected.body = body
        notifyViews()
    }

    // group specific functions
    fun addGroup(groupName:String) {
        val newGroup = Group()
        newGroup.name = groupName
        groupList.add(newGroup)
        notifyViews()
    }

    // general functions
    fun addView(view: IView) {
        views.add(view)
    }

    fun removeView(view: IView) {
        views.remove(view)
    }

    private fun notifyViews() {
        for (view in views) {
            view.updateView()
        }
    }

}