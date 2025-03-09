package org.chorus.entity.mob.monster.humanoid_monster

import cn.nukkit.entity.*
import cn.nukkit.entity.mob.villagers.EntityVillager
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author PikyCZ
 */
abstract class EntityIllager(chunk: IChunk?, nbt: CompoundTag?) : EntityHumanoidMonster(chunk, nbt), EntityWalkable {
    override fun attackTarget(entity: Entity): Boolean {
        return when (entity.getIdentifier()) {
            EntityID.Companion.VILLAGER -> entity is EntityVillager && !entity.isBaby()
            EntityID.Companion.IRON_GOLEM, EntityID.Companion.WANDERING_TRADER -> true
            else -> super.attackTarget(entity)
        }
    }
}
