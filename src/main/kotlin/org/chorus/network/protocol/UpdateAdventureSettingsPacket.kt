package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*






class UpdateAdventureSettingsPacket : DataPacket() {
    @JvmField
    var noPvM: Boolean = false
    @JvmField
    var noMvP: Boolean = false
    @JvmField
    var immutableWorld: Boolean = false
    @JvmField
    var showNameTags: Boolean = false
    @JvmField
    var autoJump: Boolean = false

    override fun decode(byteBuf: HandleByteBuf) {
        throw UnsupportedOperationException()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBoolean(noPvM)
        byteBuf.writeBoolean(noMvP)
        byteBuf.writeBoolean(immutableWorld)
        byteBuf.writeBoolean(showNameTags)
        byteBuf.writeBoolean(autoJump)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.UPDATE_ADVENTURE_SETTINGS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
