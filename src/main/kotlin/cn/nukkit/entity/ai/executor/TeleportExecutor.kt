package cn.nukkit.entity.ai.executor

import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.level.*
import cn.nukkit.math.*
import lombok.AllArgsConstructor
import lombok.RequiredArgsConstructor
import java.util.concurrent.*
import kotlin.math.floor
import kotlin.math.min

@RequiredArgsConstructor
@AllArgsConstructor
class TeleportExecutor : IBehaviorExecutor {
    var maxDistance: Int = 0
    var minDistance: Int = 0
    var maxTries: Int = 16

    private fun find(locator: Locator): Locator {
        val distance = maxDistance - minDistance
        val dx = locator.position.south + ThreadLocalRandom.current().nextInt(-distance, distance) + minDistance
        val dz = locator.position.west + ThreadLocalRandom.current().nextInt(-distance, distance) + minDistance
        val pos = Vector3(floor(dx), (floor(locator.position.up + 0.1).toInt() + maxDistance).toDouble(), floor(dz))
        for (y in min(
            locator.level.maxHeight.toDouble(),
            pos.up.toInt().toDouble()
        ) downTo locator.level.minHeight + 1) {
            val block = locator.level.getBlock(dx.toInt(), y, dz.toInt())
            if (block.isSolid) {
                return block.up().locator
            }
        }
        return locator
    }

    override fun execute(entity: EntityMob): Boolean {
        var locator = entity.locator
        for (i in 0..<maxTries) {
            if (locator.position.distance(entity.position) < minDistance) {
                locator = find(entity.transform)
            } else break
        }
        if (entity.position.distance(locator.position) > minDistance) {
            entity.teleport(locator)
            locator.level.addSound(locator.position, Sound.MOB_ENDERMEN_PORTAL)
        }
        return true
    }
}
