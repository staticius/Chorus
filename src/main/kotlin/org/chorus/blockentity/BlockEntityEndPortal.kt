package org.chorus.blockentity

import cn.nukkit.block.Block
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author GoodLucky777
 */
class BlockEntityEndPortal(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt) {
    override val isBlockEntityValid: Boolean
        get() = level
            .getBlockIdAt(floorX, floorY, floorZ) === Block.END_PORTAL
}
