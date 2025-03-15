package org.chorus.camera.data

import org.chorus.Player
import org.chorus.Server
import org.chorus.api.DoNotModify
import org.chorus.command.data.CommandEnum
import org.chorus.math.Vector2f
import org.chorus.math.Vector3f
import org.chorus.network.protocol.UpdateSoftEnumPacket
import org.chorus.network.protocol.types.camera.CameraAudioListener
import org.chorus.network.protocol.types.camera.aimassist.CameraPresetAimAssist
import org.chorus.utils.OptionalValue


import java.util.*
import java.util.function.Consumer

class CameraPreset @JvmOverloads constructor (
    private val identifier: String,
    inheritFrom: String? = null,
    private val pos: Vector3f? = null,
    private val yaw: Float? = null,
    private val pitch: Float? = null,
    private val rotationSpeed: Float? = null,
    snapToTarget: Boolean? = null,
    blockListeningRadius: Float? = null,
    private val viewOffset: Vector2f? = null,
    private val entityOffset: Vector3f? = null,
    private val radius: Float? = null,
    private val yawLimitMin: Float? = null,
    private val yawLimitMax: Float? = null,
    private val listener: CameraAudioListener? = null,
    playEffect: Boolean? = null,
    private val horizontalRotationLimit: Vector2f? = null,
    private val verticalRotationLimit: Vector2f? = null,
    continueTargeting: Boolean? = null,
    alignTargetAndCameraForward: Boolean? = null,
    aimAssist: CameraPresetAimAssist? = null
) {
    private val inheritFrom = inheritFrom ?: ""
    private val snapToTarget: OptionalValue<Boolean?> = OptionalValue.ofNullable(snapToTarget)
    private val continueTargeting: OptionalValue<Boolean?> =
        OptionalValue.ofNullable(continueTargeting)

    private val blockListeningRadius: OptionalValue<Float?> =
        OptionalValue.ofNullable(blockListeningRadius)
    private val playEffect: OptionalValue<Boolean?> = OptionalValue.ofNullable(playEffect)
    private val alignTargetAndCameraForward: OptionalValue<Boolean?> =
        OptionalValue.ofNullable(alignTargetAndCameraForward)
    private val aimAssist: OptionalValue<CameraPresetAimAssist?> =
        OptionalValue.ofNullable(aimAssist)


    private var id = 0

    /**
     * Remember to call the registerCameraPresets() method to register!
     */

    constructor(
        identifier: String,
        inheritFrom: String?,
        pos: Vector3f?,
        yaw: Float?,
        pitch: Float?,
        listener: CameraAudioListener?,
        playEffect: OptionalValue<Boolean?>?
    ) : this(
        identifier,
        inheritFrom,
        pos,
        yaw,
        pitch,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        listener,
        playEffect?.get(),
        null,
        null,
        null,
        null,
        null
    )

    companion object {
        private val PRESETS: MutableMap<String, CameraPreset> = TreeMap()

        @JvmStatic
        @get:DoNotModify
        val presets: Map<String, CameraPreset>
            get() = PRESETS

        @JvmStatic
        fun getPreset(identifier: String): CameraPreset? {
            return presets[identifier]
        }

        fun registerCameraPresets(vararg presets: CameraPreset) {
            for (preset in presets) {
                require(!PRESETS.containsKey(preset.identifier)) { "Camera preset " + preset.identifier + " already exists!" }
                PRESETS[preset.identifier] = preset
                CommandEnum.CAMERA_PRESETS.updateSoftEnum(UpdateSoftEnumPacket.Type.ADD, preset.identifier)
            }
            var id = 0
            //重新分配id
            for (preset in presets) {
                preset.id = id++
            }
            Server.instance.onlinePlayers.values.forEach(Consumer { obj: Player -> obj.sendCameraPresets() })
        }

        val FIRST_PERSON: CameraPreset = CameraPreset(
            "minecraft:first_person"
        )
        val FREE: CameraPreset = CameraPreset(
            "minecraft:free",
            pos = Vector3f(0f, 0f, 0f),
            yaw = 0f,
            pitch = 0f,
        )
        val THIRD_PERSON: CameraPreset = CameraPreset(
            "minecraft:third_person"
        )
        val THIRD_PERSON_FRONT: CameraPreset = CameraPreset(
            "minecraft:third_person_front"
        )

        init {
            registerCameraPresets(FIRST_PERSON, FREE, THIRD_PERSON, THIRD_PERSON_FRONT)
        }
    }
}
