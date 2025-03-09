package org.chorus.entity.ai.sensor

import org.chorus.Player
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.animal.EntityAnimal

open class NearestFeedingPlayerSensor @JvmOverloads constructor(
    protected var range: Double,
    protected var minRange: Double,
    override var period: Int = 1
) :
    ISensor {
    override fun sense(entity: EntityMob) {
        if (entity is EntityAnimal) {
            var player: Player? = null
            val rangeSquared = this.range * this.range
            val minRangeSquared = this.minRange * this.minRange
            //寻找范围内最近满足乞食要求的玩家
            for (p in entity.level!!.players.values) {
                if (entity.position.distanceSquared(p.position) <= rangeSquared && entity.position.distanceSquared(p.position) >= minRangeSquared && entity.isBreedingItem(
                        p.inventory.itemInHand
                    )
                ) {
                    if (player == null) {
                        player = p
                    } else {
                        if (entity.position.distanceSquared(p.position) < entity.position.distanceSquared(player.position)) {
                            player = p
                        }
                    }
                }
            }
            entity.getMemoryStorage()!!.put<Player>(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER, player)
            return
        }
        entity.memoryStorage!!.clear(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER)
    }
}
