package org.chorus.blockentity

import org.chorus.block.BlockID
import org.chorus.level.format.IChunk
import org.chorus.math.ChorusMath
import org.chorus.nbt.tag.CompoundTag

/**
 * @author joserobjr
 */
class BlockEntityTarget(chunk: IChunk, nbt: CompoundTag) : BlockEntity(chunk, nbt) {
    override val isBlockEntityValid: Boolean
        get() = levelBlock.id === BlockID.TARGET

    var activePower: Int
        get() = ChorusMath.clamp(namedTag.getInt("activePower"), 0, 15)
        set(power) {
            namedTag.putInt("activePower", power)
        }
}
