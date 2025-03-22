package org.chorus.level.format.palette

import org.chorus.nbt.tag.CompoundTag

fun interface PersistentDataDeserializer<V> {
    fun deserialize(nbtMap: CompoundTag?): V
}
