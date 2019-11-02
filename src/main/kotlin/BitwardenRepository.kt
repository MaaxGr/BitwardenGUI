package de.maaxgr.passwordmanager

import de.maaxgr.passwordmanager.entity.BitwardenFolder
import de.maaxgr.passwordmanager.entity.BitwardenItem

class BitwardenRepository(private val provider: BitwardenPasswordProvider) {

    private val passwords: List<BitwardenItem> = provider.loadAllPasswords()
    private val folders: List<BitwardenFolder> = provider.loadFolders()

    fun getFiltered(filter: String): List<BitwardenItem> {
        if (filter.isEmpty()) {
            return passwords
        }

        val matchingPasswords = mutableListOf<BitwardenItem>()

        for (password in passwords) {
            if (password.name.contains(filter, true)) {
                if (!matchingPasswords.contains(password)) {
                    matchingPasswords.add(password)
                }
            }

            val folder = getFolderById(password.folderId ?: "")

            if (folder.name.contains(filter, true)) {
                if (!matchingPasswords.contains(password)) {
                    matchingPasswords.add(password)
                }
            }
        }

        return matchingPasswords
    }

    fun getFolderById(hash: String): BitwardenFolder {
        return folders.firstOrNull() { it.id == hash } ?: BitwardenFolder("", "")
    }

}