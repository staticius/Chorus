package org.chorus.block.customblock.data

import cn.nukkit.nbt.tag.CompoundTag

interface NBTData {
    fun toCompoundTag(): CompoundTag
}
