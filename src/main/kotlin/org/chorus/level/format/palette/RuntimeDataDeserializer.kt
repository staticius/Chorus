package org.chorus.level.format.palette

fun interface RuntimeDataDeserializer<V> {
    fun deserialize(id: Int): V
}
