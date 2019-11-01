package de.maaxgr.passwordmanager.entity

data class BitwardenItem(
        val id: String,
        val folderId: String,
        val type: Int,
        val name: String,
        val notes: String,
        val login: BitwardenItemLogin,
        val revisionDate: String
)