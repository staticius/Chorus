package org.chorus.camera.instruction.impl

import cn.nukkit.camera.data.Time
import cn.nukkit.camera.instruction.CameraInstruction
import lombok.Builder
import lombok.Getter
import java.awt.Color

/**
 * @author daoge_cmd
 * @date 2023/6/11
 * PowerNukkitX Project
 */
@Builder
@Getter
class FadeInstruction : CameraInstruction {
    private val color: Color? = null
    private val time: Time? = null
}
