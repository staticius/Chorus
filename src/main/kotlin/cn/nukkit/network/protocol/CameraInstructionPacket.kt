package cn.nukkit.network.protocol

import cn.nukkit.camera.data.Ease
import cn.nukkit.camera.data.Time
import cn.nukkit.camera.instruction.CameraInstruction
import cn.nukkit.camera.instruction.impl.ClearInstruction
import cn.nukkit.camera.instruction.impl.FadeInstruction
import cn.nukkit.camera.instruction.impl.SetInstruction
import cn.nukkit.camera.instruction.impl.TargetInstruction
import cn.nukkit.entity.Entity.getId
import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.utils.OptionalBoolean
import lombok.*
import java.awt.Color
import java.util.function.Consumer

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class CameraInstructionPacket : DataPacket() {
    var setInstruction: SetInstruction? = null
    var fadeInstruction: FadeInstruction? = null
    var clearInstruction: ClearInstruction? = null
    private var targetInstruction: TargetInstruction? = null
    private val removeTarget: OptionalBoolean = OptionalBoolean.empty()

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeNotNull<SetInstruction>(setInstruction) { s: SetInstruction? ->
            byteBuf.writeIntLE(s.getPreset().getId())
            byteBuf.writeNotNull<T>(
                s.getEase(),
                Consumer<T?> { e: T -> this.writeEase(byteBuf, e) })
            byteBuf.writeNotNull<T>(s.getPos(), byteBuf::writeVector3f)
            byteBuf.writeNotNull<T>(s.getRot(), byteBuf::writeVector2f)
            byteBuf.writeNotNull<T>(s.getFacing(), byteBuf::writeVector3f)
            byteBuf.writeNotNull<T>(s.getViewOffset(), byteBuf::writeVector2f)
            byteBuf.writeNotNull<T>(s.getEntityOffset(), byteBuf::writeVector3f)
            byteBuf.writeOptional<T>(
                s.getDefaultPreset(),
                Consumer<T> { value: T? -> byteBuf.writeBoolean(value) })
        }

        if (clearInstruction == null) {
            byteBuf.writeBoolean(false)
        } else {
            byteBuf.writeBoolean(true) //optional.isPresent
            byteBuf.writeBoolean(true) //actual data
        }

        byteBuf.writeNotNull<FadeInstruction>(fadeInstruction) { f: FadeInstruction? ->
            byteBuf.writeNotNull<T>(
                f.getTime(),
                Consumer<T?> { t: T -> this.writeTimeData(byteBuf, t) })
            byteBuf.writeNotNull<T>(
                f.getColor(),
                Consumer<T?> { c: T -> this.writeColor(byteBuf, c) })
        }

        byteBuf.writeNotNull<TargetInstruction>(targetInstruction) { target: TargetInstruction? ->
            byteBuf.writeNotNull<T>(target.getTargetCenterOffset(), byteBuf::writeVector3f)
            byteBuf.writeLongLE(targetInstruction.getUniqueEntityId())
        }

        byteBuf.writeOptional(
            removeTarget.toOptionalValue()
        ) { value: Boolean? -> byteBuf.writeBoolean(value!!) }
    }

    fun setInstruction(instruction: CameraInstruction) {
        when (instruction) {
            -> this.setInstruction = set
            -> this.fadeInstruction = fade
            -> this.clearInstruction = clear
            -> this.targetInstruction = target
            else -> {}
        }
    }

    protected fun writeEase(byteBuf: HandleByteBuf, ease: Ease) {
        byteBuf.writeByte(ease.easeType.ordinal().toByte().toInt())
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
