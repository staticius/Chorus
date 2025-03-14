package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


/**
 * @since 15-10-14
 */


class TakeItemEntityPacket : DataPacket() {
    @JvmField
    var entityId: Long = 0

    @JvmField
    var target: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.target = byteBuf.readEntityRuntimeId()
        this.entityId = byteBuf.readEntityRuntimeId()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityRuntimeId(this.target)
        byteBuf.writeEntityRuntimeId(this.entityId)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.TAKE_ITEM_ENTITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
