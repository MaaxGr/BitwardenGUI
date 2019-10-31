package de.maaxgr.passwordmanager

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.maaxgr.passwordmanager.entity.BitwardenFolder
import de.maaxgr.passwordmanager.entity.BitwardenItem
import de.maaxgr.passwordmanager.util.CommandLineExecutor

class BitwardenPasswordProvider {

    companion object {
        const val SESSION = ""
    }

    fun loadFolders(): List<BitwardenFolder> {
        val result = CommandLineExecutor.run("bw list folders --session $SESSION")

        val listWithType = object : TypeToken<List<BitwardenFolder>>() {}.type
        val items = Gson().fromJson<List<BitwardenFolder>>(result, listWithType)

        return items
    }

    fun loadPasswords(folder: BitwardenFolder): List<BitwardenItem> {
        val result = CommandLineExecutor.run("bw list items --folderid ${folder.id} --session $SESSION")

        val listWithType = object : TypeToken<List<BitwardenItem>>() {}.type
        val items = Gson().fromJson<List<BitwardenItem>>(result, listWithType)

        return items
    }

    fun loadAllPasswords(): List<BitwardenItem> {
        val result = CommandLineExecutor.run("bw list items --session $SESSION")

        val listWithType = object : TypeToken<List<BitwardenItem>>() {}.type
        val items = Gson().fromJson<List<BitwardenItem>>(result, listWithType)

        return items
    }

    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

}