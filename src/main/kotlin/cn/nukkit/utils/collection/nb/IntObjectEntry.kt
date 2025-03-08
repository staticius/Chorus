package cn.nukkit.utils.collection.nb

interface IntObjectEntry<V> : MutableMap.MutableEntry<Int?, V> {
    @get:Deprecated("")
    override val key: Int
        get() = intKey

    val intKey: Int
}
