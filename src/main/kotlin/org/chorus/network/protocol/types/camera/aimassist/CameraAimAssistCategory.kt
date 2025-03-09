package org.chorus.network.protocol.types.camera.aimassist

import lombok.Data

@Data
class CameraAimAssistCategory {
    var name: String? = null
    var priorities: CameraAimAssistCategoryPriorities? = null
}
