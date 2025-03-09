package org.chorus.entity.mob.monster.humanoid_monster

import org.chorus.entity.EntityID
import org.chorus.entity.EntitySmite
import org.chorus.entity.EntityWalkable
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

/**
 * @author PikyCZ
 */
class EntityStray(chunk: IChunk?, nbt: CompoundTag?) : EntitySkeleton(chunk, nbt), EntityWalkable, EntitySmite {
    override fun getIdentifier(): String {
        return EntityID.Companion.STRAY
    }

    override fun initEntity() {
        this.maxHealth = 20
        super.initEntity()
    }

    override fun getOriginalName(): String {
        return "Stray"
    }

    override fun onUpdate(currentTick: Int): Boolean {
        burn(this)
        return super.onUpdate(currentTick)
    }
}
