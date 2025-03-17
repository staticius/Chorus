package org.chorus.entity.ai.executor.evocation

import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.monster.humanoid_monster.EntityEvocationIllager
import org.chorus.entity.mob.monster.humanoid_monster.EntityEvocationIllager.SPELL
import org.chorus.level.Transform
import kotlin.math.cos
import kotlin.math.sin

class FangCircleExecutor : FangLineExecutor() {
    override fun execute(entity: EntityMob): Boolean {
        val illager = entity as EntityEvocationIllager
        if (tick == CAST_DURATION) {
            entity.rotation.yaw = (entity.headYaw)
            summon(illager, SPAWN_RADIUS_INNER, SPAWN_COUNT_INNER)
        } else if (tick == CAST_DURATION + DELAY_BETWEEN) {
            summon(illager, SPAWN_RADIUS_OUTER, SPAWN_COUNT_OUTER)
        }
        tick++
        if (tick >= DURATION) {
            val tick = entity.level!!.tick
            entity.getMemoryStorage()!!.put<Int>(CoreMemoryTypes.Companion.LAST_ATTACK_CAST, tick)
            entity.getMemoryStorage()!!.put<Int>(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, tick)
            return false
        } else return true
    }

    private fun summon(origin: EntityEvocationIllager, size: Float, amount: Int) {
        val angleIncrement = 360.0 / amount
        for (i in 0..<amount) {
            val angle = Math.toRadians((i * angleIncrement) + origin.headYaw)
            val particleX = origin.vector3.x + cos(angle) * size
            val particleZ = origin.vector3.z + sin(angle) * size
            spawn(origin, Transform(particleX, origin.position.y, particleZ, angle, 0.0, origin.level!!))
        }
    }

    override fun startSpell(entity: EntityMob) {
        super.startSpell(entity)
        entity.memoryStorage!!.put<SPELL>(CoreMemoryTypes.Companion.LAST_MAGIC, SPELL.CAST_CIRLCE)
    }

    companion object {
        //Values represent ticks
        private const val CAST_DURATION = 40
        private const val DELAY_BETWEEN = 3
        private const val SPAWN_COUNT_INNER = 5
        private const val SPAWN_COUNT_OUTER = 8
        private const val SPAWN_RADIUS_INNER = 1.5f
        private const val SPAWN_RADIUS_OUTER = 2.5f


        private const val DURATION = CAST_DURATION + DELAY_BETWEEN + 1
    }
}
