package cn.nukkit.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.Server
import cn.nukkit.level.Transform.Companion.fromObject
import cn.nukkit.math.Vector3
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.MovePlayerPacket
import cn.nukkit.network.protocol.ProtocolInfo

class MovePlayerProcessor : DataPacketProcessor<MovePlayerPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: MovePlayerPacket) {
        val player = playerHandle.player
        if (Server.getInstance().serverAuthoritativeMovement > 0) {
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
