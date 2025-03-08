package cn.nukkit.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.network.protocol.TickSyncPacket

class TickSyncProcessor : DataPacketProcessor<TickSyncPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: TickSyncPacket) {
        val tickSyncPacketToClient = TickSyncPacket()
        tickSyncPacketToClient.requestTimestamp = pk.requestTimestamp
        tickSyncPacketToClient.responseTimestamp = playerHandle.player.getServer().getTick().toLong()
        playerHandle.player.dataPacketImmediately(tickSyncPacketToClient)
    }

    override val packetId: Int
        get() = ProtocolInfo.TICK_SYNC_PACKET
}
