package de.maaxgr.passwordmanager.scenes

import de.maaxgr.passwordmanager.BitwardenRepository
import de.maaxgr.passwordmanager.entity.BitwardenItem
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.*
import javafx.scene.layout.BorderPane
import javafx.util.Callback
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
        val colNamespace = TableColumn<BitwardenItem, String>("Namespace")
        colNamespace.cellValueFactory = Callback { p ->
            SimpleStringProperty(repository.getFolderById(p.value.folderId ?: "").name)
        }

        val colName = TableColumn<BitwardenItem, String>("Name")
        colName.cellValueFactory = PropertyValueFactory("name")

        val colUsername = TableColumn<BitwardenItem, String>("Username")
        colUsername.cellValueFactory = Callback { p ->
            SimpleStringProperty(p.value.login.username)
        }

        //add columns to table view
        tableView.columns.addAll(colNamespace, colName, colUsername)

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
            val bwItem = tableView.selectionModel.selectedItem
            val copyNameComb = KeyCombination.keyCombination("Ctrl+B")
            val copyPWComb = KeyCombination.keyCombination("Ctrl+C")

            if (copyNameComb.match(it)) {
                putInClipboard(bwItem.login.username)
            } else if (copyPWComb.match(it)) {
                putInClipboard(bwItem.login.password)
            }
        }

        tableView.items = FXCollections.observableList(repository.getFiltered(""))

        return tableView
    }

    private fun showContextMenu(tableView: TableView<BitwardenItem>, sceneX: Double, sceneY: Double) {
        val cm = ContextMenu()
        val bwItem = tableView.selectionModel.selectedItem

        val copyUsernameMI = MenuItem("Username kopieren")
        copyUsernameMI.accelerator = KeyCombination.keyCombination("Ctrl+B")
        copyUsernameMI.onAction = EventHandler {
            putInClipboard(bwItem.login.username)
        }


        val copyPasswordMI = MenuItem("Passwort koplieren")
        copyPasswordMI.accelerator = KeyCombination.keyCombination("Ctrl+C")
        copyPasswordMI.onAction = EventHandler {
            putInClipboard(bwItem.login.password)
        }

        cm.items.addAll(copyUsernameMI, copyPasswordMI)
        println(bwItem.name)

        cm.show(tableView, sceneX, sceneY)

        activeContextMenu?.hide();
        activeContextMenu = cm
    }

    private fun putInClipboard(string: String) {
        val clipboard = Clipboard.getSystemClipboard()
        val content = ClipboardContent()
        content.putString(string)

        clipboard.setContent(content)
    }

}