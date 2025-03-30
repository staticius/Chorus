package org.chorus.network.protocol.types.camera.aimassist

import org.chorus.math.Vector2f
import org.chorus.utils.OptionalValue

data class CameraPresetAimAssist(
    var presetId: OptionalValue<String>,
    var targetMode: OptionalValue<CameraAimAssist>,
    val angle: OptionalValue<Vector2f>,
    val distance: OptionalValue<Float>,
)
