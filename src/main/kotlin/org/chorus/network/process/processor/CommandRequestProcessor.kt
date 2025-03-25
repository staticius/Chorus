package org.chorus.network.process.processor

import com.google.common.util.concurrent.RateLimiter
import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.event.player.PlayerCommandPreprocessEvent
import org.chorus.event.player.PlayerHackDetectedEvent
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.CommandRequestPacket
import org.chorus.network.protocol.ProtocolInfo
import java.util.concurrent.TimeUnit

class CommandRequestProcessor : DataPacketProcessor<CommandRequestPacket>() {
    val rateLimiter: RateLimiter = RateLimiter.create(500.0)

    override fun handle(playerHandle: PlayerHandle, pk: CommandRequestPacket) {
        val length = pk.command!!.length
        if (!rateLimiter.tryAcquire(length, 300, TimeUnit.MILLISECONDS)) {
            val event = PlayerHackDetectedEvent(playerHandle.player, PlayerHackDetectedEvent.HackType.COMMAND_SPAM)
            Server.instance.pluginManager.callEvent(event)

            if (event.isKick) playerHandle.player.session.close("kick because hack")
            return
        }
        if (!playerHandle.player.spawned || !playerHandle.player.isAlive()) {
            return
        }
        val playerCommandPreprocessEvent = PlayerCommandPreprocessEvent(playerHandle.player, pk.command)
        Server.instance.pluginManager.callEvent(playerCommandPreprocessEvent)
        if (playerCommandPreprocessEvent.isCancelled) {
            return
        }
        Server.instance.executeCommand(playerCommandPreprocessEvent.player!!, playerCommandPreprocessEvent.message!!)
    }

    override val packetId: Int
        get() = ProtocolInfo.COMMAND_REQUEST_PACKET
}
