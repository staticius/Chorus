package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.ServerboundLoadingScreenPacketType


class ServerboundLoadingScreenPacket : DataPacket() {
    private var type: ServerboundLoadingScreenPacketType? = null

    /**
     * Optional int, not present if null
     */
    private var loadingScreenId: Int? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.type = ServerboundLoadingScreenPacketType.entries[byteBuf.readVarInt()]
        if (byteBuf.readBoolean()) {
            this.loadingScreenId = byteBuf.readIntLE()
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SERVERBOUND_LOADING_SCREEN_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
