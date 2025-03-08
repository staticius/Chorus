package cn.nukkit.entity.mob.animal

import cn.nukkit.entity.EntityFlyable
import cn.nukkit.entity.EntityID
import cn.nukkit.item.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author PikyCZ
 */
class EntityParrot(chunk: IChunk?, nbt: CompoundTag) : EntityAnimal(chunk, nbt), EntityFlyable {
    override fun getIdentifier(): String {
        return EntityID.Companion.PARROT
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

    override fun getDrops(): Array<Item?> {
        return arrayOf(Item.get(Item.FEATHER))
    }
}
