package cn.nukkit.entity.mob.animal

import cn.nukkit.entity.EntityID
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

open class EntityLlama(chunk: IChunk?, nbt: CompoundTag) : EntityAnimal(chunk, nbt) {
    override fun getIdentifier(): String {
        return EntityID.Companion.LLAMA
    }

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getHeight(): Float {
        return 1.9f
    }

    override fun getOriginalName(): String {
        return "Llama"
    }

    public override fun initEntity() {
        this.maxHealth = 20
        super.initEntity()
    }
}
