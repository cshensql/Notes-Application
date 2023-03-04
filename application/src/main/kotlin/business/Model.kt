package business

import javafx.scene.control.Alert
import javafx.scene.control.TextInputDialog
import presentation.IView

class Model {

    private val views = ArrayList<IView>()
    val noteList = mutableMapOf<String, Note>()
    val groupList = mutableListOf<Group>()
    private var currSelectedNote: Note? = null

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
            currSelectedNote = newNote
            isAdded = true
            notifyViews()
        }
        return isAdded
    }

    fun addNoteUnderGroup() {
        // TODO
    }

    fun deleteNote(dateCreatedList: MutableList<String>) {
        val selectedNote = currSelectedNote?.dateCreated ?: null
        dateCreatedList.forEach {
            if (noteList.containsKey(it)) {
                // make currSelected field points to an empty note if removed
                if (it == selectedNote) currSelectedNote = null
                noteList.remove(it)
            } else {
                var flag = false
                for (group in groupList) {
                    val noteList = group.noteList
                    for (i in 0 until noteList.size) {
                        val note = noteList[i]
                        if (note.dateCreated == it) {
                            // make currSelected field points to an empty note if removed
                            if (it == selectedNote) currSelectedNote = null
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

    fun getCurrSelected() = currSelectedNote

    fun updateSelection(dateCreated: String) {
        val newSelection = noteList[dateCreated]
        if (newSelection != null) {
            currSelectedNote = newSelection
            notifyViews()
        } else {
            currSelectedNote = null
            notifyViews()
        }
    }

    fun changeSelectionContent(title: String, body: String) {
        currSelectedNote?.title = title
        currSelectedNote?.body = body
        notifyViews()
    }

    // group specific functions
    fun addGroup(groupName:String) {
        val newGroup = Group()
        newGroup.name = groupName
        groupList.add(newGroup)
        notifyViews()
    }

    fun deleteGroup(groupSelectedList: MutableList<Group>){
        for (entry in groupSelectedList) {
            groupList.remove(entry)
        }
        notifyViews()
    }

    fun renameGroup(newName: String, groupRenamed: Group) {
        for (entry in groupList) {
            if (entry == groupRenamed) {
                entry.name = newName
                for (item in entry.noteList) {
                    item.groupName = newName
                }
                break
            }
        }
        notifyViews()
    }

    // If the note has been locked before, then relock it does not require the user to enter pwd/hint again
    fun lockNote(password: String = "", hint: String = "") {
        if (currSelectedNote != null && password.isNotEmpty()) {
            // The user sets up the password for the first time
            currSelectedNote?.setPwd(password)
            currSelectedNote?.passwordHint = hint
            currSelectedNote?.isLocked = true
            notifyViews()
        } else {
            // Old password exists
            currSelectedNote?.isLocked = true
            notifyViews()
        }
    }

    fun unlockNote() {
        if (currSelectedNote != null) {
            currSelectedNote?.isLocked = false
            notifyViews()
        }
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