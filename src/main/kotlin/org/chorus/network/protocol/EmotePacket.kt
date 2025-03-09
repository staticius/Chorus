package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*






class EmotePacket : DataPacket() {
    var runtimeId: Long = 0
    var xuid: String = ""
    var platformId: String = ""
    var emoteID: String? = null
    var flags: Byte = 0
    var emoteDuration: Int = 0

    override fun pid(): Int {
        return ProtocolInfo.Companion.EMOTE_PACKET
    }

    override fun decode(byteBuf: HandleByteBuf) {
        this.runtimeId = byteBuf.readEntityRuntimeId()
        this.emoteID = byteBuf.readString()
        this.emoteDuration = byteBuf.readUnsignedVarInt()
        this.xuid = byteBuf.readString()
        this.platformId = byteBuf.readString()
        this.flags = byteBuf.readByte()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityRuntimeId(this.runtimeId)
        byteBuf.writeString(emoteID!!)
        byteBuf.writeUnsignedVarInt(this.emoteDuration)
        byteBuf.writeString(this.xuid)
        byteBuf.writeString(this.platformId)
        byteBuf.writeByte(flags.toInt())
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
