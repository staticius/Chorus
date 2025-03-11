package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.hud.HudElement
import org.chorus.network.protocol.types.hud.HudVisibility
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet







class SetHudPacket : DataPacket() {
    val elements: MutableSet<HudElement> = ObjectOpenHashSet()
    var visibility: HudVisibility? = null

    override fun decode(byteBuf: HandleByteBuf) {
        elements.clear()
        byteBuf.readArray(
            this.elements
        ) { value: HandleByteBuf? -> HudElement.entries[byteBuf.readUnsignedVarInt()] }
        this.visibility = HudVisibility.entries[byteBuf.readByte().toInt()]
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeArray(
            this.elements
        ) { buf: HandleByteBuf?, element: HudElement -> byteBuf.writeUnsignedVarInt(element.ordinal) }
        byteBuf.writeByte(visibility!!.ordinal.toByte().toInt())
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_HUD
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
