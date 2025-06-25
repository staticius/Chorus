package org.chorus_oss.chorus.experimental.network.process.handler

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.network.connection.BedrockSession
import org.chorus_oss.chorus.network.protocol.PacketHandler

open class SessionHandler(protected val session: BedrockSession) : PacketHandler {
    protected val player: Player? = session.player
}
