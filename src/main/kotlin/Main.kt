package de.maaxgr.passwordmanager

import de.maaxgr.passwordmanager.scenes.PasswordTableViewScene
import de.maaxgr.passwordmanager.util.PropertiesReader
import javafx.application.Application
import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane
import javafx.stage.Stage



fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
}

class Main : Application() {

    lateinit var repository: BitwardenRepository
    lateinit var listView: ListView<String>
    lateinit var properties: PropertiesReader

    override fun start(primaryStage: Stage) {
        properties = PropertiesReader("settings.properties")

        val passwordProvider = BitwardenPasswordProvider(properties.getString("bitwarden.session"))
        repository = BitwardenRepository(passwordProvider)

        val layout = BorderPane()
        layout.center = createListView()


        //val scene = Scene(layout, 900.0, 450.0)
        val scene = PasswordTableViewScene(repository).scene

        primaryStage.scene = scene
        primaryStage.title = "Passwort Manager"
        primaryStage.show()

        registerSearch(scene)
    }

    private fun createTableView() {
        //tableView = TableView()


    }

    private fun createListView(): ListView<String> {
        listView = ListView()

        /*
        val folders = passwordProvider.loadFolders()

        val arrayList = mutableListOf<String>()

        folders.forEach {
            arrayList.addAll(
                    passwordProvider.loadPasswords(it).map { it.name }
            )
        }
         */

        val arrayList = repository.getFiltered("").map { it.name }

        val items = FXCollections.observableArrayList(
                arrayList)
        listView.items = items

        return listView
    }

    private fun registerSearch(scene: Scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED) {
            val combination = KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_ANY)

            if (combination.match(it)) {
                openSearchModal()
                println("Search pressed")
            }

        }
    }

    private fun openSearchModal() {
        val alert = TextInputDialog()
        alert.title = "Suche"
        alert.contentText = "Suchwort:"
        val result = alert.showAndWait()

        result.ifPresent {
            listView.items.clear()


            val arrayList = repository.getFiltered(it).map { item -> item.name }

            val items = FXCollections.observableArrayList(arrayList)

            listView.items = items

            listView.setCellFactory { lv ->

                val cell = ListCell<String>()
                //println(listView.items.indexOf())

                val contextMenu = ContextMenu()

                val editItem = MenuItem()
                editItem.textProperty().bind(Bindings.format("Edit \"%s\"", cell.itemProperty()))
                editItem.setOnAction({ event ->
                    val item = cell.getItem()
                    // code to edit item...
                })
                val deleteItem = MenuItem()
                deleteItem.textProperty().bind(Bindings.format("Delete \"%s\"", cell.itemProperty()))
                deleteItem.setOnAction({ event -> listView.items.remove(cell.getItem()) })
                contextMenu.getItems().addAll(editItem, deleteItem)

                cell.textProperty().bind(cell.itemProperty())

                cell.emptyProperty().addListener({ obs, wasEmpty, isNowEmpty ->
                    if (isNowEmpty) {
                        cell.setContextMenu(null)
                    } else {
                        cell.setContextMenu(contextMenu)
                    }
                })
                cell
            }

        }

    }

}
