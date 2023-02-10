package business

// folder name is required to create a folder
class Group(var name: String) : Pwd() {
    val noteList: MutableList<Note> = mutableListOf<Note>()
}