package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.level.Transform.Companion.fromObject
import org.chorus.math.Vector3
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.MovePlayerPacket
import org.chorus.network.protocol.ProtocolInfo

class MovePlayerProcessor : DataPacketProcessor<MovePlayerPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: MovePlayerPacket) {
        val player = playerHandle.player
        if (Server.instance.serverAuthoritativeMovement > 0) {
            return
        }
        val newPos = Vector3(pk.x.toDouble(), (pk.y - playerHandle.baseOffset).toDouble(), pk.z.toDouble())

        pk.yaw %= 360f
        pk.headYaw %= 360f
        pk.pitch %= 360f
        if (pk.yaw < 0) {
            pk.yaw += 360f
        }
        if (pk.headYaw < 0) {
            pk.headYaw += 360f
        }
        playerHandle.offerMovementTask(
            fromObject(
                newPos,
                player.level!!, pk.yaw.toDouble(), pk.pitch.toDouble(), pk.headYaw.toDouble()
            )
        )
    }

    override val packetId: Int
        get() = ProtocolInfo.MOVE_PLAYER_PACKET
}
