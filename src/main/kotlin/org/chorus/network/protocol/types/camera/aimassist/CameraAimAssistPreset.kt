package org.chorus.network.protocol.types.camera.aimassist

import org.chorus.utils.OptionalValue
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import lombok.Data

@Data
class CameraAimAssistPreset {
    var identifier: String? = null
    var categories: String? = null
    val exclusionList: List<String> = ObjectArrayList()
    val liquidTargetingList: List<String> = ObjectArrayList()
    val itemSettings: Map<String, String> = Object2ObjectArrayMap()
    var defaultItemSettings: OptionalValue<String>? = null
    var handSettings: OptionalValue<String>? = null
}
