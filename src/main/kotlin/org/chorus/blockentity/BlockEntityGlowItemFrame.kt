package org.chorus.blockentity

import org.chorus.block.Block
import org.chorus.block.BlockID
import org.chorus.item.ItemBlock
import org.chorus.level.format.IChunk
import org.chorus.nbt.NBTIO
import org.chorus.nbt.tag.CompoundTag

class BlockEntityGlowItemFrame(chunk: IChunk, nbt: CompoundTag) : BlockEntityItemFrame(chunk, nbt) {
    override var name: String
        get() = if (this.hasName()) namedTag.getString("CustomName") else "Glow Item Frame"
        set(name) {
            super.name = name
        }

    fun hasName(): Boolean {
        return namedTag.contains("CustomName")
    }

    override val spawnCompound: CompoundTag
        get() {
            if (!namedTag.contains("Item")) {
                this.setItem(ItemBlock(Block.get(BlockID.AIR)), false)
            }
            val item = item
            val tag = super.spawnCompound

            if (!item.isNothing) {
                val itemTag = NBTIO.putItemHelper(item)
                val networkDamage = item.damage
                val namespacedId = item.id
                if (namespacedId != null) {
                    itemTag.remove("id")
                    itemTag.putShort("Damage", networkDamage)
                    itemTag.putString("Name", namespacedId)
                }
                if (item.isBlock()) {
                    itemTag.putCompound("Block", item.blockUnsafe!!.blockState.blockStateTag)
                }
                tag.putCompound("Item", itemTag)
                    .putByte("ItemRotation", this.itemRotation)
            }
            return tag
        }
}
