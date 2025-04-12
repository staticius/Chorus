package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.PacketCompressionAlgorithm


class NetworkSettingsPacket : DataPacket() {
    var compressionThreshold: Int = 0
    var compressionAlgorithm: PacketCompressionAlgorithm? = null
    var clientThrottleEnabled: Boolean = false
    var clientThrottleThreshold: Byte = 0
    var clientThrottleScalar: Float = 0f

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeShortLE(this.compressionThreshold)
        byteBuf.writeShortLE(compressionAlgorithm!!.ordinal)
        byteBuf.writeBoolean(this.clientThrottleEnabled)
        byteBuf.writeByte(clientThrottleThreshold.toInt())
        byteBuf.writeFloatLE(this.clientThrottleScalar)
    }

    override fun pid(): Int {
        return ProtocolInfo.NETWORK_SETTINGS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<NetworkSettingsPacket> {
        override fun decode(byteBuf: HandleByteBuf): NetworkSettingsPacket {
            val packet = NetworkSettingsPacket()

            packet.compressionThreshold = byteBuf.readShortLE().toInt()
            packet.compressionAlgorithm = PacketCompressionAlgorithm.entries[byteBuf.readShortLE().toInt()]
            packet.clientThrottleEnabled = byteBuf.readBoolean()
            packet.clientThrottleThreshold = byteBuf.readByte()
            packet.clientThrottleScalar = byteBuf.readFloatLE()

            return packet
        }
    }
}
