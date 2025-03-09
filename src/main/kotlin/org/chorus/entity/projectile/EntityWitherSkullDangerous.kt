package org.chorus.entity.projectile

import cn.nukkit.entity.EntityID
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

class EntityWitherSkullDangerous(chunk: IChunk?, nbt: CompoundTag?) : EntityWitherSkull(chunk, nbt) {
    override fun getIdentifier(): String {
        return EntityID.Companion.WITHER_SKULL_DANGEROUS
    }

    override fun getStrength(): Float {
        return 1.5f
    }
}
