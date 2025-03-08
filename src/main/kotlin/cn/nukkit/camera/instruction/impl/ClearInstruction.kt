package cn.nukkit.camera.instruction.impl

import cn.nukkit.camera.instruction.CameraInstruction
import cn.nukkit.camera.instruction.impl.ClearInstruction

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
