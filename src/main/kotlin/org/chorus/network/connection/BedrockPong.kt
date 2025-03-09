package org.chorus.network.connection

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import lombok.Data
import lombok.experimental.Accessors
import org.cloudburstmc.netty.channel.raknet.RakServerChannel
import java.nio.charset.StandardCharsets
import java.util.*

@Data
@Accessors(chain = true, fluent = true)
class BedrockPong {
    val channel: RakServerChannel? = null
    val edition: String? = null
    val motd: String? = null
    private val protocolVersion = -1
    private val version: String? = null
    val playerCount = -1
    val maximumPlayerCount = -1
    private val serverId: Long = 0
    val subMotd: String? = null
    val gameType: String? = null
    private val nintendoLimited = false
    private val ipv4Port = -1
    private val ipv6Port = -1
    private val extras: Array<String>?

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
        if (this.extras != null) {
            for (extra in this.extras) {
                joiner.add(extra)
            }
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
