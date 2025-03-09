package org.chorus.entity.mob

import cn.nukkit.entity.EntityID
import cn.nukkit.entity.EntitySwimmable
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

class EntityGlowSquid(chunk: IChunk?, nbt: CompoundTag) : EntitySquid(chunk, nbt), EntitySwimmable {
    override fun getIdentifier(): String {
        return EntityID.Companion.GLOW_SQUID
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
