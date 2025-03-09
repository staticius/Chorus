package org.chorus.blockentity

import org.chorus.block.BlockID
import org.chorus.inventory.DispenserInventory
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

class BlockEntityDispenser(chunk: IChunk, nbt: CompoundTag) : BlockEntityEjectable(chunk, nbt) {
    override fun createInventory(): DispenserInventory {
        inventory = DispenserInventory(this)
        return getInventory()
    }

    override val blockEntityName: String
        get() = BlockEntityID.Companion.DISPENSER

    override fun getInventory(): DispenserInventory {
        return inventory as DispenserInventory
    }

    override val isBlockEntityValid: Boolean
        get() = this.levelBlock.id === BlockID.DISPENSER
}
