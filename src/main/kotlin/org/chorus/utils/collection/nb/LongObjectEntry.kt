package org.chorus.utils.collection.nb

interface LongObjectEntry<V> : MutableMap.MutableEntry<Long?, V> {
    @get:Deprecated("")
    override val key: Long
        get() = longKey

    val longKey: Long
}
