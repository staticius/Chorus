package org.chorus.network.connection

fun interface BedrockSessionFactory {
    fun createSession(peer: BedrockPeer, subClientId: Int): BedrockSession?
}
