package org.chorus.entity.mob.animal

import org.chorus.entity.EntityID
import org.chorus.entity.EntityWalkable
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import java.util.concurrent.*

/**
 * @author Erik Miller | EinBexiii
 */
class EntityStrider(chunk: IChunk?, nbt: CompoundTag) : EntityAnimal(chunk, nbt), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.Companion.STRIDER
    }

    override fun initEntity() {
        this.maxHealth = 20
        super.initEntity()
    }

    override fun getWidth(): Float {
        return 0.9f
    }

    override fun getHeight(): Float {
        return 1.7f
    }

    override fun getFrostbiteInjury(): Int {
        return 5
    }

    override fun getOriginalName(): String {
        return "Strider"
    }

    override fun getExperienceDrops(): Int {
        return ThreadLocalRandom.current().nextInt(2) + 1
    }
}
