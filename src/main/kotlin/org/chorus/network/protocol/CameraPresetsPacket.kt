package org.chorus.network.protocol

import org.chorus.camera.data.CameraPreset
import org.chorus.math.Vector2f
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.camera.aimassist.CameraAimAssist
import org.chorus.network.protocol.types.camera.aimassist.CameraPresetAimAssist
import org.chorus.utils.OptionalValue
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import lombok.*
import java.util.function.Consumer

@Getter
@Setter
@ToString
@AllArgsConstructor
class CameraPresetsPacket : DataPacket() {
    @JvmField
    val presets: List<CameraPreset> = ObjectArrayList()

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(presets.size)
        for (p in presets) {
            writePreset(byteBuf, p)
        }
    }

    fun writePreset(byteBuf: HandleByteBuf, preset: CameraPreset) {
        byteBuf.writeString(preset.getIdentifier())
        byteBuf.writeString(preset.getInheritFrom())
        byteBuf.writeNotNull<T>(preset.getPos(), Consumer<T?> { v: T? -> byteBuf.writeFloatLE(v.getX()) })
        byteBuf.writeNotNull<T>(preset.getPos(), Consumer<T?> { v: T? -> byteBuf.writeFloatLE(v.getY()) })
        byteBuf.writeNotNull<T>(preset.getPos(), Consumer<T?> { v: T? -> byteBuf.writeFloatLE(v.getZ()) })
        byteBuf.writeNotNull<T>(preset.getPitch(), Consumer<T?> { value: T? -> byteBuf.writeFloatLE(value) })
        byteBuf.writeNotNull<T>(preset.getYaw(), Consumer<T?> { value: T? -> byteBuf.writeFloatLE(value) })
        byteBuf.writeNotNull<T>(preset.getRotationSpeed(), Consumer<T?> { value: T? -> byteBuf.writeFloatLE(value) })
        byteBuf.writeOptional<T>(preset.getSnapToTarget(), Consumer<T> { value: T? -> byteBuf.writeBoolean(value) })
        byteBuf.writeNotNull<T>(preset.getHorizontalRotationLimit(), byteBuf::writeVector2f)
        byteBuf.writeNotNull<T>(preset.getVerticalRotationLimit(), byteBuf::writeVector2f)
        byteBuf.writeOptional<T>(
            preset.getContinueTargeting(),
            Consumer<T> { value: T? -> byteBuf.writeBoolean(value) })
        byteBuf.writeOptional<T>(
            preset.getBlockListeningRadius(),
            Consumer<T> { value: T? -> byteBuf.writeFloatLE(value) })
        byteBuf.writeNotNull<T>(preset.getViewOffset(), byteBuf::writeVector2f)
        byteBuf.writeNotNull<T>(preset.getEntityOffset(), byteBuf::writeVector3f)
        byteBuf.writeNotNull<T>(preset.getRadius(), Consumer<T?> { value: T? -> byteBuf.writeFloatLE(value) })
        byteBuf.writeNotNull<T>(preset.getYawLimitMin(), Consumer<T?> { value: T? -> byteBuf.writeFloatLE(value) })
        byteBuf.writeNotNull<T>(preset.getYawLimitMax(), Consumer<T?> { value: T? -> byteBuf.writeFloatLE(value) })
        byteBuf.writeNotNull<T>(
            preset.getListener(),
            Consumer<T?> { l: T? -> byteBuf.writeByte((l.ordinal() as Byte).toInt()) })
        byteBuf.writeOptional<T>(preset.getPlayEffect(), Consumer<T> { value: T? -> byteBuf.writeBoolean(value) })
        byteBuf.writeOptional<T>(
            preset.getAlignTargetAndCameraForward(),
            Consumer<T> { value: T? -> byteBuf.writeBoolean(value) })
        writeCameraPresetAimAssist(byteBuf, preset.getAimAssist())
    }

    fun writeCameraPresetAimAssist(byteBuf: HandleByteBuf, data: OptionalValue<CameraPresetAimAssist>) {
        val present = data.isPresent
        byteBuf.writeBoolean(present)
        if (present) {
            val value = data.get()
            byteBuf.writeOptional(
                value.getPresetId()
            ) { str: String? -> byteBuf.writeString(str!!) }
            writeTargetMode(byteBuf, value.getTargetMode())
            byteBuf.writeOptional(value.angle) { v: Vector2f? ->
                byteBuf.writeVector2f(
                    v!!
                )
            }
            byteBuf.writeOptional(
                value.distance
            ) { value: Float? -> byteBuf.writeFloatLE(value!!) }
        }
    }

    private fun writeTargetMode(byteBuf: HandleByteBuf, data: OptionalValue<CameraAimAssist>) {
        val present = data.isPresent
        byteBuf.writeBoolean(present)
        if (present) {
            byteBuf.writeIntLE(data.get().ordinal)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CAMERA_PRESETS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
