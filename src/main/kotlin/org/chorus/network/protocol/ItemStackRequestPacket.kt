package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.itemstack.request.ItemStackRequest

import java.util.List
import java.util.function.Function





class ItemStackRequestPacket : DataPacket() {
    val requests: MutableList<ItemStackRequest> = ArrayList()

    override fun decode(byteBuf: HandleByteBuf) {
        requests.addAll(
            List.of(
                *byteBuf.readArray<ItemStackRequest>(
                    ItemStackRequest::class.java,
                    Function { obj: HandleByteBuf -> obj.readItemStackRequest() })
            )
        )
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.ITEM_STACK_REQUEST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
