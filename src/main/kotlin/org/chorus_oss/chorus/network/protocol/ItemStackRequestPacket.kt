package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.ItemStackRequest


class ItemStackRequestPacket : DataPacket() {
    val requests: MutableList<ItemStackRequest> = ArrayList()

    override fun pid(): Int {
        return ProtocolInfo.ITEM_STACK_REQUEST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<ItemStackRequestPacket> {
        override fun decode(byteBuf: HandleByteBuf): ItemStackRequestPacket {
            val packet = ItemStackRequestPacket()

            packet.requests.addAll(
                byteBuf.readArray(
                    ItemStackRequest::class.java
                ) { it.readItemStackRequest() }
            )

            return packet
        }
    }
}
