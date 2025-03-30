package org.chorus.camera.instruction.impl

import org.chorus.camera.data.CameraPreset
import org.chorus.camera.data.Ease
import org.chorus.camera.instruction.CameraInstruction
import org.chorus.math.Vector2f
import org.chorus.math.Vector3f
import org.chorus.utils.OptionalValue

data class SetInstruction(
    val ease: Ease? = null,
    val pos: Vector3f? = null,
    val rot: Vector2f? = null,
    val facing: Vector3f? = null,
    val preset: CameraPreset? = null,
    val viewOffset: Vector2f? = null,
    val entityOffset: Vector3f? = null,
    val defaultPreset: OptionalValue<Boolean> = OptionalValue.empty()
) : CameraInstruction
