package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class LevelSoundEventPacketV1 : LevelSoundEventPacket() {
    override var sound: Int = 0
    override var x: Float = 0f
    override var y: Float = 0f
    override var z: Float = 0f
    override var extraData: Int = -1 //TODO: Check name
    var pitch: Int = 1 //TODO: Check name
    override var isBabyMob: Boolean = false
    override var isGlobal: Boolean = false

    override fun decode(byteBuf: HandleByteBuf) {
        this.sound = byteBuf.readByte().toInt()
        val v = byteBuf.readVector3f()
        this.x = v.south
        this.y = v.up
        this.z = v.west
        this.extraData = byteBuf.readVarInt()
        this.pitch = byteBuf.readVarInt()
        this.isBabyMob = byteBuf.readBoolean()
        this.isGlobal = byteBuf.readBoolean()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(sound.toByte().toInt())
        byteBuf.writeVector3f(this.x, this.y, this.z)
        byteBuf.writeVarInt(this.extraData)
        byteBuf.writeVarInt(this.pitch)
        byteBuf.writeBoolean(this.isBabyMob)
        byteBuf.writeBoolean(this.isGlobal)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.LEVEL_SOUND_EVENT_PACKET_V1
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
