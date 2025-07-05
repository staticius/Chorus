package org.chorus_oss.chorus.network.process.handler

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.network.connection.BedrockSession
import org.chorus_oss.chorus.network.protocol.PacketHandler

open class BedrockSessionPacketHandler(protected val session: BedrockSession) : PacketHandler {
    protected val player: Player? = session.player
}
