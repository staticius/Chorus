package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.event.entity.EntityDeathEvent
import cn.nukkit.item.Item
import cn.nukkit.lang.TextContainer
import cn.nukkit.lang.TranslationContainer

class PlayerDeathEvent(player: Player?, drops: Array<Item>?, @JvmField var deathMessage: TextContainer, @JvmField var experience: Int) :
    EntityDeathEvent(player, drops), Cancellable {
    @JvmField
    var keepInventory: Boolean = false
    @JvmField
    var keepExperience: Boolean = false

    constructor(player: Player?, drops: Array<Item>?, deathMessage: String?, experience: Int) : this(
        player,
        drops,
        TextContainer(deathMessage),
        experience
    )

    override var entity: Entity?
        get() = super.getEntity() as Player
        set(entity) {
            super.entity = entity
        }

    val translationDeathMessage: TranslationContainer
        get() = if (deathMessage is TranslationContainer) deathMessage as TranslationContainer else TranslationContainer(
            deathMessage.text
        )

    fun setDeathMessage(deathMessage: TranslationContainer) {
        this.deathMessage = deathMessage
    }

    fun setDeathMessage(deathMessage: TextContainer) {
        this.deathMessage = deathMessage
    }

    fun setDeathMessage(deathMessage: String?) {
        this.deathMessage = TextContainer(deathMessage)
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
