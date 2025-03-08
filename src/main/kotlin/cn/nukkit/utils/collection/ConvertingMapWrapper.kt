/*
 * https://PowerNukkit.org - The Nukkit you know but Powerful!
 * Copyright (C) 2020  José Roberto de Araújo Júnior
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.nukkit.utils.collection

import java.util.*
import java.util.function.Function

/**
 * @author joserobjr
 * @since 2020-10-05
 */
class ConvertingMapWrapper<K, V1, V2> @JvmOverloads constructor(
    proxied: MutableMap<K, V2>,
    converter: Function<V1, V2>,
    reverseConverter: Function<V2, V1>,
    convertReturnedNulls: Boolean = false
) :
    AbstractMap<K, V1?>() {
    private val converter: Function<V1?, V2>
    private val reverseConverter: Function<V2?, V1>
    private val proxied: MutableMap<K, V2?>
    private val entrySet: ConvertingSetWrapper<Map.Entry<K, V1>, Map.Entry<K, V2>>
    private val convertReturnedNulls: Boolean


    init {
        this.proxied = proxied
        this.converter = converter
        this.reverseConverter = reverseConverter
        this.convertReturnedNulls = convertReturnedNulls
        entrySet = ConvertingSetWrapper(
            proxied.entries,
            Function { entry: Map.Entry<K, V1>? -> EntryWrapper<V2, V1>(entry, reverseConverter, converter) },
            Function { entry: Map.Entry<K, V2>? -> EntryWrapper<V1, V2>(entry, converter, reverseConverter) }
        )
    }

    override fun entrySet(): Set<Map.Entry<K, V1>> {
        return entrySet
    }

    override fun size(): Int {
        return proxied.size
    }

    override fun isEmpty(): Boolean {
        return proxied.isEmpty()
    }

    override fun containsValue(value: Any?): Boolean {
        val uncheckedConverter = converter
        val converted: Any = uncheckedConverter.apply(value as V1?)
        return proxied.containsValue(converted)
    }

    override fun containsKey(key: Any): Boolean {
        return proxied.containsKey(key)
    }

    override fun get(key: Any): V1? {
        val found = proxied.get(key)
        if (found == null && !convertReturnedNulls) {
            return null
        }
        return reverseConverter.apply(found)
    }

    override fun put(key: K, value: V1?): V1? {
        val removed = proxied.put(key, converter.apply(value))
        if (removed == null && !convertReturnedNulls) {
            return null
        }
        return reverseConverter.apply(removed)
    }

    override fun remove(key: Any): V1? {
        val removed = proxied.remove(key)
        if (removed == null && !convertReturnedNulls) {
            return null
        }
        return reverseConverter.apply(removed)
    }

    override fun remove(key: Any, value: Any): Boolean {
        val uncheckedConverter = converter
        val converted: Any = uncheckedConverter.apply(value as V1)
        return proxied.remove(key, converted)
    }

    override fun clear() {
        proxied.clear()
    }

    override fun keySet(): Set<K> {
        return proxied.keys
    }

    private inner class EntryWrapper<E1, E2>(
        entryProxied: MutableMap.MutableEntry<K, E2>,
        entryConverter: Function<E1, E2>,
        entryReverseConverter: Function<E2, E1>
    ) :
        MutableMap.MutableEntry<K, E1?> {
        private val entryConverter: Function<E1?, E2>
        private val entryReverseConverter: Function<E2?, E1>
        private val entryProxied: MutableMap.MutableEntry<K, E2>

        init {
            this.entryConverter = entryConverter
            this.entryReverseConverter = entryReverseConverter
            this.entryProxied = entryProxied
        }

        override val key: K
            get() = entryProxied.key

        override val value: E1
            get() {
                val value: E2? = entryProxied.value
                if (value == null && !convertReturnedNulls) {
                    return null
                }
                return entryReverseConverter.apply(value)
            }

        override fun setValue(value: E1?): E1? {
            val newValue = entryConverter.apply(value)
            val oldValue: E2? = entryProxied.setValue(newValue)
            if (oldValue == null && !convertReturnedNulls) {
                return null
            }
            return entryReverseConverter.apply(oldValue)
        }

        override fun toString(): String {
            return entryProxied.key.toString() + "=" + value
        }

        override fun equals(o: Any?): Boolean {
            if (o === this) {
                return true
            }
            if (o is Map.Entry<*, *>) {
                val e = o
                return entryProxied.key == e.key && value == e.value
            }
            return false
        }

        override fun hashCode(): Int {
            return Objects.hashCode(entryProxied.key) xor Objects.hashCode(value)
        }
    }
}
