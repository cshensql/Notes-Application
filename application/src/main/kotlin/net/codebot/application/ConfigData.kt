package net.codebot.application

// Copyright (c) 2023. Andy Yang, Benjamin Du, Charles Shen, Yuying Li
object ConfigData {
    const val DEFAULT_WIDTH: Double = 870.0
    const val DEFAULT_HEIGHT: Double = 720.0
    const val MIN_FILELIST_WIDTH: Double = 110.0
    const val MAX_FILELIST_WIDTH: Double = 200.0
    const val DEFAULT_POPUP_WIDTH: Double = 300.0
    const val DEFAULT_POPUP_HEIGHT: Double = 400.0
    const val SERVER_NOTES_GET_ADDRESS = "http://127.0.0.1:8080/notes/get"
    const val SERVER_NOTES_POST_ADDRESS = "http://127.0.0.1:8080/notes/add"
    const val SERVER_NOTES_DELETE_ADDRESS = "http://127.0.0.1:8080/notes/delete"
    const val SERVER_GROUPS_GET_ADDRESS = "http://127.0.0.1:8080/groups/get"
    const val SERVER_GROUPS_POST_ADDRESS = "http://127.0.0.1:8080/groups/add"
    const val SERVER_GROUPS_DELETE_ADDRESS = "http://127.0.0.1:8080/groups/delete"
    const val SERVER_RECENTLY_DELETE_NOTES_GET_ADDRESS = "http://127.0.0.1:8080/recently-deleted/get"
    const val SERVER_RECENTLY_DELETE_NOTES_POST_ADDRESS = "http://127.0.0.1:8080/recently-deleted/add"
    const val SERVER_RECENTLY_DELETE_NOTES_DELETE_ADDRESS = "http://127.0.0.1:8080/recently-deleted/delete"
    val SORT_OPTIONS = listOf<String>("Title", "Date Modified", "Date Created")
    val SORT_ORDERS = listOf<String>("Ascending", "Descending")
    val SORT_RANGES_DEFAULT = listOf<String>("All Notes", "Groups", "Notes")
    val invisibleControlsUnderLightweight =
        listOf<String>(".html-editor-cut", ".html-editor-copy",".html-editor-paste",".html-editor-align-left",
            ".html-editor-align-right",".html-editor-align-center",".html-editor-align-justify", ".html-editor-outdent",
            ".html-editor-indent", ".html-editor-bullets", ".html-editor-numbers", ".format-menu-button",
            ".font-family-menu-button", ".font-size-menu-button", ".html-editor-strike", ".html-editor-hr",
            ".html-editor-background")
}