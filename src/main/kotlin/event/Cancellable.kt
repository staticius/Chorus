package org.chorus_oss.chorus.event

interface Cancellable {
    var isCancelled: Boolean

    fun cancelled = true
}
