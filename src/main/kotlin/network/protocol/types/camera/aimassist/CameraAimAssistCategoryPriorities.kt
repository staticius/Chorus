package org.chorus_oss.chorus.network.protocol.types.camera.aimassist

data class CameraAimAssistCategoryPriorities(
    val entities: Map<String, Int>,
    val blocks: Map<String, Int>,
)
