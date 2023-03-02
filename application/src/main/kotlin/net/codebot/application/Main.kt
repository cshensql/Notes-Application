package net.codebot.application

import business.Model
import business.WindowConfig
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.BorderPane
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Color
import javafx.stage.Screen
import javafx.stage.Stage
import presentation.ContentView
import presentation.FileListView
import presentation.MenuBarView
import java.util.prefs.Preferences

class Main : Application() {
    // Constants used to save window size and position
    private val WINDOW_POSITION_X = "Window_Position_X"
    private val WINDOW_POSITION_Y = "Window_Position_Y"
    private val WINDOW_WIDTH = "Window_Width"
    private val WINDOW_HEIGHT = "Window_Height"
    private val DEFAULT_WIDTH: Double = 870.0
    private val DEFAULT_HEIGHT: Double = 720.0
    private val NODE_NAME = "Main"
    private val MIN_FILELIST_WIDTH: Double = 110.0
    private val MAX_FILELIST_WIDTH: Double = 200.0
    override fun start(stage: Stage) {

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
        stage.width = DEFAULT_WIDTH
        stage.height = DEFAULT_HEIGHT

        // set the minimum size of the window
        stage.minHeight = 350.0
        stage.minWidth = 500.0

        stage.isResizable = true
        stage.scene = scene
        stage.title = "Notes"



        // Bind the width and height properties for each view
        layout.prefWidthProperty().bind(scene.widthProperty())
        layout.prefHeightProperty().bind(scene.heightProperty())
        contentView.prefWidthProperty().bind(scene.widthProperty())
        scene.widthProperty().addListener { observable, oldValue, newValue ->
            var expectedValue = newValue.toDouble() / 6
            if (expectedValue < MIN_FILELIST_WIDTH) {
                expectedValue = MIN_FILELIST_WIDTH
            } else if (expectedValue > MAX_FILELIST_WIDTH) {
                expectedValue = MAX_FILELIST_WIDTH
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
            width = DEFAULT_WIDTH,
            height = DEFAULT_HEIGHT
        )

        // Get window location from user preferences
        // TODO: If the WindowConfig JSON file does not exist, then we get the data from userPrefs.
        //  If the WindowConfig JSON file exists, then we get the data from JSON file
        val userPrefs = Preferences.userRoot().node(NODE_NAME)
        val windowPositionX = userPrefs.getDouble(WINDOW_POSITION_X, defaultXPosition)
        val windowPositionY = userPrefs.getDouble(WINDOW_POSITION_Y, defaultYPosition)
        val windowWidth = userPrefs.getDouble(WINDOW_WIDTH, DEFAULT_WIDTH)
        val windowHeight = userPrefs.getDouble(WINDOW_HEIGHT, DEFAULT_HEIGHT)
        stage.x = windowPositionX
        stage.y = windowPositionY
        stage.width = windowWidth
        stage.height = windowHeight


        // Store the window size and location when the stage closes
        stage.setOnCloseRequest {
            val userPref = Preferences.userRoot().node(NODE_NAME)
            userPref.putDouble(WINDOW_POSITION_X, stage.x)
            userPref.putDouble(WINDOW_POSITION_Y, stage.y)
            userPref.putDouble(WINDOW_WIDTH, stage.width)
            userPref.putDouble(WINDOW_HEIGHT, stage.height)

            windowConfig.positionX = stage.x
            windowConfig.positionY = stage.y
            windowConfig.width = stage.width
            windowConfig.height = stage.height

            // TODO: Update the corresponding JSON file
        }

        stage.show()
    }
}