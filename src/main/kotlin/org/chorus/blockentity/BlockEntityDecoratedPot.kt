package org.chorus.blockentity

import org.chorus.block.Block
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

class BlockEntityDecoratedPot(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt) {
    override val isBlockEntityValid: Boolean
        get() = block.id === Block.DECORATED_POT

    override val spawnCompound: CompoundTag
        get() = super.getSpawnCompound()
            .putList("sherds", namedTag.getList("sherds"))
}
