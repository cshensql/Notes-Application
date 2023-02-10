package net.codebot.application

import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.Stage
import presentation.ContentView
import presentation.FileListView
import presentation.MenuBarView
import javafx.scene.web.HTMLEditor

class Main : Application()  {
    override fun start(stage: Stage) {

        // create the root of the scene graph
        // BorderPane supports placing children in regions around the screen
        val layout = BorderPane()
        val menuBar = MenuBarView()

        // build the scene graph
        layout.top = menuBar
        layout.left = FileListView()
        layout.padding = javafx.geometry.Insets(10.0,0.0,0.0,0.0)
        val backgroundFill = BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
        val newBackground = Background(backgroundFill)
        layout.background = newBackground

        // TODO: Need to update this to show selected notes
        layout.center = ContentView()


        // create and show the scene
        val scene = Scene(layout)
        stage.width = 900.0
        stage.height = 600.0
        stage.scene = scene
        stage.title = "Notes"
        stage.isResizable = false
        stage.show()
    }
}