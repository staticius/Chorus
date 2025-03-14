package org.chorus.network.protocol.types.camera.aimassist

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap


class CameraAimAssistCategoryPriorities {
    var entities: Map<String, Int> = Object2IntArrayMap()
    var blocks: Map<String, Int> = Object2IntArrayMap()
}
