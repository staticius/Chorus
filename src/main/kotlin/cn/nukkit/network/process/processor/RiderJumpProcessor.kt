package cn.nukkit.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.config.ServerProperties.get
import cn.nukkit.entity.Entity.isAlive
import cn.nukkit.entity.Entity.move
import cn.nukkit.entity.Entity.setDataFlag
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.animal.EntityHorse
import cn.nukkit.entity.mob.animal.EntityHorse.getClientMaxJumpHeight
import cn.nukkit.entity.mob.animal.EntityHorse.getJumping
import cn.nukkit.item.Item.Companion.get
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.network.protocol.RiderJumpPacket
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
