package org.chorus.event.server

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.network.connection.netty.BedrockPacketWrapper


class DataPacketDecodeEvent(val player: Player, val packetWrapper: BedrockPacketWrapper) : ServerEvent(), Cancellable {
    val packetId: Int
        get() = packetWrapper.packetId

    val packetBuffer: String
        get() = packetWrapper.packetBuffer.toString()

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
