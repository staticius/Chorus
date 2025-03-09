package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*






class CompletedUsingItemPacket : DataPacket() {
    @JvmField
    var itemId: Int = 0
    @JvmField
    var action: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeShortLE(itemId)
        byteBuf.writeIntLE(action)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.COMPLETED_USING_ITEM_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val ACTION_UNKNOWN: Int = -1
        const val ACTION_EQUIP_ARMOR: Int = 0
        const val ACTION_EAT: Int = 1
        const val ACTION_ATTACK: Int = 2
        const val ACTION_CONSUME: Int = 3
        const val ACTION_THROW: Int = 4
        const val ACTION_SHOOT: Int = 5
        const val ACTION_PLACE: Int = 6
        const val ACTION_FILL_BOTTLE: Int = 7
        const val ACTION_FILL_BUCKET: Int = 8
        const val ACTION_POUR_BUCKET: Int = 9
        const val ACTION_USE_TOOL: Int = 10
        const val ACTION_INTERACT: Int = 11
        const val ACTION_RETRIEVE: Int = 12
        const val ACTION_DYED: Int = 13
        const val ACTION_TRADED: Int = 14
        const val ACTION_BRUSHING_COMPLETED: Int = 15
    }
}
