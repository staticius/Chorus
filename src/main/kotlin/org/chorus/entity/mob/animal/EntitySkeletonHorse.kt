package org.chorus.entity.mob.animal

import org.chorus.entity.EntityID
import org.chorus.entity.EntitySmite
import org.chorus.entity.EntityWalkable
import org.chorus.item.Item
import org.chorus.item.ItemID
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

class EntitySkeletonHorse(chunk: IChunk?, nbt: CompoundTag) : EntityHorse(chunk, nbt), EntitySmite, EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.SKELETON_HORSE
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
        return arrayOf(Item.get(ItemID.BONE))
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
