package org.chorus.entity.mob.water_animal.fish

import org.chorus.entity.EntityID
import org.chorus.entity.EntitySwimmable
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

class EntityPufferfish(chunk: IChunk?, nbt: CompoundTag) : EntityFish(chunk, nbt), EntitySwimmable {
    override fun getIdentifier(): String {
        return EntityID.PUFFERFISH
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
