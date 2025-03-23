package org.chorus.blockentity

import org.chorus.block.BlockID
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

class BlockEntityEndPortal(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt) {
    override val isBlockEntityValid: Boolean
        get() = level
            .getBlockIdAt(floorX, floorY, floorZ) === BlockID.END_PORTAL
}
