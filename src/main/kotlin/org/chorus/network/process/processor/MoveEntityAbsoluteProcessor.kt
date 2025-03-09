package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.entity.item.EntityBoat
import cn.nukkit.event.vehicle.VehicleMoveEvent
import cn.nukkit.math.Vector3
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.MoveEntityAbsolutePacket
import cn.nukkit.network.protocol.ProtocolInfo

class MoveEntityAbsoluteProcessor : DataPacketProcessor<MoveEntityAbsolutePacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: MoveEntityAbsolutePacket) {
        val player = playerHandle.player
        if (!player.isAlive() || !player.spawned || player.getRiding() == null) {
            return
        }
        val movedEntity = player.level!!.getEntity(pk.eid) as? EntityBoat ?: return

        val pos = Vector3(pk.x, pk.y - movedEntity.getBaseOffset(), pk.z)
        if (!movedEntity.equals(player.getRiding()) || !movedEntity.isControlling(player) || pos.distanceSquared(
                movedEntity.position
            ) > 10 * 10
        ) {
            movedEntity.moveDelta()
            return
        }

        val from = movedEntity.getTransform()
        movedEntity.setPositionAndRotation(pos, pk.headYaw, 0.0)
        val to = movedEntity.getTransform()
        if (from != to) {
            player.getServer().getPluginManager().callEvent(VehicleMoveEvent(player, from, to))
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.MOVE_ENTITY_ABSOLUTE_PACKET
}
