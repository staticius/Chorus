package org.chorus.entity.mob

import org.chorus.entity.EntityID
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

class EntityWanderingTrader(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt) {
    override fun getIdentifier(): String {
        return EntityID.WANDERING_TRADER
    }

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getHeight(): Float {
        return 1.9f
    }

    override fun getOriginalName(): String {
        return "Wandering Trader"
    }

    public override fun initEntity() {
        this.setMaxHealth(20)
        super.initEntity()
    }

    override fun getExperienceDrops(): Int {
        return 0
    }
}
