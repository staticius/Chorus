package org.chorus.camera.instruction.impl

import org.chorus.camera.instruction.CameraInstruction
import org.chorus.math.Vector3f

class TargetInstruction : CameraInstruction {
    private var targetCenterOffset: Vector3f? = null
    private var uniqueEntityId: Long = 0
}
