package org.chorus.network.protocol

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.chorus.camera.data.CameraPreset
import org.chorus.math.Vector2f
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.camera.aimassist.CameraAimAssist
import org.chorus.network.protocol.types.camera.aimassist.CameraPresetAimAssist
import org.chorus.utils.OptionalValue
import java.util.function.Consumer


class CameraPresetsPacket : DataPacket() {
    @JvmField
    val presets: MutableList<CameraPreset> = ObjectArrayList()

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(presets.size)
        for (p in presets) {
            writePreset(byteBuf, p)
        }
    }

    fun writePreset(byteBuf: HandleByteBuf, preset: CameraPreset) {
        byteBuf.writeString(preset.identifier)
        byteBuf.writeString(preset.inheritFrom)
        byteBuf.writeNotNull(preset.pos) { v -> byteBuf.writeFloatLE(v!!.x) }
        byteBuf.writeNotNull(preset.pos) { v -> byteBuf.writeFloatLE(v!!.y) }
        byteBuf.writeNotNull(preset.pos) { v -> byteBuf.writeFloatLE(v!!.z) }
        byteBuf.writeNotNull(preset.pitch) { value -> byteBuf.writeFloatLE(value!!) }
        byteBuf.writeNotNull(preset.yaw) { value -> byteBuf.writeFloatLE(value!!) }
        byteBuf.writeNotNull(preset.rotationSpeed) { value -> byteBuf.writeFloatLE(value!!) }
        byteBuf.writeOptional(preset.snapToTarget) { value -> byteBuf.writeBoolean(value!!) }
        byteBuf.writeNotNull(preset.horizontalRotationLimit) { byteBuf.writeVector2f(it!!) }
        byteBuf.writeNotNull(preset.verticalRotationLimit) { byteBuf.writeVector2f(it!!) }
        byteBuf.writeOptional(
            preset.continueTargeting
        ) { value -> byteBuf.writeBoolean(value!!) }
        byteBuf.writeOptional(
            preset.blockListeningRadius
        ) { value -> byteBuf.writeFloatLE(value!!) }
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
