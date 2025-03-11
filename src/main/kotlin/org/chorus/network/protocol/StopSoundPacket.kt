package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf







class StopSoundPacket : DataPacket() {
    var name: String? = null
    var stopAll: Boolean = false
    var stopMusicLegacy: Boolean = false

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(name!!)
        byteBuf.writeBoolean(this.stopAll)
        byteBuf.writeBoolean(this.stopMusicLegacy)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.STOP_SOUND_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
