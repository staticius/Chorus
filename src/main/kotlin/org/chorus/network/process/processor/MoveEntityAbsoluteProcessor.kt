package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.entity.item.EntityBoat
import org.chorus.event.vehicle.VehicleMoveEvent
import org.chorus.math.Vector3
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.MoveEntityAbsolutePacket
import org.chorus.network.protocol.ProtocolInfo

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
            Server.instance.pluginManager.callEvent(VehicleMoveEvent(player, from, to))
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.MOVE_ENTITY_ABSOLUTE_PACKET
}
