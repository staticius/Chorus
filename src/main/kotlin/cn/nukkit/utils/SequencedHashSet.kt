package cn.nukkit.utils

import cn.nukkit.entity.data.EntityDataMap.size
import cn.nukkit.nbt.tag.ListTag.size
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntMap

class SequencedHashSet<E> : MutableList<E> {
    private val map: Object2IntMap<E> = Object2IntLinkedOpenHashMap()
    private val inverse: Int2ObjectMap<E> = Int2ObjectLinkedOpenHashMap()
    private var index = 0

    override fun indexOf(o: Any): Int {
        return map.getInt(o)
    }

    override fun lastIndexOf(o: Any): Int {
        return map.getInt(o)
    }

    override fun listIterator(): MutableListIterator<E> {
        throw UnsupportedOperationException()
    }

    override fun listIterator(index: Int): MutableListIterator<E> {
        throw UnsupportedOperationException()
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> {
        throw UnsupportedOperationException()
    }

    override fun size(): Int {
        return map.size()
    }

    override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    override fun contains(o: Any): Boolean {
        return map.containsKey(o)
    }

    override fun iterator(): MutableIterator<E> {
        return map.keys.iterator()
    }

    override fun toArray(): Array<Any> {
        return map.keys.toTypedArray()
    }

    override fun <T> toArray(a: Array<T>): Array<T> {
        return map.keys.toArray(a)
    }

    override fun add(e: E): Boolean {
        if (!map.containsKey(e)) {
            val index = index++
            map.put(e, index)
            inverse.put(index, e)
            return true
        }
        return false
    }

    override fun remove(o: Any): Boolean {
        throw UnsupportedOperationException()
    }

    override fun containsAll(c: Collection<*>): Boolean {
        return map.keys.containsAll(c)
    }

    override fun addAll(c: Collection<E>): Boolean {
        for (e in c) {
            this.add(e)
        }
        return true
    }

    override fun addAll(index: Int, c: Collection<E>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun retainAll(c: Collection<*>?): Boolean {
        throw UnsupportedOperationException()
    }

    override fun removeAll(c: Collection<*>?): Boolean {
        throw UnsupportedOperationException()
    }

    override fun clear() {
        throw UnsupportedOperationException()
    }

    override fun get(index: Int): E {
        return inverse[index]
    }

    override fun set(index: Int, element: E): E {
        throw UnsupportedOperationException()
    }

    override fun add(index: Int, element: E) {
        throw UnsupportedOperationException()
    }

    override fun remove(index: Int): E {
        throw UnsupportedOperationException()
    }

    override fun toString(): String {
        return map.keys.toString()
    }
}
