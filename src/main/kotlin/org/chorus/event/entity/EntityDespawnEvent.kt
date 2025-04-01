package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.entity.EntityCreature
import org.chorus.entity.EntityHuman
import org.chorus.entity.item.EntityItem
import org.chorus.entity.projectile.EntityProjectile
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.level.Locator


class EntityDespawnEvent(entity: Entity) : EntityEvent(), Cancellable {
    val type: Int

    init {
        this.entity = entity
        this.type = entity.getNetworkID()
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
