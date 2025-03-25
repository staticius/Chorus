package org.chorus.network.protocol

import org.chorus.item.Item
import org.chorus.network.connection.util.HandleByteBuf

import java.util.*

class CraftingEventPacket : DataPacket() {
    var windowId: Int = 0
    var type: Int = 0
    var id: UUID? = null

    lateinit var input: Array<Item>
    lateinit var output: Array<Item>

    override fun decode(byteBuf: HandleByteBuf) {
        this.windowId = byteBuf.readByte().toInt()
        this.type = byteBuf.readVarInt()
        this.id = byteBuf.readUUID()

        this.input = byteBuf.readArray(
            Item::class.java
        ) { it.readSlot() }
        this.output = byteBuf.readArray(
            Item::class.java
        ) { it.readSlot() }
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte((windowId and 0xFF).toByte().toInt())
        byteBuf.writeVarInt(type)
        byteBuf.writeUUID(id!!)

        byteBuf.writeArray(input) { item: Item? -> byteBuf.writeSlot(item) }
        byteBuf.writeArray(output) { item: Item? -> byteBuf.writeSlot(item) }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CRAFTING_EVENT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val TYPE_INVENTORY: Int = 0
        const val TYPE_CRAFTING: Int = 1
        const val TYPE_WORKBENCH: Int = 2
    }
}
