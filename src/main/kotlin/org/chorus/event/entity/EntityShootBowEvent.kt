package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.entity.EntityLiving
import org.chorus.entity.projectile.EntityProjectile
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item

class EntityShootBowEvent(shooter: EntityLiving, bow: Item, projectile: EntityProjectile, force: Double) :
    EntityEvent(), Cancellable {
    val bow: Item

    var projectile: EntityProjectile = projectile
        set(value) {
            if (value !== field) {
                if (field.viewers.isEmpty()) {
                    field.kill()
                    field.close()
                }
                field = projectile
            }
        }

    @JvmField
    var force: Double

    init {
        this.entity = shooter
        this.bow = bow
        this.projectile = projectile
        this.force = force
    }

    fun getProjectile(): EntityProjectile {
        return this.projectile
    }

    fun setProjectile(projectile: Entity) {
        if (projectile !== this.projectile) {
            if (this.projectile.viewers.isEmpty()) {
                this.projectile.kill()
                this.projectile.close()
            }
            this.projectile = projectile as EntityProjectile
        }
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
