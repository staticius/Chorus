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

    override fun decode(byteBuf: HandleByteBuf) {
        this.compressionThreshold = byteBuf.readShortLE().toInt()
        this.compressionAlgorithm = PacketCompressionAlgorithm.entries[byteBuf.readShortLE().toInt()]
        this.clientThrottleEnabled = byteBuf.readBoolean()
        this.clientThrottleThreshold = byteBuf.readByte()
        this.clientThrottleScalar = byteBuf.readFloatLE()
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.NETWORK_SETTINGS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
