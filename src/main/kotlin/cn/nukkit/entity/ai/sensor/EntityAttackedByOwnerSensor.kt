package cn.nukkit.entity.ai.sensor

import cn.nukkit.entity.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.mob.EntityMob

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
                        .put<Entity>(CoreMemoryTypes.Companion.ENTITY_ATTACKING_OWNER, player.lastBeAttackEntity)
                } else if (player.lastAttackEntity != null) {
                    entity.getMemoryStorage()
                        .put<Entity>(CoreMemoryTypes.Companion.ENTITY_ATTACKED_BY_OWNER, player.lastAttackEntity)
                } else entity.getMemoryStorage().clear(CoreMemoryTypes.Companion.ENTITY_ATTACKED_BY_OWNER)
            }
        }
    }
}
