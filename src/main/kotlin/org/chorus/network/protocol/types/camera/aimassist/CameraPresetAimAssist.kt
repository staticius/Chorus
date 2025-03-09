package org.chorus.network.protocol.types.camera.aimassist

import org.chorus.math.Vector2f
import org.chorus.utils.OptionalValue
import lombok.Data

@Data
class CameraPresetAimAssist {
    var presetId: OptionalValue<String>? = null
    var targetMode: OptionalValue<CameraAimAssist>? = null
    private val angle: OptionalValue<Vector2f>? = null
    private val distance: OptionalValue<Float>? = null
}
