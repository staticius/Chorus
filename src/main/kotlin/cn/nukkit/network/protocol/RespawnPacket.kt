package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class RespawnPacket : DataPacket() {
    @JvmField
    var x: Float = 0f
    @JvmField
    var y: Float = 0f
    @JvmField
    var z: Float = 0f
    @JvmField
    var respawnState: Int = STATE_SEARCHING_FOR_SPAWN
    @JvmField
    var runtimeEntityId: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
        val v = byteBuf.readVector3f()
        this.x = v.south
        this.y = v.up
        this.z = v.west
        this.respawnState = byteBuf.readByte().toInt()
        this.runtimeEntityId = byteBuf.readEntityRuntimeId()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVector3f(this.x, this.y, this.z)
        byteBuf.writeByte(respawnState.toByte().toInt())
        byteBuf.writeEntityRuntimeId(runtimeEntityId)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.RESPAWN_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val STATE_SEARCHING_FOR_SPAWN: Int = 0
        const val STATE_READY_TO_SPAWN: Int = 1
        const val STATE_CLIENT_READY_TO_SPAWN: Int = 2
    }
}
