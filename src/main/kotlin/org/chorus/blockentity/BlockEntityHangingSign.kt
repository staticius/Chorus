package org.chorus.blockentity

import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

class BlockEntityHangingSign(chunk: IChunk, nbt: CompoundTag) : BlockEntitySign(chunk, nbt) {
    override var name: String
        get() = if (this.hasName()) namedTag.getString("CustomName") else "Hanging Sign"
        set(name) {
            super.name = name
        }

    fun hasName(): Boolean {
        return namedTag.contains("CustomName")
    }

    override val spawnCompound: CompoundTag
        get() = super.getSpawnCompound().putString("id", BlockEntityID.Companion.HANGING_SIGN)
}
