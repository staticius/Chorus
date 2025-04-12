package org.chorus.network.protocol

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.hud.HudElement
import org.chorus.network.protocol.types.hud.HudVisibility

class SetHudPacket : DataPacket() {
    val elements: MutableSet<HudElement> = ObjectOpenHashSet()
    var visibility: HudVisibility? = null

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeArray(this.elements) { buf, element ->
            buf.writeUnsignedVarInt(element.ordinal)
        }
        byteBuf.writeByte(visibility!!.ordinal)
    }

    override fun pid(): Int {
        return ProtocolInfo.SET_HUD
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<SetHudPacket> {
        override fun decode(byteBuf: HandleByteBuf): SetHudPacket {
            val packet = SetHudPacket()

            packet.elements.clear()
            byteBuf.readArray(packet.elements) {
                HudElement.entries[it.readUnsignedVarInt()]
            }
            packet.visibility = HudVisibility.entries[byteBuf.readByte().toInt()]

            return packet
        }
    }
}
