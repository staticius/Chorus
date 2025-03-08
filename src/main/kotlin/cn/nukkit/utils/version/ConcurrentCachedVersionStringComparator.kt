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
package cn.nukkit.utils.version

import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import javax.annotation.Nonnull

/**
 * A comparator that compares two strings as two [Version] objects but caching the object
 * to avoid parsing the same string multiple times. The cache size is not limited but the cached objects will
 * referenced weakly and will be collected by the garbage collector when not used.
 *
 * **This implementation is thread safe** because the cache is stored in a [ConcurrentHashMap].
 * @author joserobjr
 * @since 0.1.0
 */
class ConcurrentCachedVersionStringComparator
/**
 * Creates a comparator with an empty cache that will have a population limited by the given size.
 * @param gcFrequency How frequent the comparator will remove the objects that got cleared by the garbage collector
 * from the cache. `0` makes it clear in all calls and negative disables the automatic cleanup.
 * @since 0.1.0
 */(
    /**
     * How frequent the comparator will remove the objects that got cleared by the garbage collector from the cache,
     * `0` makes it clear in all calls and negative disables the automatic cleanup.
     * @since 0.1.0
     */
    private val gcFrequency: Int
) : VersionStringComparator() {
    /**
     * The map that holds all cached instances. It may contains values that got removed by the garbage collector.
     * @since 0.1.0
     */
    private val cache = ConcurrentHashMap<String, WeakReference<Version?>>()

    /**
     * The number of comparisons that was done since the last cleanup.
     * @since 0.1.0
     */
    private val comparisons = AtomicInteger()

    override fun compare(@Nonnull o1: String, @Nonnull o2: String): Int {
        if (gcFrequency >= 0 && comparisons.getAndIncrement() == gcFrequency) {
            removeGarbageCollected()
        }
        return getVersion(o1).compareTo(getVersion(o2))
    }

    /**
     * Remove all cached objects that was already cleared by garbage collector.
     */
    fun removeGarbageCollected() {
        cache.values.removeIf { ref: WeakReference<Version?> -> ref.get() == null }
        comparisons.set(0)
    }

    /**
     * Gets or create the cached [Version] object for the given version string
     * @param versionString The version string
     * @return A cached [Version] object
     */
    @Nonnull
    private fun getVersion(@Nonnull versionString: String): Version {
        var version: Version? = null
        val reference = cache[versionString]
        if (reference != null) {
            version = reference.get()
        }
        if (version == null) {
            version = Version(versionString)
            cache[versionString] = WeakReference(version)
        }
        return version
    }

    /**
     * Remove all the cached objects.
     * @since 0.1.0
     */
    fun clearCache() {
        cache.clear()
    }
}
