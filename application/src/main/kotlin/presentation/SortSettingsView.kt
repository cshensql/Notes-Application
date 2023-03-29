package presentation

import business.Model
import javafx.geometry.Insets
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import net.codebot.application.ConfigData

// Copyright (c) 2023. Andy Yang, Benjamin Du, Charles Shen, Yuying Li

class SortSettingsView(val model: Model): GridPane() {
    // labels
    private val sortOptionsLabel = Label("       Sort Notes By: ")
    private val sortOrdersLabel = Label("              Sort Order: ")
    private val sortRangesLabel = Label("Sort Notes Under: ")

    private val sortOptions = ComboBox<String>()
    private val sortOrders = ComboBox<String>()
    private val sortRanges = ComboBox<String>()
    init {
        this.padding = Insets(15.0, 15.0,5.0, 15.0)
        this.vgap = 15.0
        this.hgap = 5.0

        sortOptions.items.addAll(ConfigData.SORT_OPTIONS)
        sortOrders.items.addAll(ConfigData.SORT_ORDERS)
        sortRanges.items.addAll(ConfigData.SORT_RANGES_DEFAULT)
        addGroupsForSortRanges()

        sortOptions.selectionModel.select(model.sortSettings[0])
        sortOrders.selectionModel.select(model.sortSettings[1])
        sortRanges.selectionModel.select(model.sortSettings[2])

        sortOptions.prefWidthProperty().bind(this.widthProperty())
        sortOrders.prefWidthProperty().bind(this.widthProperty())
        sortRanges.prefWidthProperty().bind(this.widthProperty())

        // render all the views
        this.add(sortOptionsLabel, 0, 0)
        this.add(sortOptions, 1,0,3,1)

        this.add(sortOrdersLabel,0,1)
        this.add(sortOrders, 1,1,3,1)

        this.add(sortRangesLabel, 0,2)
        this.add(sortRanges,1,2,3,1)
    }

    private fun addGroupsForSortRanges(){
        for (group in model.groupList){
            sortRanges.items.add(group.name)
        }
    }

    fun getSelections():List<Int>{
        return listOf(sortOptions.selectionModel.selectedIndex,
            sortOrders.selectionModel.selectedIndex, sortRanges.selectionModel.selectedIndex)
    }
}