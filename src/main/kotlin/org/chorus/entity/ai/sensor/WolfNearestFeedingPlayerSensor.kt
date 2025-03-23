package org.chorus.entity.ai.sensor

import org.chorus.Player
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.animal.EntityAnimal
import org.chorus.item.*

//TODO: 需要进一步抽象
/**
 * 搜索狼最近携带食物的玩家,与[NearestFeedingPlayerSensor]相比它特判了Bone.
 *
 *
 * Search for wolves carrying food to the nearest player, compared to [NearestFeedingPlayerSensor], which is specially awarded to Bone.
 */
class WolfNearestFeedingPlayerSensor @JvmOverloads constructor(range: Double, minRange: Double, period: Int = 1) :
    NearestFeedingPlayerSensor(range, minRange, period) {
    override fun sense(entity: EntityMob) {
        if (entity is EntityAnimal) {
            var player: Player? = null
            val rangeSquared = this.range * this.range
            val minRangeSquared = this.minRange * this.minRange
            //寻找范围内最近满足乞食要求的玩家
            for (p in entity.level!!.players.values) {
                if (entity.position.distanceSquared(p.position) <= rangeSquared && entity.position.distanceSquared(p.position) >= minRangeSquared && (p.inventory.itemInHand.id === Item.BONE || entity.isBreedingItem(
                        p.inventory.itemInHand
                    ))
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
            entity.getMemoryStorage()!!.set<Player>(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER, player)
            return
        }
        entity.memoryStorage!!.clear(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER)
    }
}
