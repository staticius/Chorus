package org.chorus

import org.chorus.level.PlayerChunkManager
import org.chorus.network.connection.BedrockSession
import org.chorus.network.protocol.types.PlayerInfo

class TestPlayer(session: BedrockSession, info: PlayerInfo) : Player(session, info) {
    val playerChunkManager: PlayerChunkManager
        get() = playerChunkManager
}
