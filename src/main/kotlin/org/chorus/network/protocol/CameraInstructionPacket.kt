package org.chorus.network.protocol

import org.chorus.camera.data.Ease
import org.chorus.camera.data.Time
import org.chorus.camera.instruction.CameraInstruction
import org.chorus.camera.instruction.impl.ClearInstruction
import org.chorus.camera.instruction.impl.FadeInstruction
import org.chorus.camera.instruction.impl.SetInstruction
import org.chorus.camera.instruction.impl.TargetInstruction
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.utils.OptionalBoolean
import java.awt.Color

class CameraInstructionPacket : DataPacket() {
    var setInstruction: SetInstruction? = null
    var fadeInstruction: FadeInstruction? = null
    var clearInstruction: ClearInstruction? = null
    private var targetInstruction: TargetInstruction? = null
    private val removeTarget: OptionalBoolean = OptionalBoolean.empty()

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeNotNull(setInstruction) { s ->
            byteBuf.writeIntLE(s.preset!!.getId())
            byteBuf.writeNotNull(s.ease) { e -> this.writeEase(byteBuf, e) }
            byteBuf.writeNotNull(s.pos, byteBuf::writeVector3f)
            byteBuf.writeNotNull(s.rot, byteBuf::writeVector2f)
            byteBuf.writeNotNull(s.facing, byteBuf::writeVector3f)
            byteBuf.writeNotNull(s.viewOffset, byteBuf::writeVector2f)
            byteBuf.writeNotNull(s.entityOffset, byteBuf::writeVector3f)
            byteBuf.writeOptional(s.defaultPreset) { value -> byteBuf.writeBoolean(value) }
        }

        if (clearInstruction == null) {
            byteBuf.writeBoolean(false)
        } else {
            byteBuf.writeBoolean(true) // optional.isPresent
            byteBuf.writeBoolean(true) // actual data
        }

        byteBuf.writeNotNull(fadeInstruction) { f ->
            byteBuf.writeNotNull(f.time) { t -> this.writeTimeData(byteBuf, t) }
            byteBuf.writeNotNull(f.color) { c -> this.writeColor(byteBuf, c) }
        }

        byteBuf.writeNotNull(targetInstruction) { target ->
            byteBuf.writeNotNull(target.targetCenterOffset, byteBuf::writeVector3f)
            byteBuf.writeLongLE(target.uniqueEntityId)
        }

        byteBuf.writeOptional(
            removeTarget.toOptionalValue()
        ) { value: Boolean? -> byteBuf.writeBoolean(value!!) }
    }

    fun setInstruction(instruction: CameraInstruction) {
        when (instruction) {
            is SetInstruction -> this.setInstruction = instruction
            is FadeInstruction -> this.fadeInstruction = instruction
            is ClearInstruction -> this.clearInstruction = instruction
            is TargetInstruction -> this.targetInstruction = instruction
        }
    }

    protected fun writeEase(byteBuf: HandleByteBuf, ease: Ease) {
        byteBuf.writeByte(ease.easeType.ordinal.toByte().toInt())
        byteBuf.writeFloatLE(ease.time)
    }

    protected fun writeTimeData(byteBuf: HandleByteBuf, time: Time) {
        byteBuf.writeFloatLE(time.fadeIn)
        byteBuf.writeFloatLE(time.hold)
        byteBuf.writeFloatLE(time.fadeOut)
    }

    protected fun writeColor(byteBuf: HandleByteBuf, color: Color) {
        byteBuf.writeFloatLE(color.red / 255f)
        byteBuf.writeFloatLE(color.green / 255f)
        byteBuf.writeFloatLE(color.blue / 255f)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CAMERA_INSTRUCTION_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
