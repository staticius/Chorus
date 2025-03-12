package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.entity.EntityRideable
import org.chorus.entity.custom.CustomEntity
import org.chorus.entity.item.EntityChestBoat
import org.chorus.entity.item.EntityItem
import org.chorus.entity.item.EntityXpOrb
import org.chorus.entity.mob.animal.EntityHorse
import org.chorus.entity.projectile.abstract_arrow.EntityArrow
import org.chorus.event.player.PlayerHackDetectedEvent
import org.chorus.event.player.PlayerKickEvent
import org.chorus.event.player.PlayerMouseOverEntityEvent
import org.chorus.lang.BaseLang.tr
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.InteractPacket
import org.chorus.network.protocol.ProtocolInfo


class InteractProcessor : DataPacketProcessor<InteractPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: InteractPacket) {
        val player = playerHandle.player
        if (!player.spawned || !player.isAlive()) {
            return
        }

        val targetEntity = player.level!!.getEntity(pk.target)

        if (targetEntity == null || !player.isAlive() || !targetEntity.isAlive()) {
            return
        }

        if (targetEntity is EntityItem || targetEntity is EntityArrow || targetEntity is EntityXpOrb) {
            // 自定义实体在客户端中可以互动, 所以不踢出玩家
            if (targetEntity is CustomEntity) {
                return
            }

            val event = PlayerHackDetectedEvent(player, PlayerHackDetectedEvent.HackType.INVALID_PVE)
            Server.instance.pluginManager.callEvent(event)

            if (event.isKick) player.kick(
                PlayerKickEvent.Reason.INVALID_PVE,
                "Attempting to interact with an invalid entity"
            )

            InteractProcessor.log.warn(
                Server.instance.getLanguage().tr("nukkit.player.invalidEntity", player.getName())
            )
            return
        }

        when (pk.action) {
            InteractPacket.ACTION_MOUSEOVER -> {
                if (pk.target == 0L) {
                    return
                }
                Server.instance.pluginManager.callEvent(PlayerMouseOverEntityEvent(player, targetEntity))
            }

            InteractPacket.ACTION_VEHICLE_EXIT -> {
                if (targetEntity !is EntityRideable || player.riding == null) {
                    return
                }
                (player.riding as EntityRideable).dismountEntity(player)
            }

            InteractPacket.ACTION_OPEN_INVENTORY -> {
                if (targetEntity is EntityRideable) {
                    if (targetEntity is EntityChestBoat) {
                        player.addWindow(targetEntity.getInventory())
                        return
                    } else if (targetEntity is EntityHorse) {
                        if (targetEntity.hasOwner(false) && targetEntity.getOwnerName() == player.getName()) {
                            player.addWindow(targetEntity.getInventory())
                            return
                        }
                    }
                } else if (targetEntity.getId() != player.getId()) {
                    return
                }
                if (!playerHandle.inventoryOpen) {
                    playerHandle.inventoryOpen = player.getInventory().open(player)
                }
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.INTERACT_PACKET
}
