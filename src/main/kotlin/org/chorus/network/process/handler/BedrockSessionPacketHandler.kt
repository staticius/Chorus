package org.chorus.network.process.handler

import org.chorus.Player
import org.chorus.PlayerHandle
import org.chorus.network.connection.BedrockSession
import org.chorus.network.protocol.DisconnectPacket
import org.chorus.network.protocol.PacketHandler

open class BedrockSessionPacketHandler(protected val session: BedrockSession) : PacketHandler {
    protected val player: Player? = session.player
    protected val handle: PlayerHandle? = session.handle

    override fun handle(pk: DisconnectPacket) {
        player?.close(pk.message)
    }
}
