package cn.nukkit.entity.projectile.abstract_arrow

import cn.nukkit.entity.Entity
import cn.nukkit.entity.projectile.SlenderProjectile
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

abstract class EntityAbstractArrow @JvmOverloads constructor(
    chunk: IChunk?,
    nbt: CompoundTag,
    shootingEntity: Entity? = null
) :
    SlenderProjectile(chunk, nbt, shootingEntity) {
    var isCreative: Boolean = false
    var OwnerID: Long = -1L
    var player: Boolean = false

    init {
        this.isCreative = nbt.getBoolean(TAG_IS_CREATIVE)
        this.OwnerID = nbt.getLong(TAG_OWNER_ID)
        this.player = nbt.getBoolean(TAG_PLAYER)
    }

    companion object {
        const val TAG_IS_CREATIVE: String = "isCreative"
        const val TAG_OWNER_ID: String = "OwnerID"
        const val TAG_PLAYER: String = "player"
    }
}
