package org.chorus.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.entity.EntityLiving
import cn.nukkit.entity.projectile.EntityProjectile
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item

/**
 * @author Box (Nukkit Project)
 */
class EntityShootBowEvent(shooter: EntityLiving?, bow: Item, projectile: EntityProjectile, force: Double) :
    EntityEvent(), Cancellable {
    val bow: Item

    private var projectile: EntityProjectile

    @JvmField
    var force: Double

    init {
        this.entity = shooter
        this.bow = bow
        this.projectile = projectile
        this.force = force
    }

    override var entity: Entity?
        get() = entity as EntityLiving
        set(entity) {
            super.entity = entity
        }

    fun getProjectile(): EntityProjectile {
        return this.projectile
    }

    fun setProjectile(projectile: Entity) {
        if (projectile !== this.projectile) {
            if (this.projectile.getViewers().isEmpty()) {
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
