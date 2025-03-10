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

/**
 * @author daoge_cmd
 * @date 2023/6/11
 * PowerNukkitX Project
 */

class CameraPreset(
    private val identifier: String?,
    inheritFrom: String?,
    private val pos: Vector3f?,
    private val yaw: Float?,
    private val pitch: Float?,
    private val rotationSpeed: Float?,
    snapToTarget: Boolean?,
    blockListeningRadius: Float?,
    private val viewOffset: Vector2f?,
    private val entityOffset: Vector3f?,
    private val radius: Float?,
    private val yawLimitMin: Float?,
    private val yawLimitMax: Float?,
    private val listener: CameraAudioListener?,
    playEffect: Boolean?,
    private val horizontalRotationLimit: Vector2f?,
    private val verticalRotationLimit: Vector2f?,
    continueTargeting: Boolean?,
    alignTargetAndCameraForward: Boolean?,
    aimAssist: CameraPresetAimAssist?
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
        identifier: String?,
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
        playEffect?.orElseGet(null),
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
                require(!PRESETS.containsKey(preset.getIdentifier())) { "Camera preset " + preset.getIdentifier() + " already exists!" }
                PRESETS[preset.getIdentifier()] = preset
                CommandEnum.CAMERA_PRESETS.updateSoftEnum(UpdateSoftEnumPacket.Type.ADD, preset.getIdentifier())
            }
            var id = 0
            //重新分配id
            for (preset in presets) {
                preset.id = id++
            }
            Server.getInstance().onlinePlayers.values.forEach(Consumer { obj: Player -> obj.sendCameraPresets() })
        }

        val FIRST_PERSON: CameraPreset = CameraPreset.builder()
            .identifier("minecraft:first_person")
            .build()
        val FREE: CameraPreset = CameraPreset.builder()
            .identifier("minecraft:free")
            .pos(Vector3f(0f, 0f, 0f))
            .yaw(0f)
            .pitch(0f)
            .build()
        val THIRD_PERSON: CameraPreset = CameraPreset.builder()
            .identifier("minecraft:third_person")
            .build()
        val THIRD_PERSON_FRONT: CameraPreset = CameraPreset.builder()
            .identifier("minecraft:third_person_front")
            .build()

        init {
            registerCameraPresets(FIRST_PERSON, FREE, THIRD_PERSON, THIRD_PERSON_FRONT)
        }
    }
}
