package org.chorus.blockentity

import org.chorus.block.Block
import org.chorus.inventory.EnchantInventory
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag


class BlockEntityEnchantTable(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt),
    BlockEntityInventoryHolder {
    override val isBlockEntityValid: Boolean
        get() = block.id == Block.ENCHANTING_TABLE

    override var name: String
        get() = if (this.hasName()) namedTag.getString("CustomName") else "Enchanting Table"
        set(name) {
            if (name == null || name.isBlank()) {
                namedTag.remove("CustomName")
                return
            }

            namedTag.putString("CustomName", name)
        }

    override fun hasName(): Boolean {
        return namedTag.contains("CustomName")
    }

    override val spawnCompound: CompoundTag
        get() {
            val c = super.getSpawnCompound()
                .putBoolean("isMovable", false)

            if (this.hasName()) {
                c.put("CustomName", namedTag["CustomName"])
            }

            return c
        }

    override fun getInventory(): EnchantInventory {
        return EnchantInventory(this)
    }
}
