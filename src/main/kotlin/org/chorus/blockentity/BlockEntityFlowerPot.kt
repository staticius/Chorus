package org.chorus.blockentity

import cn.nukkit.block.Block
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author Snake1999
 * @since 2016/2/4
 */
class BlockEntityFlowerPot(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt) {
    init {
        movable = true
    }

    override val isBlockEntityValid: Boolean
        get() {
            val blockId = block.id
            return blockId == Block.FLOWER_POT
        }

    override val spawnCompound: CompoundTag
        get() {
            val tag = super.getSpawnCompound()
                .putBoolean("isMovable", this.isMovable)
            if (namedTag.containsCompound("PlantBlock")) tag.putCompound(
                "PlantBlock",
                namedTag.getCompound("PlantBlock")
            )
            return tag
        }
}
