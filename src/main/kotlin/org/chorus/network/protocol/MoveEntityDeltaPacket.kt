package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*






class MoveEntityDeltaPacket : DataPacket() {
    var runtimeEntityId: Long = 0

    // Unused fields have to be set to true
    var flags: Short = 0xFE00.toShort()
    var x: Float = 0f
    var y: Float = 0f
    var z: Float = 0f
    var pitch: Float = 0f
    var yaw: Float = 0f
    var headYaw: Float = 0f

    override fun decode(byteBuf: HandleByteBuf) {
        this.runtimeEntityId = byteBuf.readEntityRuntimeId()
        this.flags = byteBuf.readShortLE()
        if ((flags.toInt() and FLAG_HAS_X.toInt()) != 0) {
            this.x = this.getCoordinate(byteBuf)
        }
        if ((flags.toInt() and FLAG_HAS_Y.toInt()) != 0) {
            this.y = this.getCoordinate(byteBuf)
        }
        if ((flags.toInt() and FLAG_HAS_Z.toInt()) != 0) {
            this.z = this.getCoordinate(byteBuf)
        }
        if ((flags.toInt() and FLAG_HAS_PITCH.toInt()) != 0) {
            this.pitch = this.getRotation(byteBuf)
        }
        if ((flags.toInt() and FLAG_HAS_YAW.toInt()) != 0) {
            this.yaw = this.getRotation(byteBuf)
        }
        if ((flags.toInt() and FLAG_HAS_HEAD_YAW.toInt()) != 0) {
            this.headYaw = this.getRotation(byteBuf)
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityRuntimeId(this.runtimeEntityId)
        byteBuf.writeShortLE(flags.toInt())
        if ((flags.toInt() and FLAG_HAS_X.toInt()) != 0) {
            this.putCoordinate(byteBuf, this.x)
        }
        if ((flags.toInt() and FLAG_HAS_Y.toInt()) != 0) {
            this.putCoordinate(byteBuf, this.y)
        }
        if ((flags.toInt() and FLAG_HAS_Z.toInt()) != 0) {
            this.putCoordinate(byteBuf, this.z)
        }
        if ((flags.toInt() and FLAG_HAS_PITCH.toInt()) != 0) {
            this.putRotation(byteBuf, this.pitch)
        }
        if ((flags.toInt() and FLAG_HAS_YAW.toInt()) != 0) {
            this.putRotation(byteBuf, this.yaw)
        }
        if ((flags.toInt() and FLAG_HAS_HEAD_YAW.toInt()) != 0) {
            this.putRotation(byteBuf, this.headYaw)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.MOVE_ENTITY_DELTA_PACKET
    }

    private fun getCoordinate(byteBuf: HandleByteBuf): Float {
        return byteBuf.readFloatLE()
    }

    private fun putCoordinate(byteBuf: HandleByteBuf, value: Float) {
        byteBuf.writeFloatLE(value)
    }

    private fun getRotation(byteBuf: HandleByteBuf): Float {
        return byteBuf.readByte() * (360f / 256f)
    }

    private fun putRotation(byteBuf: HandleByteBuf, value: Float) {
        byteBuf.writeByte((value / (360f / 256f)).toInt().toByte().toInt())
    }

    fun hasFlag(flag: Int): Boolean {
        return (flags.toInt() and flag) != 0
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val FLAG_HAS_X: Short = 1
        const val FLAG_HAS_Y: Short = 2
        const val FLAG_HAS_Z: Short = 4
        const val FLAG_HAS_PITCH: Short = 8
        const val FLAG_HAS_YAW: Short = 16
        const val FLAG_HAS_HEAD_YAW: Short = 32
        const val FLAG_ON_GROUND: Short = 64
        const val FLAG_TELEPORTING: Short = 128
        const val FLAG_FORCE_MOVE_LOCAL_ENTITY: Short = 256
    }
}
