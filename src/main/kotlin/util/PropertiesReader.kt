package de.maaxgr.passwordmanager.util

import java.io.FileReader
import java.util.*

class PropertiesReader(path: String) {

    private val properties = Properties()

    init {
        properties.load(FileReader(path))
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return properties.getProperty(key, defaultValue)
    }

}