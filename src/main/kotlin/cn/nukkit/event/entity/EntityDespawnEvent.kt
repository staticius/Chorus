package cn.nukkit.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.entity.EntityCreature
import cn.nukkit.entity.EntityHuman
import cn.nukkit.entity.item.EntityItem
import cn.nukkit.entity.projectile.EntityProjectile
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.level.Locator

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EntityDespawnEvent(entity: Entity) : EntityEvent(), Cancellable {
    val type: Int

    init {
        this.entity = entity
        this.type = entity.getNetworkId()
    }

    val position: Locator
        get() = entity!!.getLocator()

    val isCreature: Boolean
        get() = entity is EntityCreature

    val isHuman: Boolean
        get() = entity is EntityHuman

    val isProjectile: Boolean
        get() = entity is EntityProjectile

    val isVehicle: Boolean
        get() = entity is Entity

    val isItem: Boolean
        get() = entity is EntityItem

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
