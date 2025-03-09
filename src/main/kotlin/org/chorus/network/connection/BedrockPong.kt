package org.chorus.network.connection

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.cloudburstmc.netty.channel.raknet.RakServerChannel
import java.nio.charset.StandardCharsets
import java.util.*

class BedrockPong {
    var channel: RakServerChannel? = null
    var edition: String? = null
    var motd: String? = null
    var protocolVersion = -1
    var version: String? = null
    var playerCount = -1
    var maximumPlayerCount = -1
    var serverId: Long = 0
    var subMotd: String? = null
    var gameType: String? = null
    var nintendoLimited = false
    var ipv4Port = -1
    var ipv6Port = -1
    var extras: Array<String>? = null

    fun toByteBuf(): ByteBuf {
        val joiner = StringJoiner(";", "", ";")
            .add(this.edition)
            .add(toString(this.motd))
            .add(protocolVersion.toString())
            .add(toString(this.version))
            .add(playerCount.toString())
            .add(maximumPlayerCount.toString())
            .add(java.lang.Long.toUnsignedString(this.serverId))
            .add(toString(this.subMotd))
            .add(toString(this.gameType))
            .add(if (this.nintendoLimited) "0" else "1")
            .add(ipv4Port.toString())
            .add(ipv6Port.toString())
        this.extras?.forEach { extra ->
            joiner.add(extra)
        }

        return Unpooled.wrappedBuffer(joiner.toString().toByteArray(StandardCharsets.UTF_8))
    }

    fun update() {
        channel!!.config().setAdvertisement(this.toByteBuf())
    }

    companion object {
        private fun toString(string: String?): String {
            return string ?: ""
        }
    }
}
