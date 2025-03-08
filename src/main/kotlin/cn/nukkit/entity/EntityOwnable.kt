package cn.nukkit.entity

import cn.nukkit.Player
import cn.nukkit.entity.ai.memory.CoreMemoryTypes

/**
 * 实现这个接口的实体可以被驯服
 *
 * @author BeYkeRYkt (Nukkit Project)
 */
interface EntityOwnable : EntityComponent {
    fun getOwnerName(): String? {
        return getMemoryStorage().get<String>(CoreMemoryTypes.Companion.OWNER_NAME)
    }

    fun setOwnerName(playerName: String?) {
        getMemoryStorage().put<String>(CoreMemoryTypes.Companion.OWNER_NAME, playerName)
    }

    fun getOwner(): Player? {
        var owner: Player? = getMemoryStorage().get<Player>(CoreMemoryTypes.Companion.OWNER)
        if (owner != null && owner.isOnline()) return owner
        else {
            val ownerName: String? = getOwnerName()
            if (ownerName == null) return null
            owner = asEntity()!!.getServer()!!.getPlayerExact(ownerName)
        }
        return owner
    }

    @JvmOverloads
    fun hasOwner(checkOnline: Boolean = true): Boolean {
        if (checkOnline) {
            return getOwner() != null
        } else {
            return getOwnerName() != null
        }
    }
}
