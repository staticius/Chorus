package org.chorus.entity.mob.animal

import org.chorus.entity.EntityID
import org.chorus.entity.EntitySmite
import org.chorus.entity.EntityWalkable
import org.chorus.item.*
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

/**
 * @author PikyCZ
 */
class EntityZombieHorse(chunk: IChunk?, nbt: CompoundTag) : EntityHorse(chunk, nbt), EntityWalkable, EntitySmite {
    override fun getIdentifier(): String {
        return EntityID.Companion.ZOMBIE_HORSE
    }


    override fun getWidth(): Float {
        return 1.4f
    }

    override fun getHeight(): Float {
        return 1.6f
    }

    override fun initEntity() {
        this.maxHealth = 15
        super.initEntity()
    }

    override fun getDrops(): Array<Item> {
        return arrayOf(Item.get(Item.ROTTEN_FLESH, 1, 1))
    }

    override fun isUndead(): Boolean {
        return true
    }

    override fun getOriginalName(): String {
        return "Zombie Horse"
    }

    override fun onUpdate(currentTick: Int): Boolean {
        burn(this)
        return super.onUpdate(currentTick)
    }
}
