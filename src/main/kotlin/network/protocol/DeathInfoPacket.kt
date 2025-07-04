package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.lang.TranslationContainer
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class DeathInfoPacket : DataPacket() {
    @JvmField
    var translation: TranslationContainer? = null

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(translation!!.text)
        byteBuf.writeArray(
            translation!!.parameters
        ) { str: String? -> byteBuf.writeString(str!!) }
    }

    override fun pid(): Int {
        return ProtocolInfo.DEATH_INFO_PACKET
    }
}
