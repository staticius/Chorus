package cn.nukkit.network.connection

fun interface BedrockSessionFactory {
    fun createSession(peer: BedrockPeer?, subClientId: Int): BedrockSession?
}
