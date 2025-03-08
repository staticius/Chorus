package cn.nukkit.level.format.palette

import cn.nukkit.nbt.tag.CompoundTag

/**
 * Allay Project 2023/4/14
 *
 * @author JukeboxMC | daoge_cmd
 */
fun interface PersistentDataSerializer<V> {
    fun serialize(value: V): CompoundTag?
}
