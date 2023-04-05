package net.codebot.application

import Service.ServiceManager
import business.Model
import persistence.LocalSaving
import business.Note
import business.Group
import business.WindowConfig
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.Screen
import javafx.stage.Stage
import presentation.ContentView
import presentation.FileListView
import presentation.MenuBarView

// Copyright (c) 2023. Andy Yang, Benjamin Du, Charles Shen, Yuying Li

class Main : Application() {
    override fun start(stage: Stage) {

        // create persistence class
        val localSaving = LocalSaving()
        
        // create model
        val model = Model()

        // create the root of the scene graph
        // BorderPane supports placing children in regions around the screen
        val layout = BorderPane()
        val menuBar = MenuBarView(model)
        val fileList = FileListView(model)
        val contentView = ContentView(model)

        // add views to model
        model.addView(menuBar)
        model.addView(fileList)
        model.addView(contentView)

        // build the scene graph
        layout.top = menuBar
        layout.left = fileList
        layout.center = contentView
        layout.padding = javafx.geometry.Insets(10.0, 0.0, 0.0, 0.0)
        val backgroundFill = BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
        val newBackground = Background(backgroundFill)
        layout.background = newBackground

        // create and show the scene
        val scene = Scene(layout)
        stage.width = ConfigData.DEFAULT_WIDTH
        stage.height = ConfigData.DEFAULT_HEIGHT

        // set the minimum size of the window
        stage.minHeight = 350.0
        stage.minWidth = 500.0

        stage.isResizable = true
        stage.scene = scene
        stage.title = "Notes"

        // set up event handler for searchBar in menuBar
        menuBar.searchBar.setOnKeyPressed {
            if (it.code == KeyCode.ENTER) {
                menuBar.searchBar.isEditable = false
                val text = menuBar.searchBar.text
                if (text != "") {
                    val (byTitle, byContent) = menuBar.searchOptions
                    // search
                    fileList.search(text, byTitle, byContent)
                } else {
                    fileList.exitSearch()
                }
            }
        }
        menuBar.cancelButton.setOnMouseClicked {
            menuBar.searchBar.text = ""
            menuBar.searchBar.border = null
            menuBar.searchBar.isEditable = false
            // exit search
            fileList.exitSearch()
        }

        menuBar.toggleL.setOnAction{
            val curvalue = contentView.toggleSwitch.isSelected
            contentView.toggleSwitch.isSelected = !curvalue
        }

        // Bind the width and height properties for each view
        layout.prefWidthProperty().bind(scene.widthProperty())
        layout.prefHeightProperty().bind(scene.heightProperty())
        contentView.prefWidthProperty().bind(scene.widthProperty())

        // Whenever the window size changes, see if we need to change the file list width
        scene.widthProperty().addListener { observable, oldValue, newValue ->
            var expectedValue = newValue.toDouble() / 6
            if (expectedValue < ConfigData.MIN_FILELIST_WIDTH) {
                expectedValue = ConfigData.MIN_FILELIST_WIDTH
            } else if (expectedValue > ConfigData.MAX_FILELIST_WIDTH) {
                expectedValue = ConfigData.MAX_FILELIST_WIDTH
            }

            fileList.prefWidth = expectedValue
        }

        // Get screen info
        val screenBounds = Screen.getPrimary().visualBounds
        val defaultXPosition = (screenBounds.width - stage.width) / 2
        val defaultYPosition = (screenBounds.height - stage.height) / 2

        // Ideas for the code about getting and saving window location and size
        // are from here
        // http://broadlyapplicable.blogspot.com/2015/02/javafx-restore-window-size-position.html

        var windowConfig = WindowConfig(
            positionX = defaultXPosition,
            positionY = defaultYPosition,
            width = ConfigData.DEFAULT_WIDTH,
            height = ConfigData.DEFAULT_HEIGHT
        )

        val savedWindowConfig = localSaving.loadConfig()
        // Since we need some initial values in the local saved file
        // we put 1.0 as the height to indicate that no user data has been saved yet
        // since we have minimum height to be 720.0, so it is impossible for user to
        // set the height to 1.0. Hence, we use this value here as an indicator
        if (savedWindowConfig.height != 1.0) {
            // we have previously saved window config
            windowConfig = savedWindowConfig
        } else {
            // we save the data locally
            localSaving.saveConfig(windowConfig)
        }

        // the file is fresh new, we need to use default value and save to file
        stage.x = windowConfig.positionX
        stage.y = windowConfig.positionY
        stage.width = windowConfig.width
        stage.height = windowConfig.height


        // Store the window size and location when the stage closes
        stage.setOnCloseRequest {

            windowConfig.positionX = stage.x
            windowConfig.positionY = stage.y
            windowConfig.width = stage.width
            windowConfig.height = stage.height

            localSaving.saveConfig(windowConfig)
            saveData(model)


        }

        fetchData(model)
        model.notifyViews()

        stage.show()
    }

    private fun saveData(model: Model) {
        val notesToBeSaved = mutableListOf<Note>()
        val groupNamesToBeSaved = mutableListOf<String>()
        val recentlyDeletedNoteToBeSaved = mutableListOf<Note>()

        // Get notes to be saved
        notesToBeSaved.addAll(model.noteList.values)
        for (group in model.groupList) {
            notesToBeSaved.addAll(group.noteList)
        }

        // Get group names to be saved
        for (group in model.groupList) {
            groupNamesToBeSaved.add(group.name)
        }

        ServiceManager.updateNotesData(notesToBeSaved)
        ServiceManager.updateGroupsData(groupNamesToBeSaved)

        recentlyDeletedNoteToBeSaved.addAll(model.recentlyDeletedNoteList.values)
        ServiceManager.updateRecentlyDeletedNotesData(recentlyDeletedNoteToBeSaved)
    }

    private fun fetchData(model: Model) {
        val savedNoteList = ServiceManager.getNotesData()
        var noteList = LinkedHashMap<String, Note>()
        var groupList = LinkedHashMap<String, Group>()
        for (note in savedNoteList) {
            val groupName = note.groupName
            if (groupName.isNotEmpty()) {
                if (groupList.contains(groupName)) {
                    groupList[groupName]!!.noteList.add(note)
                } else {
                    val newGroup = Group(groupName, mutableListOf(note))
                    groupList[note.groupName] = newGroup
                }
            } else {
                noteList[note.dateCreated] = note
            }
        }

        val savedGroupNamesList = ServiceManager.getGroupsData()
        for (groupName in savedGroupNamesList) {
            if (!groupList.containsKey(groupName)) {
                val newGroup = Group(groupName, mutableListOf<Note>())
                groupList[groupName] = newGroup
            }
        }


        val savedRecentlyDeletedNotes = ServiceManager.getRecentlyDeletedNotesData()
        var recentlyDeletedNotes = LinkedHashMap<String, Note>()
        savedRecentlyDeletedNotes.forEach {
            recentlyDeletedNotes.put(it.dateCreated, it)
        }

        model.noteList = noteList
        model.groupList = groupList.values.toMutableList()
        model.recentlyDeletedNoteList = recentlyDeletedNotes
    }
}