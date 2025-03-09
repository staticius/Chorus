package org.chorus.network.protocol.types.camera.aimassist

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import lombok.Data


class CameraAimAssistCategories {
    var identifier: String? = null
    var categories: List<CameraAimAssistCategory> = ObjectArrayList()
}
