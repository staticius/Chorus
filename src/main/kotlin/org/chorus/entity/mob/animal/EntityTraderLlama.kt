package org.chorus.entity.mob.animal

import org.chorus.entity.EntityID
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

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
