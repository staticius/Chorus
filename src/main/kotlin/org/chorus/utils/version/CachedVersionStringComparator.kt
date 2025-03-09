/*
 Copyright (C) 2020  powernukkit.org - José Roberto de Araújo Júnior
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <https://www.gnu.org/licenses/>
 */
package org.chorus.utils.version

import javax.annotation.Nonnull

/**
 * A comparator that compares two strings as two [Version] objects but caching the object
 * to avoid parsing the same string multiple times. The cache have a limited size and have
 * strong reference to the version objects.
 *
 * **Note that this implementation is not synchronized.**
 * If multiple threads access a this comparator concurrently it *must* be
 * synchronized externally.  This is typically accomplished by
 * synchronizing on some object that naturally encapsulates the comparator or the
 * object that is using it.
 * @author joserobjr
 * @since 0.1.0
 */
class CachedVersionStringComparator(cacheSize: Int) : VersionStringComparator() {
    /**
     * The map that holds the cached objects, the implementation must remove the old entries when it reaches
     * the given `cacheSize`.
     * @since 0.1.0
     */
    private val cache: MutableMap<String, Version>

    /**
     * Creates a comparator with an empty cache that will have a population limited by the given size.
     * @param cacheSize The max size of the cache. When the size is reached, older entries are removed.
     * @since 0.1.0
     */
    init {
        cache = object : LinkedHashMap<String?, Version?>() {
            override fun removeEldestEntry(eldest: Map.Entry<String?, Version?>): Boolean {
                return size > cacheSize
            }
        }
    }

    override fun compare(@Nonnull o1: String, @Nonnull o2: String): Int {
        return cache.computeIfAbsent(o1) { version: String -> Version(version) }
            .compareTo(cache.computeIfAbsent(o2) { version: String -> Version(version) })
    }

    /**
     * Remove all the cached objects.
     * @since 0.1.0
     */
    fun clearCache() {
        cache.clear()
    }
}
