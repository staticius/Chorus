package org.chorus.blockentity

import org.chorus.block.BlockID
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

/**
 * @author joserobjr
 */
class BlockEntityTarget(chunk: IChunk, nbt: CompoundTag) : BlockEntity(chunk, nbt) {
    override val isBlockEntityValid: Boolean
        get() = levelBlock.id === BlockID.TARGET

    var activePower: Int
        get() = namedTag.getInt("activePower").coerceIn(0, 15)
        set(power) {
            namedTag.putInt("activePower", power)
        }
}
