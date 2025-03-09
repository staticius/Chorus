package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.PositionTrackingDBClientRequestPacket
import cn.nukkit.network.protocol.PositionTrackingDBServerBroadcastPacket
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.positiontracking.PositionTracking
import lombok.extern.slf4j.Slf4j
import java.io.IOException

@Slf4j
class PositionTrackingDBClientRequestProcessor : DataPacketProcessor<PositionTrackingDBClientRequestPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: PositionTrackingDBClientRequestPacket) {
        val player = playerHandle.player
        try {
            val positionTracking: PositionTracking =
                player.getServer().getPositionTrackingService().startTracking(player, pk.trackingId, true)
            if (positionTracking != null) {
                return
            }
        } catch (e: IOException) {
            PositionTrackingDBClientRequestProcessor.log.warn(
                "Failed to track the trackingHandler {}",
                pk.trackingId,
                e
            )
        }
        val notFound = PositionTrackingDBServerBroadcastPacket()
        notFound.action = PositionTrackingDBServerBroadcastPacket.Action.NOT_FOUND
        notFound.trackingId = pk.trackingId
        player.dataPacket(notFound)
    }

    override val packetId: Int
        get() = ProtocolInfo.POS_TRACKING_CLIENT_REQUEST_PACKET
}
