package org.chorus.blockentity

import cn.nukkit.block.BlockID
import cn.nukkit.inventory.DropperInventory
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

class BlockEntityDropper(chunk: IChunk, nbt: CompoundTag) : BlockEntityEjectable(chunk, nbt) {
    override fun createInventory(): DropperInventory {
        inventory = DropperInventory(this)
        return getInventory()
    }

    override val blockEntityName: String
        get() = BlockEntityID.Companion.DROPPER

    override fun getInventory(): DropperInventory {
        return inventory as DropperInventory
    }

    override val isBlockEntityValid: Boolean
        get() = this.levelBlock.id === BlockID.DROPPER
}
