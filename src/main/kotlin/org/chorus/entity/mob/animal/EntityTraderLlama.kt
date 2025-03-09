package org.chorus.entity.mob.animal

import cn.nukkit.entity.EntityID
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

class EntityTraderLlama(chunk: IChunk?, nbt: CompoundTag) : EntityLlama(chunk, nbt) {
    override fun getIdentifier(): String {
        return EntityID.Companion.TRADER_LLAMA
    }

    override fun getOriginalName(): String {
        return "Trader Llama"
    }

    override fun initEntity() {
        this.maxHealth = 20
        super.initEntity()
    }
}
