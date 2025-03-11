package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponse
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseContainer
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseSlot
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseStatus






class ItemStackResponsePacket : DataPacket() {
    val entries: List<ItemStackResponse> = ArrayList()

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeArray(entries) { r: ItemStackResponse ->
            byteBuf.writeByte(r.result.ordinal().toByte().toInt())
            byteBuf.writeVarInt(r.requestId)
            if (r.result != ItemStackResponseStatus.OK) return@writeArray
            byteBuf.writeArray(
                r.containers
            ) { container: ItemStackResponseContainer ->
                byteBuf.writeFullContainerName(container.containerName)
                byteBuf.writeArray(
                    container.items
                ) { item: ItemStackResponseSlot ->
                    byteBuf.writeByte(item.slot.toByte().toInt())
                    byteBuf.writeByte(item.hotbarSlot.toByte().toInt())
                    byteBuf.writeByte(item.count.toByte().toInt())
                    byteBuf.writeVarInt(item.stackNetworkId)
                    byteBuf.writeString(item.customName)
                    byteBuf.writeString(item.filteredCustomName)
                    byteBuf.writeVarInt(item.durabilityCorrection)
                }
            }
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.ITEM_STACK_RESPONSE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
