package net.codebot.application

object ConfigData {
    const val DEFAULT_WIDTH: Double = 870.0
    const val DEFAULT_HEIGHT: Double = 720.0
    const val MIN_FILELIST_WIDTH: Double = 110.0
    const val MAX_FILELIST_WIDTH: Double = 200.0
    val SORT_OPTIONS = listOf<String>("Title", "Date Modified", "Date Created")
    val SORT_ORDERS = listOf<String>("Ascending", "Descending")
    val SORT_RANGES_DEFAULT = listOf<String>("All Notes", "Groups", "Notes")
}