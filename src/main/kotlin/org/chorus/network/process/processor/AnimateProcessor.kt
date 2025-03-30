package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.entity.item.EntityBoat
import org.chorus.event.player.PlayerAnimationEvent
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.AnimatePacket
import org.chorus.network.protocol.AnimatePacket.Action
import org.chorus.network.protocol.ProtocolInfo

class AnimateProcessor : DataPacketProcessor<AnimatePacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: AnimatePacket) {
        val player = playerHandle.player
        if (!player.spawned || !player.isAlive()) {
            return
        }

        var animation = pk.action

        // prevent client send illegal packet to server and broadcast to other client and make other client crash
        if (animation == Action.WAKE_UP || animation == Action.CRITICAL_HIT || animation == Action.MAGIC_CRITICAL_HIT
        ) {
            return
        }

        val animationEvent = PlayerAnimationEvent(player, pk)
        Server.instance.pluginManager.callEvent(animationEvent)
        if (animationEvent.isCancelled) {
            return
        }
        animation = animationEvent.animationType

        when (animation) {
            Action.ROW_RIGHT, Action.ROW_LEFT -> {
                val actionData = pk.actionData as Action.RowingData
                val riding = player.riding
                if (riding is EntityBoat) {
                    riding.onPaddle(animation, actionData.rowingTime)
                }
                return
            }
            else -> {}
        }

        if (animationEvent.animationType == Action.SWING_ARM) {
            player.setItemCoolDown(PlayerHandle.noShieldDelay, "shield")
        }

        Server.broadcastPacket(
            player.getViewers().values, AnimatePacket(
                targetRuntimeID = player.getRuntimeID(),
                action = animationEvent.animationType,
                actionData = null,
            )
        )
    }

    override val packetId: Int
        get() = ProtocolInfo.ANIMATE_PACKET
}
