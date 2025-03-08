package cn.nukkit.entity.mob

import cn.nukkit.entity.EntityID
import cn.nukkit.entity.EntityWalkable
import cn.nukkit.entity.mob.animal.EntityAnimal
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author PikyCZ
 */
class EntityLlamaSpit(chunk: IChunk?, nbt: CompoundTag) : EntityAnimal(chunk, nbt), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.Companion.LLAMA_SPIT
    }

    override fun getWidth(): Float {
        if (this.isBaby()) {
            return 0.45f
        }
        return 0.9f
    }

    override fun getHeight(): Float {
        if (this.isBaby()) {
            return 0.935f
        }
        return 1.87f
    }

    override fun getEyeHeight(): Float {
        if (this.isBaby()) {
            return 0.65f
        }
        return 1.2f
    }

    public override fun initEntity() {
        this.setMaxHealth(15)
        super.initEntity()
    }

    override fun getOriginalName(): String {
        return "Llama"
    }
}
