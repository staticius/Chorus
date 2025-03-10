package org.chorus.camera.instruction.impl

import org.chorus.camera.data.CameraPreset
import org.chorus.camera.data.Ease
import org.chorus.camera.instruction.CameraInstruction
import org.chorus.math.Vector2f
import org.chorus.math.Vector3f
import org.chorus.utils.OptionalValue



/**
 * @author daoge_cmd
 * @date 2023/6/11
 * PowerNukkitX Project
 */


class SetInstruction : CameraInstruction {
    private val ease: Ease? = null
    private val pos: Vector3f? = null
    private val rot: Vector2f? = null
    private val facing: Vector3f? = null
    private val preset: CameraPreset? = null
    private val viewOffset: Vector2f? = null
    private val entityOffset: Vector3f? = null
    private val defaultPreset: OptionalValue<Boolean> = OptionalValue.empty()
}
