package cn.nukkit.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.event.player.PlayerCommandPreprocessEvent
import cn.nukkit.event.player.PlayerHackDetectedEvent
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.CommandRequestPacket
import cn.nukkit.network.protocol.ProtocolInfo
import com.google.common.util.concurrent.RateLimiter
import java.util.concurrent.TimeUnit

class CommandRequestProcessor : DataPacketProcessor<CommandRequestPacket>() {
    val rateLimiter: RateLimiter = RateLimiter.create(500.0)

    override fun handle(playerHandle: PlayerHandle, pk: CommandRequestPacket) {
        val length = pk.command.length
        if (!rateLimiter.tryAcquire(length, 300, TimeUnit.MILLISECONDS)) {
            val event = PlayerHackDetectedEvent(playerHandle.player, PlayerHackDetectedEvent.HackType.COMMAND_SPAM)
            playerHandle.player.getServer().getPluginManager().callEvent(event)

            if (event.isKick) playerHandle.player.session.close("kick because hack")
            return
        }
        if (!playerHandle.player.spawned || !playerHandle.player.isAlive()) {
            return
        }
        val playerCommandPreprocessEvent = PlayerCommandPreprocessEvent(playerHandle.player, pk.command)
        playerHandle.player.getServer().getPluginManager().callEvent(playerCommandPreprocessEvent)
        if (playerCommandPreprocessEvent.isCancelled) {
            return
        }
        playerHandle.player.getServer()
            .executeCommand(playerCommandPreprocessEvent.player, playerCommandPreprocessEvent.message)
    }

    override val packetId: Int
        get() = ProtocolInfo.COMMAND_REQUEST_PACKET
}
