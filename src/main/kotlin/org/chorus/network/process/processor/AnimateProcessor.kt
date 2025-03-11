package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.entity.item.EntityBoat
import org.chorus.entity.item.EntityBoat.onPaddle
import org.chorus.event.player.PlayerAnimationEvent
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.AnimatePacket
import org.chorus.network.protocol.ProtocolInfo

class AnimateProcessor : DataPacketProcessor<AnimatePacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: AnimatePacket) {
        var pk = pk
        val player = playerHandle.player
        if (!player.spawned || !player.isAlive()) {
            return
        }

        var animation = pk.action

        // prevent client send illegal packet to server and broadcast to other client and make other client crash
        if (animation == null // illegal action id
            || animation == AnimatePacket.Action.WAKE_UP // these actions are only for server to client
            || animation == AnimatePacket.Action.CRITICAL_HIT || animation == AnimatePacket.Action.MAGIC_CRITICAL_HIT
        ) {
            return
        }

        val animationEvent = PlayerAnimationEvent(player, pk)
        Server.instance.getPluginManager().callEvent(animationEvent)
        if (animationEvent.isCancelled) {
            return
        }
        animation = animationEvent.animationType

        when (animation) {
            AnimatePacket.Action.ROW_RIGHT, AnimatePacket.Action.ROW_LEFT -> {
                if (player.riding is EntityBoat) {
                    riding.onPaddle(animation, pk.rowingTime)
                }
                return
            }
        }

        if (animationEvent.animationType == AnimatePacket.Action.SWING_ARM) {
            player.setItemCoolDown(PlayerHandle.getNoShieldDelay(), "shield")
        }

        pk = AnimatePacket()
        pk.eid = player.getId()
        pk.action = animationEvent.animationType
        pk.rowingTime = animationEvent.rowingTime
        Server.broadcastPacket(player.getViewers().values, pk)
    }

    override val packetId: Int
        get() = ProtocolInfo.ANIMATE_PACKET
}
