package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.network.protocol.TickSyncPacket

class TickSyncProcessor : DataPacketProcessor<TickSyncPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: TickSyncPacket) {
        val tickSyncPacketToClient = TickSyncPacket()
        tickSyncPacketToClient.requestTimestamp = pk.requestTimestamp
        tickSyncPacketToClient.responseTimestamp = Server.instance.getTick().toLong()
        playerHandle.player.dataPacketImmediately(tickSyncPacketToClient)
    }

    override val packetId: Int
        get() = ProtocolInfo.TICK_SYNC_PACKET
}
