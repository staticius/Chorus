package org.chorus.blockentity

import cn.nukkit.block.BlockID
import cn.nukkit.inventory.DispenserInventory
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

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
