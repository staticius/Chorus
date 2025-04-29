package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.PlayerHandle
import org.chorus_oss.chorus.entity.data.EntityFlag
import org.chorus_oss.chorus.entity.mob.animal.EntityHorse
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.RiderJumpPacket
import kotlin.math.max

class RiderJumpProcessor : DataPacketProcessor<RiderJumpPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: RiderJumpPacket) {
        val riding = playerHandle.player.riding
        if (riding is EntityHorse && riding.isAlive() && !riding.getJumping().get()) {
            val maxMotionY: Float = riding.getClientMaxJumpHeight()
            val motion = maxMotionY * (max(1.0, pk.jumpStrength.toDouble()) / 100.0)
            riding.getJumping().set(true)
            riding.move(0.0, motion, 0.0)
            //避免onGround不更新
            riding.motion.x = 0.0
            riding.motion.y = 0.0
            riding.motion.z = 0.0
            riding.setDataFlag(EntityFlag.STANDING)
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.RIDER_JUMP_PACKET
}
