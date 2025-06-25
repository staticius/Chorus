package org.chorus_oss.chorus.experimental.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.PositionTrackingDBClientRequestPacket
import org.chorus_oss.chorus.network.protocol.PositionTrackingDBServerBroadcastPacket
import org.chorus_oss.chorus.network.protocol.ProtocolInfo
import org.chorus_oss.chorus.utils.Loggable
import java.io.IOException


class PositionTrackingDBClientRequestProcessor : DataPacketProcessor<PositionTrackingDBClientRequestPacket>() {
    override fun handle(player: Player, pk: PositionTrackingDBClientRequestPacket) {
        val player = player.player
        try {
            val positionTracking =
                Server.instance.getPositionTrackingService().startTracking(player, pk.trackingId, true)
            if (positionTracking != null) {
                return
            }
        } catch (e: IOException) {
            log.warn(
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

    companion object : Loggable
}
