package org.chorus.camera.instruction.impl

import org.chorus.camera.instruction.CameraInstruction
import org.chorus.camera.instruction.impl.ClearInstruction

/**
 * @author daoge_cmd
 * @date 2023/6/11
 * PowerNukkitX Project
 */
object ClearInstruction : CameraInstruction {
    private val INSTANCE: ClearInstruction = ClearInstruction()

    @JvmStatic
    fun get(): ClearInstruction {
        return INSTANCE
    }
}
