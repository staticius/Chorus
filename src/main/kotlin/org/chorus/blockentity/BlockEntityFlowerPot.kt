package org.chorus.blockentity

import org.chorus.block.Block
import org.chorus.block.BlockID
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

/**
 * @author Snake1999
 * @since 2016/2/4
 */
class BlockEntityFlowerPot(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt) {
    init {
        isMovable = true
    }

    override val isBlockEntityValid: Boolean
        get() {
            val blockId = block.id
            return blockId == BlockID.FLOWER_POT
        }

    override val spawnCompound: CompoundTag
        get() {
            val tag = super.spawnCompound
                .putBoolean("isMovable", this.isMovable)
            if (namedTag.containsCompound("PlantBlock")) tag.putCompound(
                "PlantBlock",
                namedTag.getCompound("PlantBlock")
            )
            return tag
        }
}
