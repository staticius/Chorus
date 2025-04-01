package org.chorus.entity

import org.chorus.Player
import org.chorus.Server
import org.chorus.entity.ai.memory.CoreMemoryTypes

interface EntityOwnable : EntityComponent {
    fun getOwnerName(): String? {
        return getMemoryStorage()[CoreMemoryTypes.OWNER_NAME]
    }

    fun setOwnerName(playerName: String) {
        getMemoryStorage()[CoreMemoryTypes.OWNER_NAME] = playerName
    }

    fun getOwner(): Player? {
        var owner: Player? = getMemoryStorage()[CoreMemoryTypes.OWNER]
        if (owner != null && owner.isOnline) return owner
        else {
            val ownerName: String = getOwnerName() ?: return null
            owner = Server.instance.getPlayerExact(ownerName)
        }
        return owner
    }

    val owner: Player?
        get() = getOwner()

    fun hasOwner(checkOnline: Boolean = true): Boolean {
        return if (checkOnline) {
            getOwner() != null
        } else {
            getOwnerName() != null
        }
    }
}
