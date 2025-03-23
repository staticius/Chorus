package org.chorus.entity.ai.sensor

import org.chorus.entity.*
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.mob.EntityMob

class EntityAttackedByOwnerSensor(override var period: Int, protected var changeTarget: Boolean) : ISensor {
    override fun sense(entity: EntityMob) {
        if (entity is EntityOwnable) {
            val player = entity.owner
            if (player != null) {
                val current = entity.getMemoryStorage().get<Entity>(CoreMemoryTypes.Companion.ENTITY_ATTACKED_BY_OWNER)
                if (!changeTarget) {
                    if (current != null && current.isAlive) return
                    else entity.getMemoryStorage().clear(CoreMemoryTypes.Companion.ENTITY_ATTACKED_BY_OWNER)
                }
                if (player.lastBeAttackEntity != null) {
                    entity.getMemoryStorage()
                        .set<Entity>(CoreMemoryTypes.Companion.ENTITY_ATTACKING_OWNER, player.lastBeAttackEntity)
                } else if (player.lastAttackEntity != null) {
                    entity.getMemoryStorage()
                        .set<Entity>(CoreMemoryTypes.Companion.ENTITY_ATTACKED_BY_OWNER, player.lastAttackEntity)
                } else entity.getMemoryStorage().clear(CoreMemoryTypes.Companion.ENTITY_ATTACKED_BY_OWNER)
            }
        }
    }
}
