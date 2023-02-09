package presentation

import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class ToolBarView: IView, ToolBar() {

    private val boldButton = Button("Bold")
    private val italicButton = Button("Italic")
    private val underlineButton = Button("Underline")
    private val colorButton = Button("Color")



    init {
        boldButton.isFocusTraversable = false
        italicButton.isFocusTraversable = false
        underlineButton.isFocusTraversable = false
        colorButton.isFocusTraversable = false

        boldButton.graphic = ImageView(Image("bold.png", 18.0, 18.0, true, true))
        italicButton.graphic = ImageView(Image("italic.png", 18.0, 18.0, true, true))
        underlineButton.graphic = ImageView(Image("underline.png", 18.0, 18.0, true, true))
        colorButton.graphic = ImageView(Image("color.png", 18.0, 18.0, true, true))

        this.items.addAll(boldButton, italicButton, underlineButton, colorButton)
    }

    override fun updateView() {
        TODO("Not yet implemented")
    }
}