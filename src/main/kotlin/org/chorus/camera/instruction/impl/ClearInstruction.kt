package org.chorus.camera.instruction.impl

import org.chorus.camera.instruction.CameraInstruction
import org.chorus.camera.instruction.impl.ClearInstruction

object ClearInstruction : CameraInstruction {
    @JvmStatic
    fun get(): ClearInstruction {
        return this
    }
}
