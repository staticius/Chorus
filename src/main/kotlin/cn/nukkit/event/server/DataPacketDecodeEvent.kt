package cn.nukkit.event.server

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.network.connection.netty.BedrockPacketWrapper

/**
 * @author MagicDroidX (Nukkit Project)
 */
class DataPacketDecodeEvent(val player: Player, val packetWrapper: BedrockPacketWrapper) : ServerEvent(), Cancellable {
    val packetId: Int
        get() = packetWrapper.packetId

    val packetBuffer: String
        get() = packetWrapper.packetBuffer.toString()

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
