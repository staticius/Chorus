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
package org.chorus.utils.collection

import java.util.*
import java.util.function.Function

/**
 * @author joserobjr
 * @since 2020-10-05
 */
class ConvertingSetWrapper<V1, V2>(
    private val proxied: MutableSet<V2>,
    private val converter: Function<V1, V2>,
    private val reverseConverter: Function<V2, V1>
) :
    AbstractSet<V1>() {
    override fun iterator(): MutableIterator<V1> {
        return ConvertingIterator()
    }

    override fun size(): Int {
        return proxied.size
    }

    override fun isEmpty(): Boolean {
        return proxied.isEmpty()
    }

    override fun contains(o: Any): Boolean {
        val uncheckedConverter = converter
        val converted: Any = uncheckedConverter.apply(o as V1)
        return proxied.contains(converted)
    }

    override fun add(v1: V1): Boolean {
        return proxied.add(converter.apply(v1))
    }

    override fun remove(o: Any): Boolean {
        val uncheckedConverter = converter
        val converted: Any = uncheckedConverter.apply(o as V1)
        return proxied.remove(converted)
    }

    override fun clear() {
        proxied.clear()
    }

    private inner class ConvertingIterator : MutableIterator<V1> {
        private val proxiedIterator = proxied.iterator()

        override fun remove() {
            proxiedIterator.remove()
        }

        override fun hasNext(): Boolean {
            return proxiedIterator.hasNext()
        }

        override fun next(): V1 {
            return reverseConverter.apply(proxiedIterator.next())
        }
    }
}
