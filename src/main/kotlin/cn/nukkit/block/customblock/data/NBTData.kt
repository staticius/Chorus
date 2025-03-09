package cn.nukkit.block.customblock.data

import cn.nukkit.nbt.tag.CompoundTag

interface NBTData {
    fun toCompoundTag(): CompoundTag
}
