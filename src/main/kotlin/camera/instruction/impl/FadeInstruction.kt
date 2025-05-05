package org.chorus_oss.chorus.camera.instruction.impl

import org.chorus_oss.chorus.camera.data.Time
import org.chorus_oss.chorus.camera.instruction.CameraInstruction

import java.awt.Color

class FadeInstruction(
    val color: Color? = null,
    val time: Time? = null,
) : CameraInstruction
