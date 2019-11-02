package de.maaxgr.passwordmanager.scenes

import de.maaxgr.passwordmanager.BitwardenRepository
import de.maaxgr.passwordmanager.entity.BitwardenItem
import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.*
import javafx.scene.layout.BorderPane
import javafx.util.Callback
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.MouseInfo

class PasswordTableViewScene(private val repository: BitwardenRepository) {

    val scene: Scene
    lateinit var tableView: TableView<BitwardenItem>
    var activeContextMenu: ContextMenu? = null

    init {
        val borderPane = BorderPane()

        createTableView()

        borderPane.center = tableView


        scene = Scene(borderPane, 900.0, 450.0)

        registerSearch(scene)
    }

    private fun createTableView() {
        tableView = TableView()

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

        cm.show(tableView, sceneX, sceneY)

        activeContextMenu?.hide();
        activeContextMenu = cm
    }

    private fun registerSearch(scene: Scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED) {
            val combination = KeyCodeCombination.keyCombination("Ctrl+F")
            if (combination.match(it)) {
                openSearchModal()
            }

        }
    }

    private fun openSearchModal() {
        Platform.runLater {
            val alert = TextInputDialog()
            alert.disableResizing()
            alert.title = "Suche"
            alert.contentText = "Suchwort:"
            val result = alert.showAndWait()

            result.ifPresent {
                tableView.items = FXCollections.observableList(repository.getFiltered(it))
            }
        }
    }

    private fun putInClipboard(string: String) {
        val clipboard = Clipboard.getSystemClipboard()
        val content = ClipboardContent()
        content.putString(string)

        clipboard.setContent(content)
    }

    fun <T> Dialog<T>.disableResizing() {
        isResizable = true
        onShown = EventHandler {
            GlobalScope.launch {
                delay(1)

                Platform.runLater {
                    isResizable = false
                }
            }
        }
    }

}