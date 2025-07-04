package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

class SimulationTypePacket : DataPacket() {
    var type: SimulationType = SimulationType.GAME

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(type.ordinal.toByte().toInt())
    }

    enum class SimulationType {
        GAME,
        EDITOR,
        TEST
    }

    override fun pid(): Int {
        return ProtocolInfo.SIMULATION_TYPE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<SimulationTypePacket> {
        override fun decode(byteBuf: HandleByteBuf): SimulationTypePacket {
            val packet = SimulationTypePacket()

            packet.type = TYPES[byteBuf.readByte().toInt()]

            return packet
        }

        private val TYPES = SimulationType.entries.toTypedArray()
    }
}
