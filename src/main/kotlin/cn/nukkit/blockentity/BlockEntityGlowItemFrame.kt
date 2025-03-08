package cn.nukkit.blockentity

import cn.nukkit.block.Block
import cn.nukkit.block.BlockID
import cn.nukkit.item.ItemBlock
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag

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
            val tag = super.getSpawnCompound()

            if (!item!!.isNull) {
                val itemTag = NBTIO.putItemHelper(item)
                val networkDamage = item.damage
                val namespacedId = item.id
                if (namespacedId != null) {
                    itemTag.remove("id")
                    itemTag.putShort("Damage", networkDamage)
                    itemTag.putString("Name", namespacedId)
                }
                if (item.isBlock) {
                    itemTag.putCompound("Block", item.blockUnsafe.blockState.blockStateTag)
                }
                tag!!.putCompound("Item", itemTag)
                    .putByte("ItemRotation", this.itemRotation)
            }
            return tag!!
        }
}
