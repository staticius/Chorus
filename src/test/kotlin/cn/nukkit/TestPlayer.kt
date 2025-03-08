package cn.nukkit

import cn.nukkit.level.PlayerChunkManager
import cn.nukkit.network.connection.BedrockSession
import cn.nukkit.network.protocol.types.PlayerInfo

class TestPlayer(session: BedrockSession, info: PlayerInfo) : Player(session, info) {
    val playerChunkManager: PlayerChunkManager
        get() = playerChunkManager
}
