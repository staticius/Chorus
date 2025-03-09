package org.chorus.event.server

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.network.protocol.DataPacket

/**
 * @author MagicDroidX (Nukkit Project)
 */
class DataPacketSendEvent(val player: Player, val packet: DataPacket) : ServerEvent(), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
