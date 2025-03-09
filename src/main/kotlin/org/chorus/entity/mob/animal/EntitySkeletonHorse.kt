package org.chorus.entity.mob.animal

import cn.nukkit.entity.EntityID
import cn.nukkit.entity.EntitySmite
import cn.nukkit.entity.EntityWalkable
import cn.nukkit.item.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author PikyCZ
 */
class EntitySkeletonHorse(chunk: IChunk?, nbt: CompoundTag) : EntityHorse(chunk, nbt), EntitySmite, EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.Companion.SKELETON_HORSE
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

    override fun getDrops(): Array<Item?> {
        return arrayOf(Item.get(Item.BONE))
    }

    override fun isUndead(): Boolean {
        return true
    }

    override fun getOriginalName(): String {
        return "Skeleton Horse"
    }

    override fun onUpdate(currentTick: Int): Boolean {
        burn(this)
        return super.onUpdate(currentTick)
    }
}
