package presentation

import javafx.scene.control.Button
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.control.ToolBar
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class ToolBarView: IView, ToolBar() {

    private val saveButton = Button("Save")



    init {
        // Remove focus traversable
        saveButton.isFocusTraversable = false
        saveButton.graphic = ImageView(Image("save.png", 18.0, 18.0, true, true))
        this.items.add(saveButton)


    }

    override fun updateView() {
        TODO("Not yet implemented")
    }
}