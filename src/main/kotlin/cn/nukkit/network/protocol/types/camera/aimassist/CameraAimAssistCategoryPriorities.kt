package cn.nukkit.network.protocol.types.camera.aimassist

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap
import lombok.Data

@Data
class CameraAimAssistCategoryPriorities {
    var entities: Map<String, Int> = Object2IntArrayMap()
    var blocks: Map<String, Int> = Object2IntArrayMap()
}
