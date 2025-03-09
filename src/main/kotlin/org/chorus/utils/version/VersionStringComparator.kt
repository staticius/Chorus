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
 * A comparator that compares two strings as two [cn.nukkit.utils.version.Version] objects.
 * @author joserobjr
 * @since 0.1.0
 */
open class VersionStringComparator : Comparator<String> {
    /**
     * Compare two strings as two [cn.nukkit.utils.version.Version] objects.
     * @param o1 The first version string
     * @param o2 The second version string
     * @return `-1`, `0`, or `1` if the first is older, equals, or newer than the second version
     * @since 0.1.0
     */
    override fun compare(@Nonnull o1: String, @Nonnull o2: String): Int {
        return Version(o1).compareTo(Version(o2))
    }

    companion object {
        /**
         * A common instance that will be created when necessary.
         * @since 0.1.0
         */
        @get:Nonnull
        var instance: VersionStringComparator? = null
            /**
             * A common instance of a simple version comparator
             * @return The common instance, creates it on first call
             * @since 0.1.0
             */
            get() {
                if (field == null) {
                    field = VersionStringComparator()
                }
                return field
            }
            private set
    }
}
