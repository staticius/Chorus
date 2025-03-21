package org.chorus.blockentity

import org.chorus.block.BlockID
import org.chorus.inventory.BarrelInventory
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

class BlockEntityBarrel(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnableContainer(chunk, nbt) {
    init {
        isMovable = true
    }

    override fun requireContainerInventory(): BarrelInventory {
        return BarrelInventory(this)
    }

    override val spawnCompound: CompoundTag
        get() = super.spawnCompound
            .putBoolean("isMovable", this.isMovable)
            .putBoolean("Findable", false)

    override val isBlockEntityValid: Boolean
        get() = block.id === BlockID.BARREL

    override var name: String?
        get() = if (this.hasName()) namedTag.getString("CustomName") else "Barrel"
        set(name) {
            if (name.isNullOrEmpty()) {
                namedTag.remove("CustomName")
                return
            }

            namedTag.putString("CustomName", name)
        }

    override fun hasName(): Boolean {
        return namedTag.contains("CustomName")
    }
}
