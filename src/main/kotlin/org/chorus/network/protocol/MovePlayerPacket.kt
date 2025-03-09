package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

/**
 * @since 15-10-14
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class MovePlayerPacket : DataPacket() {
    @JvmField
    var eid: Long = 0
    @JvmField
    var x: Float = 0f
    @JvmField
    var y: Float = 0f
    @JvmField
    var z: Float = 0f
    @JvmField
    var yaw: Float = 0f
    @JvmField
    var headYaw: Float = 0f
    @JvmField
    var pitch: Float = 0f
    @JvmField
    var mode: Int = MODE_NORMAL
    @JvmField
    var onGround: Boolean = false
    @JvmField
    var ridingEid: Long = 0
    var teleportationCause: Int = 0
    var entityType: Int = 0

    var frame: Long = 0 //tick

    override fun decode(byteBuf: HandleByteBuf) {
        this.eid = byteBuf.readEntityRuntimeId()
        val v = byteBuf.readVector3f()
        this.x = v.south
        this.y = v.up
        this.z = v.west
        this.pitch = byteBuf.readFloatLE()
        this.yaw = byteBuf.readFloatLE()
        this.headYaw = byteBuf.readFloatLE()
        this.mode = byteBuf.readByte().toInt()
        this.onGround = byteBuf.readBoolean()
        this.ridingEid = byteBuf.readEntityRuntimeId()
        if (this.mode == MODE_TELEPORT) {
            this.teleportationCause = byteBuf.readIntLE()
            this.entityType = byteBuf.readIntLE()
        }
        this.frame = byteBuf.readUnsignedVarLong()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityRuntimeId(this.eid)
        byteBuf.writeVector3f(this.x, this.y, this.z)
        byteBuf.writeFloatLE(this.pitch)
        byteBuf.writeFloatLE(this.yaw)
        byteBuf.writeFloatLE(this.headYaw)
        byteBuf.writeByte(mode.toByte().toInt())
        byteBuf.writeBoolean(this.onGround)
        byteBuf.writeEntityRuntimeId(this.ridingEid)
        if (this.mode == MODE_TELEPORT) {
            byteBuf.writeIntLE(this.teleportationCause)
            byteBuf.writeIntLE(this.entityType)
        }
        byteBuf.writeUnsignedVarLong(this.frame)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.MOVE_PLAYER_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val MODE_NORMAL: Int = 0
        const val MODE_RESET: Int = 1 //MODE_RESPAWN
        const val MODE_TELEPORT: Int = 2
        const val MODE_PITCH: Int = 3 //facepalm Mojang MODE_HEAD_ROTATION
    }
}
