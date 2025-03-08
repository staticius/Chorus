package cn.nukkit.blockentity

import cn.nukkit.block.BlockID
import cn.nukkit.inventory.BarrelInventory
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

class BlockEntityBarrel(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnableContainer(chunk, nbt) {
    init {
        movable = true
    }

    override fun requireContainerInventory(): BarrelInventory {
        return BarrelInventory(this)
    }

    override val spawnCompound: CompoundTag
        get() = super.getSpawnCompound()
            .putBoolean("isMovable", this.isMovable)
            .putBoolean("Findable", false)

    override val isBlockEntityValid: Boolean
        get() = block.id === BlockID.BARREL

    override fun getInventory(): BarrelInventory {
        return inventory as BarrelInventory
    }

    override var name: String
        get() = if (this.hasName()) namedTag.getString("CustomName") else "Barrel"
        set(name) {
            if (name == null || name == "") {
                namedTag.remove("CustomName")
                return
            }

            namedTag.putString("CustomName", name)
        }

    override fun hasName(): Boolean {
        return namedTag.contains("CustomName")
    }
}
