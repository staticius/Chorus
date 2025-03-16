package org.chorus.level.format.palette

import org.chorus.nbt.tag.CompoundTag

fun interface PersistentDataSerializer<V> {
    fun serialize(value: V): CompoundTag
}
