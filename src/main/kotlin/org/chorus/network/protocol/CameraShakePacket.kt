package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class CameraShakePacket : DataPacket() {
    @JvmField
    var intensity: Float = 0f
    @JvmField
    var duration: Float = 0f
    @JvmField
    var shakeType: CameraShakeType? = null
    @JvmField
    var shakeAction: CameraShakeAction? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.intensity = byteBuf.readFloatLE()
        this.duration = byteBuf.readFloatLE()
        this.shakeType = CameraShakeType.entries[byteBuf.readByte().toInt()]
        this.shakeAction = CameraShakeAction.entries[byteBuf.readByte().toInt()]
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeFloatLE(this.intensity)
        byteBuf.writeFloatLE(this.duration)
        byteBuf.writeByte(shakeType!!.ordinal.toByte().toInt())
        byteBuf.writeByte(shakeAction!!.ordinal.toByte().toInt())
    }

    enum class CameraShakeAction {
        ADD,
        STOP
    }

    enum class CameraShakeType {
        POSITIONAL,
        ROTATIONAL
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CAMERA_SHAKE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
