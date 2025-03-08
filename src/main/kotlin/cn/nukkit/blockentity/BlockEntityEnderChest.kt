package cn.nukkit.blockentity

import cn.nukkit.block.Block
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

class BlockEntityEnderChest(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt), BlockEntityNameable {
    init {
        movable = true
    }

    override val isBlockEntityValid: Boolean
        get() = this.block.id == Block.ENDER_CHEST

    override val spawnCompound: CompoundTag
        get() {
            val spawnCompound = super.getSpawnCompound()
                .putBoolean("isMovable", this.isMovable)
            if (this.hasName()) {
                spawnCompound.put("CustomName", namedTag["CustomName"])
            }
            return spawnCompound
        }

    override var name: String
        get() = if (this.hasName()) namedTag.getString("CustomName") else "EnderChest"
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
}
