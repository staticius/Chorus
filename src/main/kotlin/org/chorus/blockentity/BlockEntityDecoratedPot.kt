package org.chorus.blockentity

import cn.nukkit.block.Block
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

class BlockEntityDecoratedPot(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt) {
    override val isBlockEntityValid: Boolean
        get() = block.id === Block.DECORATED_POT

    override val spawnCompound: CompoundTag
        get() = super.getSpawnCompound()
            .putList("sherds", namedTag.getList("sherds"))
}
