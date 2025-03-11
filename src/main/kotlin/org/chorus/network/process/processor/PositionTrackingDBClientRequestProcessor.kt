package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.PositionTrackingDBClientRequestPacket
import org.chorus.network.protocol.PositionTrackingDBServerBroadcastPacket
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.positiontracking.PositionTracking

import java.io.IOException


class PositionTrackingDBClientRequestProcessor : DataPacketProcessor<PositionTrackingDBClientRequestPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: PositionTrackingDBClientRequestPacket) {
        val player = playerHandle.player
        try {
            val positionTracking: PositionTracking =
                Server.instance.getPositionTrackingService().startTracking(player, pk.trackingId, true)
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
