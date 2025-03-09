package org.chorus.entity.mob.water_animal.fish

import cn.nukkit.entity.EntityID
import cn.nukkit.entity.EntitySwimmable
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author PetteriM1
 */
class EntityPufferfish(chunk: IChunk?, nbt: CompoundTag) : EntityFish(chunk, nbt), EntitySwimmable {
    override fun getIdentifier(): String {
        return EntityID.Companion.PUFFERFISH
    }

    override fun getOriginalName(): String {
        return "Pufferfish"
    }

    override fun getWidth(): Float {
        return 0.8f
    }

    override fun getHeight(): Float {
        return 0.8f
    }

    public override fun initEntity() {
        this.maxHealth = 3
        super.initEntity()
    }
}
