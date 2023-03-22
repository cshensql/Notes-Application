package net.codebot.application

object ConfigData {
    const val DEFAULT_WIDTH: Double = 870.0
    const val DEFAULT_HEIGHT: Double = 720.0
    const val MIN_FILELIST_WIDTH: Double = 110.0
    const val MAX_FILELIST_WIDTH: Double = 200.0
    const val DEFAULT_POPUP_WIDTH: Double = 300.0
    const val DEFAULT_POPUP_HEIGHT: Double = 400.0
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