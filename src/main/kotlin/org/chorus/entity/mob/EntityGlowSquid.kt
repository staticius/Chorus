package org.chorus.entity.mob

import org.chorus.entity.EntityID
import org.chorus.entity.EntitySwimmable
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

class EntityGlowSquid(chunk: IChunk?, nbt: CompoundTag) : EntitySquid(chunk, nbt), EntitySwimmable {
    override fun getEntityIdentifier(): String {
        return EntityID.GLOW_SQUID
    }

    override fun getWidth(): Float {
        return 0.475f
    }

    override fun initEntity() {
        this.setMaxHealth(10)
        super.initEntity()
    }

    override fun getOriginalName(): String {
        return "GlowSquid"
    }
}
