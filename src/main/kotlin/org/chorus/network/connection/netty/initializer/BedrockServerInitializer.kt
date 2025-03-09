package org.chorus.network.connection.netty.initializer

import org.chorus.network.connection.BedrockPeer
import org.chorus.network.connection.BedrockSession

abstract class BedrockServerInitializer : BedrockChannelInitializer<BedrockSession>() {
    public override fun createSession0(peer: BedrockPeer?, subClientId: Int): BedrockSession {
        return BedrockSession(peer, subClientId)
    }
}
