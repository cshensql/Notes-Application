package business

import presentation.IView

class Model {

    private val views = ArrayList<IView>()
    val noteList = mutableMapOf<String, Note>()
    val groupList = mutableListOf<Group>()
    private var currSelectedNote: Note? = null
    private var currSelectedGroupIndex: Int = -1

    // note specific functions

    // Check if a new note is already created
    private fun containsNewNote(): Boolean {
        var containsNewNote = false

        // check if there is a new note under groups
        for (group in groupList) {
            for (note in group.noteList) {
                if (note.title == "New Note" && note.body == "") {
                    containsNewNote = true
                    break
                }
            }
            if (containsNewNote) break
        }
        if (containsNewNote) return true
        else {
            // check if there is a new note in noteList
            for (entry in noteList) {
                val note = entry.value
                if (note.title == "New Note" && note.body == "") {
                    containsNewNote = true
                    break
                }
            }
        }
        return containsNewNote
    }

    // Add a new note inside noteList
    fun addNote(): Boolean {
        var isAdded = false
        if (!containsNewNote()) {
            val newNote = Note()
            noteList[newNote.dateCreated] = newNote

            // update selection to newNote
            currSelectedNote = newNote
            currSelectedGroupIndex = -1

            isAdded = true
            notifyViews()
        }
        return isAdded
    }

    // Add a new note under currently selected group
    fun addNoteUnderGroup(): Boolean {
        var isAdded = false
        if (!containsNewNote() && currSelectedGroupIndex != -1) {
            val newNote = Note()
            val group = groupList[currSelectedGroupIndex]
            // modify groupName field of the new note and add it to the group
            newNote.groupName = group.name
            group.noteList.add(newNote)

            // update currSelectedNote
            currSelectedNote = newNote
            currSelectedGroupIndex = -1

            isAdded = true
            notifyViews()
        }
        return isAdded
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

    fun getCurrSelectedNote() = currSelectedNote
    fun getCurrSelectedGroupIndex() = currSelectedGroupIndex

    // The user needs to input either dateCreated to select a note,
    // or selectedGroupIndex to select a group.
    // If inputs are invalid, do nothing
    fun updateSelection(dateCreated: String = "", selectedGroupIndex: Int = -1) {
        // Check if a user tries to select a group
        if (selectedGroupIndex >= 0) {
            // change selection only if the inputs are valid
            if (selectedGroupIndex < groupList.size && dateCreated == "") {
                currSelectedNote = null
                currSelectedGroupIndex = selectedGroupIndex
                notifyViews()
            }
        } else {
            // selectedGroupIndex < 0: The user tries to select a note
            val newSelection = noteList[dateCreated]
            if (newSelection != null) {
                // newSelection is inside noteList
                currSelectedNote = newSelection
                currSelectedGroupIndex = -1
                notifyViews()
            } else {
                // newSelection is inside groupList or not valid
                var found = false
                for (group in groupList) {
                    for (note in group.noteList) {
                        if (note.dateCreated == dateCreated) {
                            currSelectedNote = note
                            currSelectedGroupIndex = -1
                            found = true
                            break
                        }
                        if (found) break
                    }
                }
                if (found) notifyViews()
            }
        }
    }

    fun changeSelectionContent(title: String, body: String) {
        currSelectedNote?.title = title
        currSelectedNote?.body = body
        notifyViews()
    }

    // group specific functions
    fun addGroup(groupName: String) {
        val newGroup = Group()
        newGroup.name = groupName
        groupList.add(newGroup)

        // update selection to the newGroup
        currSelectedNote = null
        currSelectedGroupIndex = groupList.size - 1

        notifyViews()
    }

    fun deleteGroup(groupSelectedList: MutableList<Group>) {
        // Need to check if currently selected group is removed
        val currSelectedGroup =
            if (currSelectedGroupIndex >= 0) groupList[currSelectedGroupIndex]
            else null
        for (entry in groupSelectedList) {
            groupList.remove(entry)
        }

        // check if initially a group is selected
        if (currSelectedGroupIndex >= 0) {
            // If the selected group is removed, index changes to -1
            // otherwise, get the new index
            currSelectedGroupIndex =
                if (currSelectedGroup == null) -1
                else groupList.indexOf(currSelectedGroup)
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