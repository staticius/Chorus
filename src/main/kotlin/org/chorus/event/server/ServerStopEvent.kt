package org.chorus.event.server

import org.chorus.event.HandlerList

class ServerStopEvent : ServerEvent() {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
