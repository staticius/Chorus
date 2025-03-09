package org.chorus.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.entity.EntityLiving
import cn.nukkit.entity.projectile.EntityProjectile
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item

/**
 * @author GoodLucky777, Superice666
 */
class EntityShootCrossbowEvent(shooter: EntityLiving?, crossbow: Item, vararg projectiles: EntityProjectile) :
    EntityEvent(), Cancellable {
    private val projectiles: Array<EntityProjectile>
    val crossbow: Item

    init {
        this.entity = shooter
        this.crossbow = crossbow
        this.projectiles = projectiles
    }

    override var entity: Entity?
        get() = entity as EntityLiving
        set(entity) {
            super.entity = entity
        }

    fun getProjectile(array: Int): EntityProjectile {
        return projectiles[array]
    }

    fun getProjectiles(): Array<EntityProjectile> {
        return this.projectiles
    }

    val projectilesCount: Int
        get() = projectiles.size

    fun setProjectile(projectile: EntityProjectile, array: Int) {
        if (projectile !== projectiles[array]) {
            if (projectiles[array].getViewers().isEmpty()) {
                projectiles[array].kill()
                projectiles[array].close()
            }
            projectiles[array] = projectile
        }
    }

    fun setProjectiles(projectiles: Array<EntityProjectile>) {
        for (i in this.projectiles.indices) {
            if (projectiles[i] !== this.projectiles[i]) {
                if (this.projectiles[i].getViewers().isEmpty()) {
                    this.projectiles[i].kill()
                    this.projectiles[i].close()
                }
                this.projectiles[i] = projectiles[i]
            }
        }
    }

    fun killProjectiles() {
        for (projectile in this.projectiles) {
            projectile.kill()
        }
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}