package business

// folder name is required to create a folder
data class Group(
    var name: String = "New Group",
    val noteList: MutableList<Note> = mutableListOf<Note>()
)