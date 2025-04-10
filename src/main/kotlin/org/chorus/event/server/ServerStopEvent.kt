package org.chorus.event.server

import org.chorus.event.HandlerList

object ServerStopEvent : ServerEvent() {
    val handlers: HandlerList = HandlerList()
}
