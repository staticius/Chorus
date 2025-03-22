package org.chorus.level.format.palette

fun interface RuntimeDataSerializer<V> {
    fun serialize(value: V): Int
}
