package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Event
import org.chorus.event.HandlerList


class PlayerCreationEvent(@JvmField var playerClass: Class<out Player>) : Event() {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
