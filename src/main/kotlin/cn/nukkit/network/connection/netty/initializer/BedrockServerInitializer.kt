package cn.nukkit.network.connection.netty.initializer

import cn.nukkit.network.connection.BedrockPeer
import cn.nukkit.network.connection.BedrockSession

abstract class BedrockServerInitializer : BedrockChannelInitializer<BedrockSession>() {
    public override fun createSession0(peer: BedrockPeer?, subClientId: Int): BedrockSession {
        return BedrockSession(peer, subClientId)
    }
}
