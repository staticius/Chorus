package org.chorus.network.protocol

import org.chorus.lang.TranslationContainer
import org.chorus.network.connection.util.HandleByteBuf







class DeathInfoPacket : DataPacket() {
    @JvmField
    var translation: TranslationContainer? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(translation!!.text)
        byteBuf.writeArray(
            translation!!.parameters
        ) { str: String? -> byteBuf.writeString(str!!) }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.DEATH_INFO_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
