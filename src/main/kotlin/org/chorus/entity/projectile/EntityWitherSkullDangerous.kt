package org.chorus.entity.projectile

import org.chorus.entity.EntityID
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

class EntityWitherSkullDangerous(chunk: IChunk?, nbt: CompoundTag?) : EntityWitherSkull(chunk, nbt) {
    override fun getIdentifier(): String {
        return EntityID.Companion.WITHER_SKULL_DANGEROUS
    }

    override fun getStrength(): Float {
        return 1.5f
    }
}
