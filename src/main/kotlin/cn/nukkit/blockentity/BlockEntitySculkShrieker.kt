package cn.nukkit.blockentity

import cn.nukkit.block.BlockID
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author Kevims KCodeYT
 */
class BlockEntitySculkShrieker(chunk: IChunk, nbt: CompoundTag) : BlockEntity(chunk, nbt) {
    override val isBlockEntityValid: Boolean
        get() = levelBlock.id === BlockID.SCULK_SHRIEKER
}
