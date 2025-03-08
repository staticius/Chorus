package cn.nukkit.blockentity

import cn.nukkit.block.BlockID
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.NukkitMath
import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author joserobjr
 */
class BlockEntityTarget(chunk: IChunk, nbt: CompoundTag) : BlockEntity(chunk, nbt) {
    override val isBlockEntityValid: Boolean
        get() = levelBlock.id === BlockID.TARGET

    var activePower: Int
        get() = NukkitMath.clamp(namedTag.getInt("activePower"), 0, 15)
        set(power) {
            namedTag.putInt("activePower", power)
        }
}
