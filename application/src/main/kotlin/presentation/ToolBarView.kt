package presentation

import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.control.ToolBar
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class ToolBarView: IView, ToolBar() {

    private val boldButton = ToggleButton("Bold")
    private val italicButton = ToggleButton("Italic")
    private val underlineButton = ToggleButton("Underline")
    private val bulletListButton = ToggleButton("Bulleted List")
    private val redColorButton = ToggleButton("Red")
    private val blueColorButton = ToggleButton("Blue")
    private val blackColorButton = ToggleButton("Black")
    private val colorGroup = ToggleGroup()



    init {
        // Remove focus traversable
        boldButton.isFocusTraversable = false
        italicButton.isFocusTraversable = false
        underlineButton.isFocusTraversable = false
        bulletListButton.isFocusTraversable = false
        redColorButton.isFocusTraversable = false
        blueColorButton.isFocusTraversable = false
        blackColorButton.isFocusTraversable = false



        // Set image for tools
        boldButton.graphic = ImageView(Image("bold.png", 18.0, 18.0, true, true))
        italicButton.graphic = ImageView(Image("italic.png", 18.0, 18.0, true, true))
        underlineButton.graphic = ImageView(Image("underline.png", 18.0, 18.0, true, true))
        bulletListButton.graphic = ImageView(Image("bulletList.png", 18.0, 18.0, true, true))
        redColorButton.graphic = ImageView(Image("red.png", 20.0, 20.0, true, true))
        blueColorButton.graphic = ImageView(Image("blue.png", 20.0, 20.0, true, true))
        blackColorButton.graphic = ImageView(Image("black.png", 20.0, 20.0, true, true))
        colorGroup.toggles.addAll(blackColorButton, redColorButton, blackColorButton)
        this.items.addAll(boldButton, italicButton, underlineButton, bulletListButton, blackColorButton, redColorButton, blueColorButton)
    }

    override fun updateView() {
        TODO("Not yet implemented")
    }
}