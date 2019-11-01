package de.maaxgr.passwordmanager.scenes

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.util.Callback
import java.awt.MouseInfo


class TableViewScene {

    val scene: Scene

    private val data = getTestData()

    init {
        val borderPane = BorderPane()

        val tableView = TableView<TableEntry>()

        val tableCol = TableColumn<TableEntry, String>("Name")
        tableCol.cellValueFactory = PropertyValueFactory("name")

        val tableCol2 = TableColumn<TableEntry, String>("Alter")
        tableCol2.cellValueFactory = PropertyValueFactory("age")

        val tableCol3 = TableColumn<TableEntry, String>("Baum")

        tableCol3.cellFactory = Callback { p -> TableCell() }


        tableCol3.cellValueFactory = PropertyValueFactory("baum")

        tableView.columns.addAll(tableCol, tableCol2, tableCol3)

        tableView.addEventHandler(MouseEvent.MOUSE_CLICKED) {
            if(it.button == MouseButton.SECONDARY) {

                val p = MouseInfo.getPointerInfo().location
                println(p.x)
                println(p.x)
                println(it.sceneY)
                showContextMenu(tableView, p.x.toDouble(), p.y.toDouble())
            }
        }

        tableView.items = data

        borderPane.center = tableView
        scene = Scene(borderPane, 900.0, 450.0)
    }

    private fun getTestData(): ObservableList<TableEntry> {
        val entry = TableEntry("Max", 21, true)
        val entry2 = TableEntry("Marie", 14, false)

        return FXCollections.observableArrayList(entry, entry2)
    }

    private fun showContextMenu(tableView: TableView<TableEntry>, sceneX: Double, sceneY: Double) {

        val cm = ContextMenu()
        val mi = MenuItem("Menu 1")
        val mii = MenuItem("Menu 1")
        cm.items.addAll(mi, mii)

        cm.show(tableView, sceneX, sceneY)
    }


    data class TableEntry(
            val name: String,
            val age: Int,
            val baum: Boolean
    )

}