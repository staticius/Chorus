package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf







class RemoveVolumeEntityPacket : DataPacket() {
    var id: Long = 0

    /**
     * @since v503
     */
    var dimension: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        id = byteBuf.readUnsignedVarInt().toLong()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(id.toInt())
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.REMOVE_VOLUME_ENTITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
