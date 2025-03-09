package org.chorus.entity.ai.executor

import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.EntityShulker
import org.chorus.level.Sound
import org.chorus.utils.Utils

class ShulkerIdleExecutor : IBehaviorExecutor {
    private var stayTicks = 0
    private var tick = 0

    override fun execute(entity: EntityMob?): Boolean {
        tick++
        if (tick >= stayTicks) return false
        return true
    }

    override fun onStart(entity: EntityMob) {
        tick = 0
        stayTicks = Utils.rand(20, 61)
        if (entity is EntityShulker) {
            entity.setPeeking(30)
            entity.level!!.addSound(entity.position, Sound.MOB_SHULKER_OPEN)
        }
    }

    override fun onStop(entity: EntityMob) {
        if (entity is EntityShulker) {
            entity.setPeeking(0)
            entity.level!!.addSound(entity.position, Sound.MOB_SHULKER_CLOSE)
        }
    }

    override fun onInterrupt(entity: EntityMob) {
        onStop(entity)
    }
}
