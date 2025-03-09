package org.chorus.level.format.palette

import org.chorus.nbt.tag.CompoundTag

/**
 * Allay Project 2023/4/14
 *
 * @author JukeboxMC | daoge_cmd
 */
fun interface PersistentDataSerializer<V> {
    fun serialize(value: V): CompoundTag?
}
