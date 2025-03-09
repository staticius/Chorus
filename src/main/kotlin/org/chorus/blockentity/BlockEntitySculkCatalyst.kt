package org.chorus.blockentity

import org.chorus.block.BlockID
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

/**
 * @author Kevims KCodeYT
 */
class BlockEntitySculkCatalyst(chunk: IChunk, nbt: CompoundTag) : BlockEntity(chunk, nbt) {
    override val isBlockEntityValid: Boolean
        get() = levelBlock.id === BlockID.SCULK_CATALYST
}
