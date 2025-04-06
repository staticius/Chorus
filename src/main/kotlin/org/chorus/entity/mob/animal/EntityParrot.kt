package org.chorus.entity.mob.animal

import org.chorus.entity.EntityFlyable
import org.chorus.entity.EntityID
import org.chorus.item.*
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

/**
 * @author PikyCZ
 */
class EntityParrot(chunk: IChunk?, nbt: CompoundTag) : EntityAnimal(chunk, nbt), EntityFlyable {
    override fun getIdentifier(): String {
        return EntityID.PARROT
    }


    override fun getOriginalName(): String {
        return "Parrot"
    }

    override fun getWidth(): Float {
        return 0.5f
    }

    override fun getHeight(): Float {
        return 1.0f
    }

    public override fun initEntity() {
        this.maxHealth = 6
        super.initEntity()
    }

    override fun getDrops(): Array<Item> {
        return arrayOf(Item.get(ItemID.FEATHER))
    }
}
