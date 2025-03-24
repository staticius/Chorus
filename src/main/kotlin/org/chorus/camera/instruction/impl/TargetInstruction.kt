package org.chorus.camera.instruction.impl

import org.chorus.camera.instruction.CameraInstruction
import org.chorus.math.Vector3f

data class TargetInstruction(val targetCenterOffset: Vector3f? = null, val uniqueEntityId: Long = 0) : CameraInstruction
