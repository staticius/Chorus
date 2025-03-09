package org.chorus.network.protocol

import cn.nukkit.math.Vector2f
import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class CameraAimAssistPacket : DataPacket() {
    private var presetId: String? = null
    private var viewAngle: Vector2f? = null
    private var distance = 0f
    private var targetMode: TargetMode? = null
    private var action: Action? = null

    override fun pid(): Int {
        return ProtocolInfo.Companion.CAMERA_AIM_ASSIST_PACKET
    }

    @Suppress("unused")
    override fun decode(byteBuf: HandleByteBuf) {
        this.setPresetId(byteBuf.readString())
        this.setViewAngle(byteBuf.readVector2f())
        this.setDistance(byteBuf.readFloatLE())
        this.setTargetMode(TargetMode.entries[byteBuf.readUnsignedByte().toInt()])
        this.setAction(
            Action.entries[byteBuf.readUnsignedByte()
                .toInt()]
        )
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(presetId!!)
        byteBuf.writeVector2f(this.getViewAngle())
        byteBuf.writeFloatLE(this.getDistance())
        byteBuf.writeByte(this.getTargetMode().ordinal)
        byteBuf.writeByte(this.getAction().ordinal)
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    enum class TargetMode {
        ANGLE,
        DISTANCE,
        COUNT
    }

    enum class Action {
        SET,
        CLEAR,
        COUNT
    }
}
