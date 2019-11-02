package de.maaxgr.passwordmanager

import com.sun.javafx.application.LauncherImpl
import de.maaxgr.passwordmanager.scenes.PasswordTableViewScene
import de.maaxgr.passwordmanager.util.PropertiesReader
import javafx.application.Application
import javafx.application.Preloader
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
    LauncherImpl.launchApplication(Main::class.java, MainPreloader::class.java, args)
}

class MainPreloader : Preloader() {
    override fun start(p0: Stage?) {
        com.sun.glass.ui.Application.GetApplication().name = "Test"
        println("Test")
    }
}

class Main : Application() {

    lateinit var repository: BitwardenRepository
    lateinit var listView: ListView<String>
    lateinit var properties: PropertiesReader

    override fun start(primaryStage: Stage) {
        properties = PropertiesReader("settings.properties")

        val passwordProvider = BitwardenPasswordProvider(properties.getString("bitwarden.session"))
        repository = BitwardenRepository(passwordProvider)

        val scene = PasswordTableViewScene(repository).scene

        primaryStage.scene = scene
        primaryStage.title = "Passwort Manager"
        primaryStage.show()
    }


}
