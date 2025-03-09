package org.chorus.entity.ai.executor

import org.chorus.entity.mob.EntityMob
import org.chorus.level.Sound
import lombok.AllArgsConstructor
import java.util.concurrent.ThreadLocalRandom


class PlaySoundExecutor(private val sound: Sound) : IBehaviorExecutor {
    var minPitch: Float = 0.8f
    var maxPitch: Float = 1.2f
    var minVolume: Float = 1f
    var maxVolume: Float = 1f


    override fun execute(entity: EntityMob): Boolean {
        val volume =
            if (minVolume == maxVolume) minVolume else ThreadLocalRandom.current().nextFloat(minVolume, maxVolume)
        val pitch = if (minPitch == maxPitch) minPitch else ThreadLocalRandom.current().nextFloat(minPitch, maxPitch)
        entity.locator.getLevel().addSound(entity.position, sound, volume, pitch)
        return false
    }
}
