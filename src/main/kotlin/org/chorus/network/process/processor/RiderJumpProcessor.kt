package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.config.ServerProperties.get
import org.chorus.entity.Entity.isAlive
import org.chorus.entity.Entity.move
import org.chorus.entity.Entity.setDataFlag
import org.chorus.entity.data.EntityFlag
import org.chorus.entity.mob.animal.EntityHorse
import org.chorus.entity.mob.animal.EntityHorse.getClientMaxJumpHeight
import org.chorus.entity.mob.animal.EntityHorse.getJumping
import org.chorus.item.Item.Companion.get
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.network.protocol.RiderJumpPacket
import kotlin.math.max

class RiderJumpProcessor : DataPacketProcessor<RiderJumpPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: RiderJumpPacket) {
        if (playerHandle.player.riding is EntityHorse && riding.isAlive() && !riding.getJumping().get()) {
            val maxMotionY: Float = riding.getClientMaxJumpHeight()
            val motion = maxMotionY * (max(1.0, pk.jumpStrength.toDouble()) / 100.0)
            riding.getJumping().set(true)
            riding.move(0.0, motion, 0.0)
            //避免onGround不更新
            riding.motion.south = 0.0
            riding.motion.up = 0.0
            riding.motion.west = 0.0
            riding.setDataFlag(EntityFlag.STANDING)
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.RIDER_JUMP_PACKET
}
