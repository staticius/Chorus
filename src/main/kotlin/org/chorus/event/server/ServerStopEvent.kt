package org.chorus.event.server

import org.chorus.event.HandlerList

/**
 * @author NycuRO (NukkitX Project)
 */
object ServerStopEvent : ServerEvent() {
    val handlers: HandlerList = HandlerList()
}
