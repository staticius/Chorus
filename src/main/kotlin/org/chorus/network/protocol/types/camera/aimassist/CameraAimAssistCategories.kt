package org.chorus.network.protocol.types.camera.aimassist

data class CameraAimAssistCategories(
    val identifier: String,
    val categories: List<CameraAimAssistCategory>,
)
