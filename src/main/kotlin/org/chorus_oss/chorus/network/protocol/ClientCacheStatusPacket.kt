package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

data class ClientCacheStatusPacket(
    val isCacheSupported: Boolean
) : DataPacket() {
    override fun pid(): Int {
        return ProtocolInfo.CLIENT_CACHE_STATUS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<ClientCacheStatusPacket> {
        override fun decode(byteBuf: HandleByteBuf): ClientCacheStatusPacket {
            return ClientCacheStatusPacket(
                isCacheSupported = byteBuf.readBoolean()
            )
        }
    }
}
