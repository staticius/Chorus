package org.chorus.event.server

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.network.protocol.DataPacket

class BatchPacketsEvent(val players: Array<Player>, val packets: Array<DataPacket>, val isForceSync: Boolean) :
    ServerEvent(), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
