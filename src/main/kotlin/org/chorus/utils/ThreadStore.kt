package org.chorus.utils

import java.util.concurrent.ConcurrentHashMap

/**
 * @author MagicDroidX (Nukkit Project)
 */
object ThreadStore {
    val store: Map<String, Any> = ConcurrentHashMap()
}
