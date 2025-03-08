package cn.nukkit.level.format.palette

/**
 * Allay Project 2023/4/14
 *
 * @author JukeboxMC | daoge_cmd
 */
fun interface RuntimeDataSerializer<V> {
    fun serialize(value: V): Int
}
