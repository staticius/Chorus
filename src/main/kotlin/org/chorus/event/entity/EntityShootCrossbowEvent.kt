package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.entity.EntityLiving
import org.chorus.entity.projectile.EntityProjectile
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item

class EntityShootCrossbowEvent(shooter: EntityLiving, val crossbow: Item, vararg projectiles: EntityProjectile) :
    EntityEvent(), Cancellable {
    private val projectiles: Array<EntityProjectile> = projectiles.toList().toTypedArray()

    override var entity: Entity = shooter

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