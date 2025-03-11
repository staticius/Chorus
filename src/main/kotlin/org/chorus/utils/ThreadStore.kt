package org.chorus.utils

import java.util.concurrent.ConcurrentHashMap


object ThreadStore {
    val store: MutableMap<String, Any> = ConcurrentHashMap()
}
