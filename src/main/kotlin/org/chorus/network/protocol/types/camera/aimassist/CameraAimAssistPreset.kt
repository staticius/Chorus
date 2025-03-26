package org.chorus.network.protocol.types.camera.aimassist

import org.chorus.utils.OptionalValue

data class CameraAimAssistPreset(
    val identifier: String,
    val categories: String,
    val exclusionList: List<String>,
    val liquidTargetingList: List<String>,
    val itemSettings: Map<String, String>,
    val defaultItemSettings: OptionalValue<String>,
    val handSettings: OptionalValue<String>,
)
