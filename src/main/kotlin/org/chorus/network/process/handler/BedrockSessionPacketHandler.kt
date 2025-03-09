package org.chorus.network.process.handler

import cn.nukkit.Player
import cn.nukkit.PlayerHandle
import cn.nukkit.network.connection.BedrockSession
import cn.nukkit.network.protocol.DisconnectPacket
import cn.nukkit.network.protocol.PacketHandler

open class BedrockSessionPacketHandler(protected val session: BedrockSession) : PacketHandler {
    protected val player: Player? = session.player
    protected val handle: PlayerHandle? = session.handle

    override fun handle(pk: DisconnectPacket) {
        player?.close(pk.message)
    }
}
