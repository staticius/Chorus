package cn.nukkit.entity.projectile.throwable

import cn.nukkit.entity.Entity
import cn.nukkit.entity.projectile.EntityProjectile
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

abstract class EntityThrowable @JvmOverloads constructor(
    chunk: IChunk?,
    nbt: CompoundTag,
    shootingEntity: Entity? = null
) :
    EntityProjectile(chunk, nbt, shootingEntity) {
    var inGround: Boolean = false
    var ownerId: Long = -1L
    var shake: Boolean = false

    init {
        this.inGround = nbt.getBoolean(TAG_IN_GROUND)
        this.ownerId = nbt.getLong(TAG_OWNER_ID)
        this.shake = nbt.getBoolean(TAG_SHAKE)
    }

    companion object {
        const val TAG_IN_GROUND: String = "InGround"
        const val TAG_OWNER_ID: String = "OwnerID"
        const val TAG_SHAKE: String = "shake"
    }
}
