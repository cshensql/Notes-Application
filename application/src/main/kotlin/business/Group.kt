package business

// Copyright (c) 2023. Andy Yang, Benjamin Du, Charles Shen, Yuying Li

// folder name is required to create a folder
data class Group(
    var name: String = "New Group",
    var noteList: MutableList<Note> = mutableListOf<Note>()
)