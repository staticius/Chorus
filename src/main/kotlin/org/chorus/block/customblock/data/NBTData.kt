package org.chorus.block.customblock.data

import org.chorus.nbt.tag.CompoundTag

interface NBTData {
    fun toCompoundTag(): CompoundTag
}
