package org.chorus.level.format.palette

import org.chorus.nbt.tag.CompoundTag

/**
 * Allay Project 2023/4/14
 *
 * @author JukeboxMC | daoge_cmd
 */
fun interface PersistentDataDeserializer<V> {
    fun deserialize(nbtMap: CompoundTag?): V
}
