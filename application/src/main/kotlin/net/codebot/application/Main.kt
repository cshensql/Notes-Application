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
import business.Model
import javafx.scene.web.HTMLEditor

class Main : Application()  {
    override fun start(stage: Stage) {

        // create model
        val model = Model()



        // create the root of the scene graph
        // BorderPane supports placing children in regions around the screen
        val layout = BorderPane()
        val menuBar = MenuBarView(model)
        val fileList = FileListView(model)

        // add views to model
        model.addView(menuBar)
        model.addView(fileList)

        // build the scene graph
        layout.top = menuBar
        layout.left = fileList
        layout.padding = javafx.geometry.Insets(10.0,0.0,0.0,0.0)
        val backgroundFill = BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
        val newBackground = Background(backgroundFill)
        layout.background = newBackground

        // TODO: Need to update this to show selected notes
        val contentView = ContentView()
        layout.center = contentView


        // create and show the scene
        val scene = Scene(layout)
        stage.width = 1000.0
        stage.height = 800.0

        // set the minimum size of the window
        stage.minHeight = 500.0

        stage.isResizable = true
        stage.scene = scene
        stage.title = "Notes"


        // Bind the width and height properties for each view
        layout.prefWidthProperty().bind(scene.widthProperty())
        layout.prefHeightProperty().bind(scene.heightProperty())
        contentView.prefWidthProperty().bind(scene.widthProperty())


        stage.show()
    }
}