package org.chorus_oss.chorus.network.chorus

import org.chorus_oss.protocol.core.ProtoCodec

data class PacketHeader(
    val packetID: Short,
    val senderSubClientID: Byte,
    val targetSubClientID: Byte,
) {
    companion object : ProtoCodec<PacketHeader> {
        override fun serialize(value: PacketHeader, stream: kotlinx.io.Sink) {
            TODO("Not yet implemented")
        }

        override fun deserialize(stream: kotlinx.io.Source): PacketHeader {
            TODO("Not yet implemented")
        }
    }
}
