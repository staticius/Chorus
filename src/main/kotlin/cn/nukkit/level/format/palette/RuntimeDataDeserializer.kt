package cn.nukkit.level.format.palette

/**
 * Allay Project 2023/4/14
 *
 * @author JukeboxMC | daoge_cmd
 */
fun interface RuntimeDataDeserializer<V> {
    fun deserialize(id: Int): V
}
