package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

data class AvailableActorIdentifiersPacket(
    val tag: ByteArray,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBytes(tag)
    }

    override fun pid(): Int {
        return ProtocolInfo.AVAILABLE_ACTOR_IDENTIFIERS_PACKET
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AvailableActorIdentifiersPacket

        return tag.contentEquals(other.tag)
    }

    override fun hashCode(): Int {
        return tag.contentHashCode()
    }
}


