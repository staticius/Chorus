package org.chorus.plugin

import org.chorus.event.Event
import org.chorus.event.Listener
import org.chorus.utils.EventException

/**
 * @author iNevet (Nukkit Project)
 */
interface EventExecutor {
    @Throws(EventException::class)
    fun execute(listener: Listener?, event: Event)
}
