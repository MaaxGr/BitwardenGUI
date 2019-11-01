package de.maaxgr.passwordmanager.scenes

import de.maaxgr.passwordmanager.BitwardenRepository
import de.maaxgr.passwordmanager.entity.BitwardenItem
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import java.awt.MouseInfo

class PasswordTableViewScene(private val repository: BitwardenRepository) {

    val scene: Scene
    var activeContextMenu: ContextMenu? = null

    init {
        val borderPane = BorderPane()
        borderPane.center = createCenterView()


        scene = Scene(borderPane, 900.0, 450.0)
    }

    private fun createCenterView(): Node {
        return createTableView()
    }

    private fun createTableView(): TableView<BitwardenItem> {
        val tableView = TableView<BitwardenItem>()

        //init columns
        val colName = TableColumn<BitwardenItem, String>("Name")
        colName.cellValueFactory = PropertyValueFactory("name")

        //add columns to table view
        tableView.columns.addAll(colName)

        //on table view entry klicked
        tableView.addEventHandler(MouseEvent.MOUSE_CLICKED) {
            if(it.button == MouseButton.SECONDARY) {

                val p = MouseInfo.getPointerInfo().location
                showContextMenu(tableView, p.x.toDouble(), p.y.toDouble())
            } else {
                activeContextMenu?.hide()
            }
        }

        tableView.addEventHandler(KeyEvent.KEY_PRESSED) {
            val comb = KeyCombination.keyCombination("Ctrl+C")

            if (comb.match(it)) {
                val bwItem = tableView.selectionModel.selectedItem
                println("Copy: " + bwItem.name)

            }
        }

        tableView.items = FXCollections.observableList(repository.getFiltered(""))

        return tableView
    }

    private fun showContextMenu(tableView: TableView<BitwardenItem>, sceneX: Double, sceneY: Double) {
        val cm = ContextMenu()
        cm.isAutoHide = true

        val mi = MenuItem("Username kopieren")
        mi.accelerator = KeyCombination.keyCombination("Ctrl+C")
        mi.onAction = EventHandler {
            println("copy")
        }


        val mii = MenuItem("Menu 2")
        cm.items.addAll(mi, mii)

        val bwItem = tableView.selectionModel.selectedItem
        println(bwItem.name)

        cm.show(tableView, sceneX, sceneY)

        activeContextMenu?.hide();
        activeContextMenu = cm
    }

}