package cn.nukkit.entity.projectile

import cn.nukkit.entity.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

class EntityBreezeWindCharge @JvmOverloads constructor(
    chunk: IChunk?,
    nbt: CompoundTag?,
    shootingEntity: Entity? = null
) :
    EntityWindCharge(chunk, nbt, shootingEntity) {
    override fun getIdentifier(): String {
        return EntityID.Companion.BREEZE_WIND_CHARGE_PROJECTILE
    }

    override fun getOriginalName(): String {
        return "Breeze Wind Charge Projectile"
    }

    override fun getBurstRadius(): Double {
        return 3.0
    }

    override fun getKnockbackStrength(): Double {
        return 0.18
    }
}
