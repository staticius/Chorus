package cn.nukkit.entity.ai.sensor

import cn.nukkit.Player
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.mob.EntityMob
import lombok.Getter
import kotlin.math.abs

@Getter
open class PlayerStaringSensor(
    protected var range: Double,
    protected var triggerDiff: Double,
    protected var ignoreRotation: Boolean
) :
    ISensor {
    override fun sense(entity: EntityMob) {
        for (player in entity.viewers.values) {
            if (player.position.distance(entity.position) <= range) {
                if (ignoreRotation || abs(abs(player.headYaw - entity.headYaw) - 180) <= this.triggerDiff) {
                    if (player.isLookingAt(
                            entity.position.add(0.0, entity.eyeHeight.toDouble(), 0.0),
                            triggerDiff,
                            true
                        )
                    ) {
                        entity.memoryStorage!!
                            .put<Player>(CoreMemoryTypes.Companion.STARING_PLAYER, player)
                        return
                    }
                }
            }
        }
        entity.memoryStorage!!.clear(CoreMemoryTypes.Companion.STARING_PLAYER)
    }
}
