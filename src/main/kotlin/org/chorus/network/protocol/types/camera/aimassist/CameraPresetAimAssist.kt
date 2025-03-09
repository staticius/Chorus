package org.chorus.network.protocol.types.camera.aimassist

import cn.nukkit.math.Vector2f
import cn.nukkit.utils.OptionalValue
import lombok.Data

@Data
class CameraPresetAimAssist {
    var presetId: OptionalValue<String>? = null
    var targetMode: OptionalValue<CameraAimAssist>? = null
    private val angle: OptionalValue<Vector2f>? = null
    private val distance: OptionalValue<Float>? = null
}
