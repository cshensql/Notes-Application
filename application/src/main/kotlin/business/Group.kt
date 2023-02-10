package business

// folder name is required to create a folder
data class Group(
    var name: String,
    val noteList: MutableList<Note> = mutableListOf<Note>()
) : Pwd()