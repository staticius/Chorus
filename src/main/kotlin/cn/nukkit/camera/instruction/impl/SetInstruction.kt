package cn.nukkit.camera.instruction.impl

import cn.nukkit.camera.data.CameraPreset
import cn.nukkit.camera.data.Ease
import cn.nukkit.camera.instruction.CameraInstruction
import cn.nukkit.math.Vector2f
import cn.nukkit.math.Vector3f
import cn.nukkit.utils.OptionalValue
import lombok.Builder
import lombok.Getter

/**
 * @author daoge_cmd
 * @date 2023/6/11
 * PowerNukkitX Project
 */
@Builder
@Getter
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
